package br.com.mercante.api.model.serasapefin

import com.ancientprogramming.fixedformat4j.annotation.Align
import com.ancientprogramming.fixedformat4j.annotation.Field
import com.ancientprogramming.fixedformat4j.annotation.Record

@Record(length = 600)
open class RecordPefin {
    var tipoRegistro = 0
        @Field(offset = 1, length = 1) get() = field

    var sequencia = 0
        @Field(offset = 594, length = 7, align = Align.RIGHT, paddingChar = '0')
        get() = field
}