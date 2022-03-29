package br.com.mercante.api.repository

import br.com.mercante.api.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.transaction.Transactional

interface ClienteRepository : JpaRepository<Cliente, Long> {

    @Query("select c from Cliente c where c.dataExclusao is null and c.limiteDeCredito > :limite")
    fun buscarClientesAtivosComLimiteMaiorQue(limite: Float): List<Cliente>

    @Modifying
    @Transactional
    @Query("update Cliente set limiteDeCredito = :limite where codigo = :codigoCliente")
    fun alterarLimite(@Param("limite") novoLimite: Float,
                      @Param("codigoCliente") codigoCliente: Long)

    @Modifying
    @Transactional
    @Query("update Cliente set codigoCobranca = :cobranca where codigo = :codigoCliente")
    fun alterarCobranca(@Param("cobranca") novaCobranca: String,
                        @Param("codigoCliente") codigoCliente: Long)

    @Modifying
    @Transactional
    @Query("update Cliente set codigoPlanoPagamento = :planoDePagamento where codigo = :codigoCliente")
    fun alterarPlanoPagamento(@Param("planoDePagamento") codigoPlanoPagamento: Int,
                              @Param("codigoCliente") codigoCliente: Long)

    @Query("SELECT * FROM PCCLIENT where regexp_replace(CGCENT, '\\D') = :cgc", nativeQuery = true)
    fun buscarClientePorCGC(@Param("cgc") cgc: String): List<Cliente>

    /** @Query("""select c from Cliente c
        where c.bloqueioSefazPed = 'N'
          and c.bloqueioSefaz = 'N'
          and c.dataExclusao is null
          and c.inscricao not like '%ISENTO%'
          and c.estado = 'BA'
    """)
    fun buscarClientesAtivosSefaz(): ArrayList<Cliente>
     **/

    /** @Query("""
        select c from Cliente c
        where c.bloqueioSefazPed = 'S'
          and c.bloqueioSefaz = 'S'
          and c.dataExclusao is null
          and c.estado = 'BA'
    """)
    fun buscarClientesBloqueadosSefaz(): ArrayList<Cliente>
    **/
}