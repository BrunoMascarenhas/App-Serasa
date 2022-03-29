package br.com.mercante.api.model.serasapefin

import com.ancientprogramming.fixedformat4j.annotation.*
import java.util.*

@Suppress("unused")
@Record(length = 600)
class DetailPefin : RecordPefin() {
    var codigoOperacao = ""
        @Field(offset = 2, length = 9) get() = field
    var filialEDigito = 0
        @Field(offset = 3, length = 6, align = Align.RIGHT, paddingChar = '0') get() = field
    var dataOcorrencia = Date()
        @Field(offset = 9, length = 8) @FixedFormatPattern("yyyyMMdd") get() = field
    var dataTerminoContrato = Date()
        @Field(offset = 17, length = 8) @FixedFormatPattern("yyyyMMdd") get() = field
    var naturezaOperacao = ""
        @Field(offset = 25, length = 3) get() = field
    var codigoEmbratel = ""
        @Field(offset = 28, length = 4) get() = field
    var tipoPessoaPrincipal = ""
        @Field(offset = 32, length = 1) get() = field
    var tipoPrimeiroDocPrincipal = ""
        @Field(offset = 33, length = 1) get() = field
    var primeiroDocPrincipal: Long = 0
        @Field(offset = 34, length = 15, align = Align.RIGHT, paddingChar = '0') get() = field
    var motivoBaixa = ""
        @Field(offset = 49, length = 2) get() = field
    var tipoSegundoDocPrincipal = ""
        @Field(offset = 51, length = 1) get() = field
    var segundoDocPrincipal = ""
        @Field(offset = 52, length = 15) get() = field
    var ufPrinncipal = ""
        @Field(offset = 67, length = 2) get() = field
    var tipoPessoaCoobrigado = ""
        @Field(offset = 69, length = 1) get() = field
    var tipoPrimeiroDocCoobrigado = ""
        @Field(offset = 70, length = 1) get() = field
    var primeiroDocCoobrigado = ""
        @Field(offset = 71, length = 15) get() = field
    var branco1 = ""
        @Field(offset = 86, length = 2) get() = field
    var tipoSegundoDocCoobrigado = ""
        @Field(offset = 88, length = 1) get() = field
    var segundoDocCoobrigado = ""
        @Field(offset = 89, length = 15) get() = field
    var ufCoobrigado = ""
        @Field(offset = 104, length = 2) get() = field
    var nomeDevedor = ""
        @Field(offset = 106, length = 70) get() = field
    var nascDevedor = 0
        @Field(offset = 176, length = 8, align = Align.RIGHT, paddingChar = '0') get() = field
    var nomePai = ""
        @Field(offset = 184, length = 70) get() = field
    var nomeMae = ""
        @Field(offset = 254, length = 70) get() = field
    var enderecoDevedor = ""
        @Field(offset = 324, length = 70) get() = field
    var bairroDevedor = ""
        @Field(offset = 369, length = 20) get() = field
    var cidadeDevedor = ""
        @Field(offset = 389, length = 20) get() = field
    var ufDevedor = ""
        @Field(offset = 414, length = 2) get() = field
    var cepDevedor = 0
        @Field(offset = 416, length = 8, align = Align.RIGHT, paddingChar = '0') get() = field
    var valorDevido = 0f
        @Field(offset = 424, length = 15, align = Align.RIGHT, paddingChar = '0') get() = field
    var numeroContrato = ""
        @Field(offset = 439, length = 16) get() = field
    var nossoNumero = 0
        @Field(offset = 455, length = 9, align = Align.RIGHT, paddingChar = '0') get() = field
    var complementoEndereco = ""
        @Field(offset = 464, length = 25) get() = field
    var dddDevedor: Long = 0
        @Field(offset = 489, length = 4, align = Align.RIGHT, paddingChar = '0') get() = field
    var telefoneDevedor: Long = 0
        @Field(offset = 493, length = 9, align = Align.RIGHT, paddingChar = '0') get() = field
    var dataCompromisso = Date()
        @Field(offset = 502, length = 8) @FixedFormatPattern("yyyyMMdd") get() = field
    var valorTotal = 0f
        @Field(offset = 510, length = 15, align = Align.RIGHT, paddingChar = '0') get() = field
    var branco2 = ""
        @Field(offset = 525, length = 1) get() = field
    var branco3 = ""
        @Field(offset = 526, length = 5) get() = field
    var tipoComunicado = ""
        @Field(offset = 531, length = 1) get() = field
    var branco4 = ""
        @Field(offset = 532, length = 1) get() = field
    var melhorEndereco = ""
        @Field(offset = 533, length = 1) get() = field
    var erros = ""
        @Field(offset = 534, length = 60) get() = field
}