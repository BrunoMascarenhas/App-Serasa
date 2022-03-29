package br.com.mercante.api.repository

import br.com.mercante.api.model.PreEntrada
import org.springframework.data.jpa.repository.JpaRepository

interface PreEntradaRepository : JpaRepository<PreEntrada, Long>