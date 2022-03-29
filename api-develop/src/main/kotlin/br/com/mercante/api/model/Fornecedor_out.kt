package br.com.mercante.api.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PCFORNEC")
data class Fornecedor(
        @Id
        @Column(name = "CODFORNEC")
        val codigo: Long,

        @Column(name = "FORNECEDOR")
        val razaoSocial: String,

        @Column(name = "CGC")
        val cnpj: String)