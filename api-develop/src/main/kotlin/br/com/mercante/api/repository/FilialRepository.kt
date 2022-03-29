package br.com.mercante.api.repository

import br.com.mercante.api.model.Filial
import org.springframework.data.jpa.repository.JpaRepository

interface FilialRepository : JpaRepository<Filial, Long>