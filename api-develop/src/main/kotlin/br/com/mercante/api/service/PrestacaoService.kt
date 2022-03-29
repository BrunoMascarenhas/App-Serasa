package br.com.mercante.api.service

import br.com.mercante.api.model.Filial
import br.com.mercante.api.model.Prestacao
import br.com.mercante.api.model.serasapefin.OperacaoPefin
import br.com.mercante.api.repository.PrestacaoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PrestacaoService(
        private val prestacaoRepository: PrestacaoRepository
) {

    /**
     * Função responsável por gerar a lista de títulos que serão incluídos no serasa, por inadiplência de uma determinada data
     * @param [filial] a filial que os títulos serão identificados
     * @param [numDiasVencido] quantidade de dias que o título vencido será enviado para o serasa
     * @param [cobrancas] lista com as cobranças dos títulos que serão enviados
     *
     * @return uma lista contendo os títulos a serem incluídos no serasa
     * */
    fun buscarTitulosIncluirSerasa(filial: Filial, numDiasVencido: Long, dataOperacao: LocalDate, cobrancas: List<String>): List<Prestacao> {
        val vencimentoInicial = dataOperacao.minusYears(5)
        val vencimentoFinal = dataOperacao.minusDays(numDiasVencido)

        log.debug("Buscando títulos vencidos entre $vencimentoInicial e $vencimentoFinal a serem incluidos do serasa")
        val titulos = this.prestacaoRepository.buscarTitulosIncluirNoSerasa(filial, vencimentoInicial, vencimentoFinal, cobrancas)
        log.debug("${titulos.size} foram encontrados")

        return titulos

    }

    /**
     * Função reposável por gerar a lista de títulos que já foram pagos e deverão ser removidos do serasa.
     *
     * @param [filial] a filial que os títulos serão identificados
     * @param [numDiasVencido] quantidade de dias que o título vencido será enviado para o serasa
     * @param [cobrancas] lista com as cobranças dos títulos que serão enviados
     *
     * @return uma lista contendo os títulos a serem removidos do serasa.
     * */
    fun bucarTitulosRemoverSerasa(filial: Filial, numDiasVencido: Long, dataOperacao: LocalDate, cobrancas: List<String>): List<Prestacao> {
        val vencimentoFinal = dataOperacao.minusDays(numDiasVencido)

        log.debug("Buscando títulos vencidos até $vencimentoFinal a serem removidos do serasa")
        val titulos = this.prestacaoRepository.buscarTitulosRemoverDoSerasa(filial, vencimentoFinal, cobrancas)
        log.debug("${titulos.size} foram encontrados")
        return titulos
    }

    /**
     * Atualiza o título com as informações de data de inclusão ou data de remoção do serasa.
     *
     * @param [prestacao] o titulo a ser atualizado
     * @param [operacao] o tipo de operação, se é inclusão ou remoção do serasa
     * @param [data] a data da operação
     * */
    fun atualizarTituloSerasa(prestacao: Prestacao, operacao: OperacaoPefin, data: LocalDate) {
        when (operacao) {
            OperacaoPefin.INCLUSAO -> prestacao.dataEnvioSerasa = data
            OperacaoPefin.EXCLUSAO -> prestacao.dataRetiradaSerasa = data
            else -> throw IllegalArgumentException("$operacao é uma operação desconhecida.")
        }

        this.prestacaoRepository.save(prestacao)
    }

    companion object {
        internal val log = LoggerFactory.getLogger(this::class.java)
    }
}