package br.com.mercante.api.model

import br.com.mercante.api.model.identities.PrestacaoId
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "PCPREST")
@DynamicUpdate
data class Prestacao(
        @EmbeddedId
        val id: PrestacaoId,

        @Column(name = "DUPLIC")
        val duplicata: Long,

        @Column(name = "VALOR")
        val valor: Float,

        @Column(name = "DTVENC")
        val dataVencimento: LocalDate,

        @Column(name = "DTPAG")
        val dataPagamento: LocalDate,

        @Column(name = "VPAGO")
        val valorPago: Float? = 0f,

        @Column(name = "CODCOB")
        val cobranca: String,

        @Column(name = "CODCOBORIG")
        val cobrancaOriginal: String,

        @ManyToOne
        @JoinColumn(name = "CODCLI")
        val cliente: Cliente,

        @ManyToOne
        @JoinColumn(name = "CODFILIAL")
        val filial: Filial,

        @Column(name = "DTENVIOSERASA")
        var dataEnvioSerasa: LocalDate,

        @Column(name = "DTRETIRADASERASA")
        var dataRetiradaSerasa: LocalDate
)