package br.com.mercante.api.model.dto

interface VendaPorFornecodorDTO{
        val codFornecedor: Long
        val fornecedor: String
        val qtCliPos: Long
        val vltotal: Float
}