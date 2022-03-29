package br.com.mercante.api.model

import javax.persistence.*

@Entity
@Table(name = "PCMOVPREENT")
data class ItemPreEntrada(
        @Id
        @Column(name = "NUMTRANSITEM")
        val numeroTransacao: Long,

        @ManyToOne
        @JoinColumn(name = "CODPROD")
        val produto: Produto,

        @Column(name = "NUMPED")
        val numPedidoCompra: Long,

        @Column(name = "NUMSEQPED")
        val numSequenciaPedidoCompra: Long
)