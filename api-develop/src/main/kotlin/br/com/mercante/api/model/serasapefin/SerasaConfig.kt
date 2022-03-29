package br.com.mercante.api.model.serasapefin

import br.com.mercante.api.utils.LongListConverter
import br.com.mercante.api.utils.StringListConverter
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "MDSERASACONFIG")
data class SerasaConfig(
        @Id
        @Column(name = "CODIGO")
        val codigo: Long,
        @Column(name = "NUMDIASVENCIDOS")
        val numdiasVencidos: Int,
        @Column(name = "NUMULTIMAREMESSA")
        var numUltimaRemessa: Int,
        @Column(name = "DTHORAULTIMAREMESSA")
        var ultimaRemessa: LocalDateTime,
        @Column(name = "CODCOBRANCAS")
        @Convert(converter = StringListConverter::class)
        val cobrancas: List<String> = emptyList(),
        @Column(name = "CODFILIAIS")
        @Convert(converter = LongListConverter::class)
        val filiais: List<Long> = emptyList(),
        @Column(name = "DIREXPORTACAO")
        val diretorioExportacao: String,
        @Column(name = "DIRRECEPCAO")
        val diretorioRecepcao: String,
        @Column(name = "EXECUTAVEL")
        val pathExecutavel: String
)