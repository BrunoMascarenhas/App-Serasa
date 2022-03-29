package br.com.mercante.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class ApiApplication(
        @Value("\${app.message}")
        val message: String
)

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
