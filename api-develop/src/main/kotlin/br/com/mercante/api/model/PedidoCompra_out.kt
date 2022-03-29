package br.com.mercante.api.model

import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name = "PCPEDIDO")
data class PedidoCompra(
        @Id
        @Column(name = "NUMPED")
        val numeroPedido: Long,

        @Column(name = "DTEMISSAO")
        val dataEmissao: LocalDate,

        @ManyToOne
        @JoinColumn(name = "CODFORNEC")
        val fornecedor: Fornecedor,

        @Column(name = "CODFILIAL")
        val codigoFilial: Long,

        @OneToMany
        val items: List<ItemPedidoCompra>
)