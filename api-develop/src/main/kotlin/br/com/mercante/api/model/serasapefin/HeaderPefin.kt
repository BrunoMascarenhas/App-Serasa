package br.com.mercante.api.model.serasapefin

import com.ancientprogramming.fixedformat4j.annotation.Align
import com.ancientprogramming.fixedformat4j.annotation.Field
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern
import com.ancientprogramming.fixedformat4j.annotation.Record
import java.util.*

@Suppress("unused")
@Record(length = 600)
class HeaderPefin : RecordPefin() {
    var cnpj = 0
        @Field(offset = 2, length = 9, align = Align.RIGHT, paddingChar = '0')
        get() = field
    var dataMovimento = Date()
        @Field(offset = 11, length = 8)
        @FixedFormatPattern("yyyyMMdd")
        get() = field
    var dddEmpresa = 0
        @Field(offset = 19, length = 4, align = Align.RIGHT, paddingChar = '0')
        get() = field
    var telefoneEmpresa = 0
        @Field(offset = 23, length = 8, align = Align.RIGHT, paddingChar = '0')
        get() = field
    var ramalEmpresa = 0
        @Field(offset = 31, length = 4, align = Align.RIGHT, paddingChar = '0')
        get() = field
    var contatoEmpresa = ""
        @Field(offset = 35, length = 70)
        get() = field
    var sufixoSerasa = "SERASA-CONVEM04"
        @Field(offset = 105, length = 15)
        get() = field
    var numeroRemessa = 0
        @Field(offset = 120, length = 6, align = Align.RIGHT, paddingChar = '0')
        get() = field
    var codigoEnvio = ""
        @Field(offset = 126, length = 1)
        get() = field
    var diferencialRemessa = ""
        @Field(offset = 127, length = 4)
        get() = field
    var branco1 = ""
        @Field(offset = 131, length = 3)
        get() = field
    var logon = ""
        @Field(offset = 134, length = 8)
        get() = field
    var branco2 = ""
        @Field(offset = 142, length = 392)
        get() = field
    var erros = ""
        @Field(offset = 534, length = 60)
        get() = field
}