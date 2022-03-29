package br.com.mercante.api.utils

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

class FileUtils {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)

        /**
         * Realiza o download de um arquivo
         * @param [url] url de download
         * @param [pathSave] caminho para salvar o arquivo baixado
         *
         * @return o path do arquivo salvo
         * */
        fun downloadFile(url: String, pathSave: String): Path {
            log.debug("Iniciando download do arquivo $url")
            val messageConverters = ArrayList<HttpMessageConverter<*>>()
            messageConverters.add(ByteArrayHttpMessageConverter())

            val restTemplate = RestTemplate(messageConverters)

            val headers = HttpHeaders()
            val entity = HttpEntity<String>(headers)

            val response = restTemplate.exchange(url, HttpMethod.GET, entity, ByteArray::class.java)

            if (response.statusCode == HttpStatus.OK) {
                val path = Files.write(Paths.get(pathSave), response.body)
                log.debug("Download realizado com sucesso.")
                return path
            } else {
                log.error("Falha ao realizar o download")
                throw RuntimeException("Não foi possível baixar ou salvar o arquivo")
            }
        }

        fun extractFileExeSefaz(path: Path, apagarOrigem: Boolean = true): File {
            val file = path.toFile()
            log.debug("Iniciando extração do arquivo ${file.path}")
            //verifica se existe o plugin - para sistemas baseados em rpm
            val result = executarComando("rpm -qa | grep p7zip")
            if (result.isBlank())
                throw NoSuchElementException("Plugins de descompactação não instalado")
            //Caso o arquivo não exista é lançado uma exception
            if (!file.exists())
                throw FileNotFoundException("Arquivo ${file.path} informado não encontrado")
            //extrai o arquivo no mesmo local do arquivo de origem
            executarComando("7z e -y ${file.path} -o${file.parent}")
            //Apaga o arquivo de origem após a extração, caso seja necessário.
            if (apagarOrigem)
                executarComando("rm -rf ${file.path}")
            log.debug("Extração realizada com sucesso.")
            //Altera o nome do file para que seja igual ao do arquivo extraido
            return File(file.parent, file.name.replaceAfterLast('.', "txt", "exe"))
        }

        /**
         * Executa um comando direto no sistema operacional
         * @param [comando] o comando a ser executado
         *
         * @return uma string contendo o retorno do comando
         * */
        private fun executarComando(comando: String): String {
            val process = Runtime.getRuntime().exec(comando)
            val scan = Scanner(process.inputStream)

            return if (scan.hasNext()) scan.next() else ""
        }
    }
}