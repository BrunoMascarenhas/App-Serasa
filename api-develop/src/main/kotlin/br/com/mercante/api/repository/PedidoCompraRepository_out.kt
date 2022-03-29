package br.com.mercante.api.repository

import br.com.mercante.api.model.PedidoCompra
import org.springframework.data.jpa.repository.JpaRepository

interface PedidoCompraRepository : JpaRepository<PedidoCompra, Long>