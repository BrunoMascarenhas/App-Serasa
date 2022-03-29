package br.com.mercante.api.repository

import br.com.mercante.api.model.Produto
import org.springframework.data.jpa.repository.JpaRepository


interface ProdutoRepository : JpaRepository<Produto, Long>