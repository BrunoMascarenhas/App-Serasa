package br.com.mercante.api.service

import br.com.mercante.api.mail.MailService
import br.com.mercante.api.model.Cliente
import br.com.mercante.api.model.dto.ResumoTitulosVencidosDto
import br.com.mercante.api.repository.ClienteRepository
import br.com.mercante.api.repository.PrestacaoRepository
import br.com.mercante.api.utils.FileUtils
import br.com.mercante.api.utils.removerCaractersNaoNumericos
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.transaction.Transactional

@Service
class ClienteService(
        private val clienteRepository: ClienteRepository,
        private val prestacaoRepository: PrestacaoRepository,
        private val mailService: MailService) {

    /**
     * Função responsável por zerar o limite de clientes que tem títulos vencidos a mais que x dias.
     * Após realizar a busca e zerar o limite do cliente é executada a função para enviar um email ao responsável da
     * cobrança, para que a mesma fique ciente do procedimento.
     * */
    @Async
    fun bloquearClientesComLimiteVencido() {
        val cobrancas = listOf("001", "2377", "033", "104", "341", "CHD3", "CHP", "CHDV", "CHD1")
        val diasVencido = 30
        val limite = 0f

        log.debug("Iniciando verificação dos clientes que tem limite maior que 0,00 e estão ativos.")
        val clientes = this.clienteRepository.buscarClientesAtivosComLimiteMaiorQue(limite)
        log.debug("Encontrado ${clientes.size} clientes.")

        log.debug("Iniciando verificação dos títulos vencidos.")
        val resumosProcessados = clientes.mapNotNull {
            val resumoTitulos = prestacaoRepository.buscarTitulosVencidos(it.codigo, diasVencido, cobrancas)

            if (resumoTitulos.isPresent) {
                val resumo = resumoTitulos.get()
                log.debug("O cliente ${it.codigo} tem ${resumo.quantidade} titulos vencidos no valor de ${resumo.valor}")
                this.removerPrazoELimiteDoCliente(it)
                return@mapNotNull resumo
            } else {
                return@mapNotNull null
            }
        }

        log.debug("Finalizando verificação dos títulos vencidos. ${resumosProcessados.size} clientes encontrados.")
        enviarRelatorioDeTitulosVencidos(resumosProcessados)
    }

    /**
     * Função responsável em alterar o plano de pagamento, cobrança e limite do cliente. Usa a uma annotation @Transactional
     * para prevenir que se uma operação não haja sucesso ele faça o rollback automaticamente.
     *
     * @param [cliente] o cliente a ser alterado
     * */
    @Transactional
    fun removerPrazoELimiteDoCliente(cliente: Cliente) {
        val limite = 0f
        val planoPagamento = 1
        val cobranca = "DH"
        log.debug("Alterando limite {de: ${cliente.limiteDeCredito}, para: $limite} cobranca {de :${cliente.codigoCobranca}" +
                ", para: $cobranca} plano de pagamento {de: ${cliente.codigoPlanoPagamento}, para: $planoPagamento} do cliente ${cliente.codigo}.")
        this.clienteRepository.alterarLimite(limite, cliente.codigo)
        //this.clienteRepository.alterarCobranca(cobranca, cliente.codigo)
        //this.clienteRepository.alterarPlanoPagamento(planoPagamento, cliente.codigo)
    }

    /**
     * Função resposável por enviar o relatório de títulos vencidos por email.
     *
     * @param [relatorios] lista de titulos vencidos
     * */
    private fun enviarRelatorioDeTitulosVencidos(relatorios: List<ResumoTitulosVencidosDto>) {
        val subject = "Clientes com limite zerado ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
        val template = "mail/clientes_limite_zerado.html"
        val params = HashMap<String, Any>()
        params["date"] = LocalDate.now()
        params["relatorios"] = relatorios

        val to = arrayOf("leila.silva@mercante.com.br", "auditoria-financeiro@mercante.com.br", "danilo.pires@mercante.com.br", "bruno.silva@mercante.com.br")

        this.mailService.sendMailWithTemplate(to, subject, template, params)
    }

    /**
     * Função resposável por realizar a validação dos clientes junto a sefaz, após realizar o processamento
     * envia um email com o resultado.

    @Async
    fun processarValidacaoSefaz() {
        log.debug("Iniciando validação de clientes sefaz.")
        val desbloqueados = processarArquivoSefaz("contrbpjativo", false)
        val bloqueados = processarArquivoSefaz("contrbpjinativo", true)
        bloqueados.addAll(processarArquivoSefaz("contrbpjoutro", true))

        enviarRelatorioClientesProcessadosSefaz(bloqueados, desbloqueados)
    }
    **/
    /**
     * Realiza o processamento do arquivo sefaz, faz o download do arquivo, extrai e depois converte em string para ser analisado
     * Verifica se os clientes no sistema estão contindo na lista da sefaz e depois realiza a alteração do cadastro,
     * caso ele esteja no banco de dados.
     * @param [arquivo] string contendo o nome do arquivo a ser processado
     * @param [isBloquear] se true a ação a ser realizada é bloquear
     *
     * @return lista com os clientes que foram encontrado nos arquivo da sefaz.

    private fun processarArquivoSefaz(arquivo: String, isBloquear: Boolean): ArrayList<Cliente> {
        val url = "https://sefaz.ba.gov.br/contribuinte/legislacao/download/$arquivo.exe"
        val destino = "/opt/mercante/api/sefaz/$arquivo"
        log.debug("Buscando clientes a serem ${if (isBloquear) "bloqueados" else "desbloqueados"}.")
        //obtem a lista de clientes a serem processado
        val clientes: ArrayList<Cliente> = if (isBloquear) {
            this.clienteRepository.buscarClientesAtivosSefaz()
        } else {
            this.clienteRepository.buscarClientesBloqueadosSefaz()
        }
        log.debug("${clientes.size} clientes encontrados")
        //realiza o download do arquivo
        val path = FileUtils.downloadFile(url, "$destino.exe")
        //extrair arquivo
        val arquivoExtraido = FileUtils.extractFileExeSefaz(path)
        //converte o arquivo extraido
        val cnpjsSefaz = converterArquivoCnpjParaLista(arquivoExtraido)

        val clientesProcessados = arrayListOf<Cliente>()
        log.debug("Iniciando o processamento do arquivo $url")
        //verifica se os clientes estão no arquivo da sefaz
        clientes.forEach { cliente ->
            val opt = buscarClienteComCnpjNaLista(cliente, cnpjsSefaz)
            if (opt.isPresent) {
                val cli = opt.get()
                clientesProcessados.add(cli)
                alterarStatusSefaz(cli, isBloquear)
            }
        }

        log.debug("${clientesProcessados.size} clientes ${if (isBloquear) "bloqueado" else "desbloqueado"}.")
        return clientesProcessados
    }
/**

     * Resposável por alterar o status do cliente na sefaz, ativando ou bloqueando
     * @param [cliente] o cliente a ser alterado
     * @param [bloquear] se for true a ação realizada é a de bloquear, caso contrário será desbloquear
     *
     * */
    private fun alterarStatusSefaz(cliente: Cliente, bloquear: Boolean) {
        val acao = if (bloquear) 'S' else 'N'
        cliente.bloqueioSefaz = acao
        cliente.bloqueioSefazPed = acao

        this.clienteRepository.save(cliente)
        log.debug("Cliente ${cliente.codigo} ${if (bloquear) "bloqueado" else "desbloqueado"} com sucesso.")
    }

    /**
     * Esta função verifica e retorna um cliente caso o cnpj dele esteja contido na lista passada por parâmetros.
     *
     * @param [cliente] o cliente a ser analisado
     * @param [cnpjsSefaz] uma lista contendo todos os cnpjs a serem verificados
     *
     * @return um optional, contendo o resultado da busca
     * */
    @Async
    fun buscarClienteComCnpjNaLista(cliente: Cliente, cnpjsSefaz: List<String>): Optional<Cliente> {
        if (cnpjsSefaz.contains(cliente.cgc.removerCaractersNaoNumericos()))
            return Optional.of(cliente)
        return Optional.empty()
    }

    /**
     * Obtem um arquivo e trasforma para uma lista de String com os cnpjs
     * @param [file] arquivo a ser convertido
     *
     * @return uma lista com os itens extraidos do arquivo
     * */
    private fun converterArquivoCnpjParaLista(file: File): List<String> {
        log.debug("Convertendo arquivo ${file.path} para lista.")
        val itens = arrayListOf<String>()

        if (!file.exists())
            throw FileNotFoundException("Arquivo ${file.path} não encontrado.")

        file.forEachLine(Charset.forName("utf-16le"), action = {
            val line = it.trim()
            if (line.isBlank())
                return@forEachLine

            itens.add(it.take(14))
        })
        //apos converter o arquivo, faz o download do arquivo utilizado
        file.delete()

        return itens
    }

    /**
     * Realiza o envio do relatório dos clientes processados por email. Contendo os dados de clientes bloqueados e desbloqueados
     *
     * @param [bloqueados] lista com clientes que foram bloqueados
     * @param [desbloqueados] lista com clientes que foram desbloqueados
     * */
    private fun enviarRelatorioClientesProcessadosSefaz(bloqueados: ArrayList<Cliente>, desbloqueados: ArrayList<Cliente>) {
        val subject = "Relatório processamento clientes SEFAZ ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}"
        val to = arrayOf("bruno.silva@mercante.com.br", "cadastro@mercante.com.br", "assistentelogistica@mercante.com.br")
        val template = "mail/clientes-processados-sefaz.html"
        val params = HashMap<String, Any>()
        params["date"] = LocalDate.now()
        params["desbloqueados"] = desbloqueados
        params["bloqueados"] = bloqueados

        this.mailService.sendMailWithTemplate(to, subject, template, params)
    }**/

    companion object {
        internal val log = LoggerFactory.getLogger(this::class.java)
    }
}