package br.com.mercante.api.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PCNFENTXML")
data class XmlNFe(
        @Id
        @Column(name = "CHAVENFE")
        val chaveNFe: String,

        @Column(name = "NUMNOTA")
        val numeroNota: Long,

        @Column(name = "SERIE")
        val serie: String,

        @Column(name = "CNPJ")
        val cnpj: String,

        @Column(name = "DADOSXML")
        val dadosXml: String
)