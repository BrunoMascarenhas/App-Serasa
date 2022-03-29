package br.com.mercante.api.model

import br.com.mercante.api.model.identities.ItemPedidoCompraId
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "PCITEM")
data class ItemPedidoCompra(

        @EmbeddedId
        val itemPedidoCompraId: ItemPedidoCompraId,

        @Column(name = "PCOMPRA")
        val precoCompra: Float,

        @Column(name = "QTPEDIDA")
        val quantidadePedida: Float
) {
    fun getProduto(): Produto {
        return this.itemPedidoCompraId.produto
    }
}