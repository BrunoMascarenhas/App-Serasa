package br.com.mercante.api.utils

import java.text.Normalizer
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.regex.Pattern

fun String.removerAcentuacao(): String {
    val nfdNormalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}

fun String.removerCaractersNaoNumericos() = this.replace(Regex("\\D"), "")

fun String.cleanNotPrintableChars(extendedChars: Boolean = false): String {
    val sb = StringBuilder()
    for (c in this) {
        val i = c.toInt()
        if (i in 32..126 || (!extendedChars && i >= 128)) sb.append(c)
    }
    return sb.toString()
}

fun LocalDate.asDate(): Date = Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())