package br.com.mercante.api.service

import br.com.mercante.api.model.Filial
import br.com.mercante.api.model.Prestacao
import br.com.mercante.api.model.serasapefin.*
import br.com.mercante.api.repository.FilialRepository
import br.com.mercante.api.repository.SerasaConfigRepository
import br.com.mercante.api.repository.SerasaLogRepository
import br.com.mercante.api.utils.*
import com.ancientprogramming.fixedformat4j.format.impl.FixedFormatManagerImpl
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SerasaService(
        private val prestacaoService: PrestacaoService,
        private val filialRepository: FilialRepository,
        private val serasaLogRepository: SerasaLogRepository,
        private val serasaConfigRepository: SerasaConfigRepository,
        private val environment: Environment
) {
    /**
     * Função reponsável por gerar o arquivo cnab contendo os títulos vencidos por determinado período ao serasa.
     * A função obtém os títulos e processa os que serão inclídos e excluídos do serasa. Após o processamento, um
     * arquivo é gerado no diretório encontrado nas configurações do serasa.
     * O arquivo sendo gerado com sucesso, então os títulos são marcados como enviados ou retirados do serasa.
     * */
    fun processarSerasaPefin() {
        log.debug("Iniciando processamento de títulos serasa pefim")

        val configuracoes = serasaConfigRepository.getConfig()
        log.debug("Ultima remessa gerada: ${configuracoes.numUltimaRemessa} em: ${configuracoes.ultimaRemessa}")

        log.debug("Buscando filiais...")
        val filiais = this.filialRepository.findAllById(configuracoes.filiais)
        val data = LocalDate.now()

        filiais.forEach { filial ->
            log.debug("Buscando títulos para inclusão.")
            val titulosParaInclusao = prestacaoService.buscarTitulosIncluirSerasa(
                    filial, configuracoes.numdiasVencidos.toLong(), data, configuracoes.cobrancas)
            log.debug("${titulosParaInclusao.size} títulos encontrados.")
            log.debug("Buscando títulos para remoção.")
            val titulosParaRemover = prestacaoService.bucarTitulosRemoverSerasa(
                    filial, configuracoes.numdiasVencidos.toLong(), data, configuracoes.cobrancas)
            log.debug("${titulosParaRemover.size} títulos encontrados.")

            //Se nenhum título for encontrado salta o lopping
            if (titulosParaInclusao.isEmpty() && titulosParaRemover.isEmpty()) {
                log.debug("Nenhum título encontrado. Não será gerado arquivo")
                return@forEach
            }

            log.debug("Processamento Serasa/Pefin da filial ${filial.codigo} iniciado...")
            val arquivoPefin = CnabPefin()

            //Adiciona o header
            arquivoPefin.addRecord(this.gerarPefinHeader(filial, data))
            //Adiciona os títulos a serem incluídos no serasa
            log.debug("${titulosParaInclusao.size} títulos encontrado para inclusão.")
            titulosParaInclusao.forEach { arquivoPefin.addRecord(gerarPefinDetail(it, OperacaoPefin.INCLUSAO)) }
            //Adiciona os títulos a serem removidos do serasa
            log.debug("${titulosParaRemover.size} títulos encontrado para remover.")
            titulosParaRemover.forEach { arquivoPefin.addRecord(gerarPefinDetail(it, OperacaoPefin.EXCLUSAO)) }
            //Adiciona o trailler do arquivo
            arquivoPefin.addRecord(TraillerPefin())
            log.debug("Processamento Serasa/Pefin da filial ${filial.codigo} finalizado...")

            try {
                this.gerarArquivoPefin(arquivoPefin)
                /* Após a gravação do arquivo, gravar no títulos a data de envio e remoção serasa, salvar o log de processamento
                * e incrementar o número da remessa
                * Essa operação só pode ser efetuada caso haja sucesso na criação do arquivo.
                * */
                configuracoes.numUltimaRemessa = arquivoPefin.getHeader().numeroRemessa
                configuracoes.ultimaRemessa = LocalDateTime.now()

                this.serasaConfigRepository.save(configuracoes)
                this.executarEdiInbox()
                titulosParaInclusao.forEach {
                    this.prestacaoService.atualizarTituloSerasa(it, OperacaoPefin.INCLUSAO, data)
                    this.logTitulo(it, OperacaoPefin.INCLUSAO, configuracoes.numUltimaRemessa)
                }
                titulosParaRemover.forEach {
                    this.prestacaoService.atualizarTituloSerasa(it, OperacaoPefin.EXCLUSAO, data)
                    this.logTitulo(it, OperacaoPefin.EXCLUSAO, configuracoes.numUltimaRemessa)
                }
            } catch (ex: IOException) {
                log.error("Não foi possível gerar o arquivo. ${ex.message}", ex)
            }
        }
    }

    /**
     * Gera o header do pefin
     * @param [filial] os dados da filial
     * @param [data] data da operação
     *
     * @return o header do pefin
     * */
    private fun gerarPefinHeader(filial: Filial, data: LocalDate): HeaderPefin {
        log.debug("Gerando header")
        val numeroProxRemessa = this.serasaConfigRepository.buscarNumeroProximaRemessa()
        filial.run {
            return HeaderPefin().apply {
                cnpj = extrairPrimeiraParteCNPJ().toInt()
                dataMovimento = data.asDate()
                dddEmpresa = getDDDTelefone().toInt()
                telefoneEmpresa = getTelefoneSemDDD().toInt()
                ramalEmpresa = getRamal().toInt()
                contatoEmpresa = contato.removerAcentuacao()
                codigoEnvio = "E"
                numeroRemessa = numeroProxRemessa
            }
        }
    }

    /**
     * Converte um título em uma classe Detail
     *
     * @param [prestacao] o título que será convertido
     * @param [operacao] se é uma operação de inclusão ou remoção do serasa.
     *
     * @return o detail do perfin
     * */
    private fun gerarPefinDetail(prestacao: Prestacao, operacao: OperacaoPefin): DetailPefin {
        val cliente = prestacao.cliente
        val detail = DetailPefin()
        val filial = prestacao.filial

        log.debug("Gerando registro do titulo ${prestacao.duplicata}-${prestacao.id.prestacao}")

        detail.run {
            codigoOperacao = operacao.valor
            filialEDigito = filial.extrairUltimaParteCNPJ().toInt()

            prestacao.apply {
                dataOcorrencia = dataVencimento.asDate()
                dataTerminoContrato = dataVencimento.asDate()
                dataCompromisso = dataVencimento.asDate()
                naturezaOperacao = "NF"
                valorDevido = valor
                valorTotal = valor
                nossoNumero = 0
                numeroContrato = "$duplicata${id.prestacao}"
            }

            cliente.apply {
                tipoPessoaPrincipal = tipoPessoa
                tipoPrimeiroDocPrincipal = if (tipoPessoa == "J") "1" else "2"
                primeiroDocPrincipal = cgc.removerCaractersNaoNumericos().toLong()
                if (tipoPessoa == "F") {
                    if (rg != null && rg.isNotEmpty()) {
                        tipoSegundoDocPrincipal = "3"
                        segundoDocPrincipal = rg
                        ufPrinncipal = ""
                    }
                }
                nomeDevedor = nome.removerAcentuacao().cleanNotPrintableChars()
                nascDevedor = 0
                nomePai = ""
                nomeMae = ""
                val end = endereco.cleanNotPrintableChars().removerAcentuacao()
                        .plus(cliente.numero?.cleanNotPrintableChars()?.removerAcentuacao() ?: "")
                val sobraEndereco = end.drop(45)
                enderecoDevedor = end
                bairroDevedor = cliente.bairro.cleanNotPrintableChars().removerAcentuacao()
                cidadeDevedor = cidade.cleanNotPrintableChars().removerAcentuacao()
                ufDevedor = estado
                cepDevedor = cep.removerCaractersNaoNumericos().toInt()
                complementoEndereco = sobraEndereco
                dddDevedor = getDDDTelefone()
                telefoneDevedor = getTelefoneSemDDD()
            }

            if (operacao == OperacaoPefin.EXCLUSAO) {
                motivoBaixa = "01"
            }
            //Após realizar o preenchimento do detail, retorna sua instância
            return this
        }
    }

    /**
     * Exporta uma classe pefin para um arquivo de texto.
     *
     * @param [pefin] a classe pefin que sera convertida em arquivo de texto
     * */
    @Throws(IOException::class)
    private fun gerarArquivoPefin(pefin: CnabPefin) {
        val config = serasaConfigRepository.getConfig()

        /*O Nome do arquivo é constituido pelo prefixo REMPEFIN e o número da remessa gerada com 7 digitos.*/
        val fileName = "REMPEFIN".plus("%07d".format(pefin.getHeader().numeroRemessa)).plus(".txt")
        val file = File(config.diretorioExportacao, fileName)

        /*Se houver arquivo no local o mesmo deverá ser excluído.*/
        if (file.exists()) {
            file.delete()
        }

        log.debug("Exportando pefin para o arquivo ${file.name} em ${file.parent}")

        val manager = FixedFormatManagerImpl()

        val writer = file.bufferedWriter(Charsets.US_ASCII)
        writer.use { out ->
            pefin.records.forEach {
                out.write(manager.export(it))
                out.write("\r\n")
            }
        }

        log.debug("Arquivo ${file.absolutePath} gerado com sucesso!")
    }

    /**
     * Executa o script do ediinbox para fazer o envio e recebimento dos arquivos
     * */
    private fun executarEdiInbox() {
        if (environment.activeProfiles[0] == "prod") {
            Command.exec("cd /opt/Edi7Web/bin/ && sh ${this.serasaConfigRepository.getConfig().pathExecutavel}")
        }
    }

    /**
     * Grava o log do título na tabela de log
     *
     * @param [prestacao] a prestação a ser feito log
     * @param [operacao] a operação a ser logada.
     * @param [remessa] o número da remessa que gerou a operação
     * */
    fun logTitulo(prestacao: Prestacao, operacao: OperacaoPefin, remessa: Int) {
        val id = this.getProximoIdLogSerasa()
        val log = SerasaLog(codigo = id,
                remessa = remessa,
                operacao = operacao.valor,
                filial = prestacao.filial.codigo,
                data = LocalDateTime.now(),
                duplicata = prestacao.duplicata.toInt(),
                dataVenc = prestacao.dataVencimento,
                prestacao = prestacao.id.prestacao)

        serasaLogRepository.save(log)
    }

    /**
     * Obtém o proximo id que será utilizado no log de movimentação do serasa
     *
     * @return o id de log
     * */
    private fun getProximoIdLogSerasa(): Long {
        val id: Long = try {
            serasaLogRepository.getUltimoIdUsado()
        } catch (ex: EmptyResultDataAccessException) {
            1
        }

        return id + 1
    }

    companion object {
        internal val log = LoggerFactory.getLogger(this::class.java)
    }
}