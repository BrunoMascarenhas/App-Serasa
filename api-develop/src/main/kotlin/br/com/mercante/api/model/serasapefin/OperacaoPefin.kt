package br.com.mercante.api.model.serasapefin

enum class OperacaoPefin(val valor: String) {
    CONSULTA("C"),
    INCLUSAO("I"),
    EXCLUSAO("E")
}