package br.com.mercante.api.mail

import br.com.mercante.api.config.property.MailProperty
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.*
import javax.mail.MessagingException

/**
 * Classe resposável por realiazar o envio dos emails
 * */
@Component
class MailService(private val mailSender: JavaMailSender,
                  private val mailProperty: MailProperty,
                  private val thymeleaf: TemplateEngine) {

    /**
     * Envia um email utilizando template html, caso o template não tenha variavéis, não será
     * necessário informar o segundo parâmetro
     *
     * @param [to] destinatários para envio do email.
     * @param [subject] Assunto da mensagem a ser enviada
     * @param [template] template utilizado para construir o corpo do email, deve está na pasta resources.template.mail
     * @param [params] parametros utilizado para preencher as variáveis do template.
     * */
    fun sendMailWithTemplate(to: Array<String>, subject: String, template: String, params: Map<String, Any> = hashMapOf()) {
        log.debug("Enviando email com template.")
        val message = constructBody(template, params)

        this.sendMail(to, subject, message)
    }

    /**
     *
     * */
    @Async
    fun sendMail(to: Array<String>, subject: String, message: String) {
        try {
            log.debug("Iniciando envio de email com sendMail")
            log.debug("Destinatários: ${to.joinToString()}")
            val mimeMessage = this.mailSender.createMimeMessage()
            val mimeHelper = MimeMessageHelper(mimeMessage, mailProperty.encoding)
            mimeHelper.apply {
                setFrom(mailProperty.from, "Notificações MR")
                setTo(to)
                setSubject(subject)
                setText(message, true)
            }

            mailSender.send(mimeMessage)
            log.debug("Email enviado com sucesso!")
        } catch (e: MessagingException) {
            log.error("Não foi possível enviar o email. Ocorreu um erro inesperado")
            throw RuntimeException("Ocorreu um erro ao enviar o email!", e)
        }
    }

    /**
     * Função responsável por construir o corpo da mensagem, baseando-se com no template e parametros do email
     * @param [template] path do template a ser utilizado para constuir a mensagem.
     * @param [params] parâmetros nessesários para o prechimento do template.
     *
     * @return uma string contendo a mensagem com email a ser enviado.
     * */
    private fun constructBody(template: String, params: Map<String, Any>): String {
        log.debug("Template utilizado: $template com ${params.size} parâmetros.")
        val context = Context(Locale("pt", "BR"))
        context.setVariables(params)

        return this.thymeleaf.process(template, context)
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}