package br.com.mercante.api.model

import javax.persistence.*

@Entity
@Table(name = "PCFILIAL")
data class Filial(
        @Id
        @Column(name = "CODIGO") val codigo: Long,
        @Column(name = "RAZAOSOCIAL") val razaoSocial: String,
        @Column(name = "CGC") val cnpj: String,
        @Column(name = "TELEFONE") private val telefone: String,
        @Column(name = "CONTATO") val contato: String,
        @OneToOne
        @JoinColumn(name = "CODCLi") val clienteFilial: Cliente
) {
    fun extrairUltimaParteCNPJ() = this.cnpj.takeLast(6)

    fun extrairPrimeiraParteCNPJ() = this.cnpj.dropLast(6)

    fun getTelefoneSemDDD() = this.telefone.takeLast(8)

    fun getDDDTelefone() = this.telefone.dropLast(8)

    fun getRamal() = this.telefone.takeLast(4)
}