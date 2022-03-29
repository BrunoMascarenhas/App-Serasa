package br.com.mercante.api.repository

import br.com.mercante.api.model.XmlNFe
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface XmlNFeRepository : JpaRepository<XmlNFe, String> {
    fun findByCnpjAndNumeroNotaAndSerie(cnpj: String, numNota: Long, serie: String): Optional<XmlNFe>
}