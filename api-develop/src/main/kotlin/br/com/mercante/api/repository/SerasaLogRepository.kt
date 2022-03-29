package br.com.mercante.api.repository

import br.com.mercante.api.model.serasapefin.SerasaLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SerasaLogRepository : JpaRepository<SerasaLog, Long> {

    @Query("select max(codigo) from SerasaLog")
    fun getUltimoIdUsado(): Long
}