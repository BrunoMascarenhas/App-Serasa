package br.com.mercante.api.repository

import br.com.mercante.api.model.serasapefin.SerasaConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SerasaConfigRepository : JpaRepository<SerasaConfig, Long> {

    @Query("select c from SerasaConfig c where c.codigo = 1")
    fun getConfig(): SerasaConfig

    @Query("select max(numUltimaRemessa) +1 from SerasaConfig where codigo = 1")
    fun buscarNumeroProximaRemessa(): Int
}