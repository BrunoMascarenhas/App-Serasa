package br.com.mercante.api.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "PCPRODUT")
data class Produto(
        @Id
        @Column(name = "CODPROD")
        val codigo: Long,

        @Column(name = "DESCRICAO")
        val description: String,

        @Column(name = "CODFAB")
        val codigoFabrica: String)