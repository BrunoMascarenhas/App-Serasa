package br.com.mercante.api.model.identities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class PrestacaoId(

        @Column(name = "NUMTRANSVENDA")
        val numTransVenda: Long,

        @Column(name = "PREST")
        val prestacao: String
) : Serializable