package br.com.mercante.api.model.identities

import br.com.mercante.api.model.PedidoCompra
import br.com.mercante.api.model.Produto
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
data class ItemPedidoCompraId(

        @ManyToOne
        @JoinColumn(name = "NUMPED")
        val pedidoCompra: PedidoCompra,

        @ManyToOne
        @JoinColumn(name = "CODPROD")
        val produto: Produto,

        @Column(name = "NUMSEQ")
        val numeroSequencia: Long
) : Serializable