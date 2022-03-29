package br.com.mercante.api.repository

import br.com.mercante.api.model.ItemPedidoCompra
import br.com.mercante.api.model.identities.ItemPedidoCompraId
import org.springframework.data.jpa.repository.JpaRepository

interface ItemPedidoCompraRepository: JpaRepository<ItemPedidoCompra, ItemPedidoCompraId>