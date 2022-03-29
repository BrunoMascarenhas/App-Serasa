package br.com.mercante.api.repository

import br.com.mercante.api.model.Filial
import br.com.mercante.api.model.Prestacao
import br.com.mercante.api.model.dto.ResumoTitulosVencidosDto
import br.com.mercante.api.model.identities.PrestacaoId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.*

interface PrestacaoRepository : JpaRepository<Prestacao, PrestacaoId> {

    /**
     * Realiza a busca por todos os titulos vencidos em x dias de um cliente para os tipos de cobranças informadas
     * @param [codigo] codigo do cliente a ser buscados os títulos
     * @param [diasVencido] quantidade de dias que os títulos estão vencidos
     * @param [cobrancas] uma lista contento os tipos de cobranças que serão filtradas
     *
     * @return uma lista com as títulos vencidos
     * */
    @Query("""SELECT
        p.CODCLI as codigo,
        (SELECT C.CLIENTE FROM PCCLIENT C WHERE C.CODCLI = P.CODCLI) as nome,
        sum(p.VALOR) as valor,
        count(distinct p.PREST || p.NUMTRANSVENDA) as quantidade
        FROM PCPREST p
        WHERE p.CODCLI = :codigo
            AND p.DTPAG IS NULL
            AND p.DTVENC <= sysdate - :diasVencido
            AND p.CODCOB in (:cobrancas)
        GROUP BY p.CODCLI""", nativeQuery = true)
    fun buscarTitulosVencidos(@Param("codigo") codigo: Long,
                              @Param("diasVencido") diasVencido: Int,
                              @Param("cobrancas") cobrancas: List<String>): Optional<ResumoTitulosVencidosDto>

    /**
     * Busca todos os títulos que estão vencidos entre uma data base e de determiada cobrança, para que sejam enviados ao
     * serasa.
     * @param [filial] a filial que os títulos serão identificados
     * @param [dataInicial] a data de vencimento inicial
     * @param [dataFinal] data de vencimento final
     * @param [cobrancas] lista com as cobranças dos títulos que serão enviados
     *
     * @return uma lista com os títulos vencidos
     * */
    @Query("""SELECT p FROM Prestacao p
        INNER JOIN p.cliente c
        WHERE p.filial = :filial
        AND c.enviarDadosSerasa = 'S'
        AND p.dataPagamento IS NULL
        AND p.dataVencimento between :dataInicial and :dataFinal
        AND p.dataEnvioSerasa IS NULL
        AND p.valor between  10 AND 9999999
        AND p.cobranca in (:cobrancas)""")
    fun buscarTitulosIncluirNoSerasa(filial: Filial, dataInicial: LocalDate, dataFinal: LocalDate,
                                     cobrancas: List<String>): List<Prestacao>

    /**
     * Busca todos os títulos que foram enviados ao serasa que já foram pagos e não foram removidos do serasa
     *
     * @param [filial] a filial dos títulos
     * @param [data] a data de vencimento
     * @param [cobrancas] lista com as cobranças dos títulos que serão enviados
     *
     * @return uma lista com os títulos a serem removidos da serasa
     * */
    @Query("""SELECT p FROM Prestacao p
        INNER JOIN p.cliente c
        WHERE p.filial = :filial
        AND c.enviarDadosSerasa = 'S'
        AND p.dataPagamento IS NOT NULL
        AND p.dataVencimento < :data
        AND p.dataEnvioSerasa IS NOT NULL
        AND p.dataRetiradaSerasa IS NULL
        AND p.valorPago > 0
        AND p.valor BETWEEN 10 AND 9999999
        AND p.cobranca IN (:cobrancas)""")
    fun buscarTitulosRemoverDoSerasa(filial: Filial, data: LocalDate, cobrancas: List<String>): List<Prestacao>
}