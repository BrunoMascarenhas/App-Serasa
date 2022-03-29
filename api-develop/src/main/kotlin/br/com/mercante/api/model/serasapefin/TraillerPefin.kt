package br.com.mercante.api.model.serasapefin

import com.ancientprogramming.fixedformat4j.annotation.Field
import com.ancientprogramming.fixedformat4j.annotation.Record

@Suppress("unused")
@Record(length = 600)
class TraillerPefin : RecordPefin() {
    var branco = ""
        @Field(offset = 2, length = 592)
        get() = field
}