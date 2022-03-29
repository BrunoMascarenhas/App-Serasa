package br.com.mercante.api.utils

import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter
class StringListConverter : AttributeConverter<List<String>, String> {
    override fun convertToEntityAttribute(dbData: String): List<String> {
        return dbData.split(",").map { it.trim() }
    }

    override fun convertToDatabaseColumn(attribute: List<String>) = attribute.joinToString()
}

class LongListConverter : AttributeConverter<List<Long>, String> {
    override fun convertToEntityAttribute(dbData: String): List<Long> {
        return dbData.split(",").map { it.trim().toLong() }
    }

    override fun convertToDatabaseColumn(attribute: List<Long>) = attribute.joinToString()
}

