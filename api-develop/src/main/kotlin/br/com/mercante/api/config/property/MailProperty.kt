package br.com.mercante.api.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "api.mail")
class MailProperty {

    lateinit var host: String

    var port: Int = 0

    lateinit var username: String

    lateinit var password: String

    lateinit var from: String

    lateinit var fromName: String

    lateinit var encoding: String
}