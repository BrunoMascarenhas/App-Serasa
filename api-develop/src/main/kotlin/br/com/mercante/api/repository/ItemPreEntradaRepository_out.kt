package br.com.mercante.api.repository

import br.com.mercante.api.model.ItemPreEntrada
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ItemPreEntradaRepository : JpaRepository<ItemPreEntrada, Long> {

    @Modifying
    @Query("update ItemPreEntrada i set i.numPedidoCompra = ?1, i.numSequenciaPedidoCompra =?2 where i.numeroTransacao = ?3")
    fun setNumPedido(numPedidoCompra: Long, numSequenciaPedido: Long, numTransacaoItem: Long)
}