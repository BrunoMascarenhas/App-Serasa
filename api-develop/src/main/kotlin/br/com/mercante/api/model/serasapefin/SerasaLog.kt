package br.com.mercante.api.model.serasapefin

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "MDSERASALOG")
data class SerasaLog(
        @Id
        @Column(name = "CODIGO")
        val codigo: Long,
        @Column(name = "DUPLICATA")
        val duplicata: Int,
        @Column(name = "PRESTACAO")
        val prestacao: String,
        @Column(name = "NUMREMESSA")
        val remessa: Int,
        @Column(name = "DTMOV")
        val data: LocalDateTime,
        @Column(name = "DTVENCIMENTO")
        val dataVenc: LocalDate,
        @Column(name = "OPERACAO")
        val operacao: String,
        @Column(name = "FILIAL")
        val filial: Long
)