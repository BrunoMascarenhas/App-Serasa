package br.com.mercante.api.model

import javax.persistence.*

@Entity
@Table(name = "PCNFENTPREENT")
data class PreEntrada(

        @Id
        @Column(name = "NUMTRANSENT")
        val numTransaction: Long,

        @Column(name = "NUMNOTA")
        val numNota: Long?,

        @Column(name = "SERIE")
        val serie: String,

        @ManyToOne
        @JoinColumn(name = "CODFORNEC")
        val fornecedor: Fornecedor,

        @OneToMany(orphanRemoval = true)
        val itemsPreEntrada: List<ItemPreEntrada>
)