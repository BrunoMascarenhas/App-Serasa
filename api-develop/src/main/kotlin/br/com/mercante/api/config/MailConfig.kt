package br.com.mercante.api.config

import br.com.mercante.api.config.property.MailProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.*


/**
 * Classe responsável por realizar a configuração do envio de emails
 * */
@Configuration
class MailConfig(private val mailProperty: MailProperty) {

    /**
     * Configura um mailsender com as propriedades recebidas por mailProperty
     *
     * @return um JavaMailSender
     * */
    @Bean
    fun mailSender(): JavaMailSender {
        val props = Properties()
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = true
        props["mail.smtp.starttls.enable"] = true
        props["mail.smtp.connectiontimeout"] = 30000    

        return JavaMailSenderImpl().apply {
            javaMailProperties = props
            host = mailProperty.host
            port = mailProperty.port
            username = mailProperty.username
            password = mailProperty.password
        }
    }
}