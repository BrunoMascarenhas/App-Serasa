package br.com.mercante.api.utils

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class Command {
    companion object {
        internal val log = LoggerFactory.getLogger(this::class.java)

        fun exec(comando: String): MutableMap<String, String> {
            val result = mutableMapOf<String, String>()
            val process = Runtime.getRuntime().exec(comando)
            process.waitFor()
            result["exec"] = inputStreamToString(process.inputStream)
            result["error"] = inputStreamToString(process.errorStream)

            return result
        }

        private fun inputStreamToString(input: InputStream): String {

            val buffer = StringBuffer()
            val lines = BufferedReader(InputStreamReader(input)).readLines()

            lines.forEachIndexed { index, line ->
                buffer.append(line)
                if (index > 1) buffer.append("\n")
            }
            return buffer.toString()
        }
    }
}