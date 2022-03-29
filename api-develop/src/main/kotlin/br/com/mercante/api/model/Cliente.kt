package br.com.mercante.api.model

import br.com.mercante.api.utils.removerCaractersNaoNumericos
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PCCLIENT")
@DynamicUpdate
data class Cliente(
        @Id
        @Column(name = "CODCLI", length = 10) val codigo: Long,
        @Column(name = "CLIENTE") val nome: String,
        @Column(name = "CGCENT") val cgc: String,
        @Column(name = "IEENT") val inscricao: String,
        @Column(name = "RG") val rg: String?,
        @Column(name = "LIMCRED") val limiteDeCredito: Float,
        @Column(name = "CODCOB") val codigoCobranca: String,
        @Column(name = "CODPLPAG") val codigoPlanoPagamento: Int,
        @Column(name = "DTEXCLUSAO") val dataExclusao: LocalDate,
        @Column(name = "TIPOFJ") val tipoPessoa: String,
        @Column(name = "ENDERENT") val endereco: String,
        @Column(name = "NUMEROENT") val numero: String?,
        @Column(name = "BAIRROENT") val bairro: String,
        @Column(name = "MUNICENT") val cidade: String,
        @Column(name = "ESTENT") val estado: String,
        @Column(name = "CEPENT") val cep: String,
        @Column(name = "TELENT") val telefone: String?,
        @Column(name = "ENVIADADOSSERASA") val enviarDadosSerasa: String,
        @Column(name = "BLOQUEIOSEFAZ") var bloqueioSefaz: Char,
        @Column(name = "BLOQUEIOSEFAZPED") var bloqueioSefazPed: Char
) {
    fun getTelefoneSemDDD(): Long {
        val valor = this.telefone?.removerCaractersNaoNumericos()?.takeLast(8)
        return if (valor != null && valor.isNotEmpty()) {
            valor.toLong()
        } else {
            0
        }
    }

    fun getDDDTelefone(): Long {
        val valor = this.telefone?.removerCaractersNaoNumericos()?.dropLast(8)
        return if (valor != null && valor.isNotEmpty()) {
            valor.toLong()
        } else {
            0
        }
    }
}