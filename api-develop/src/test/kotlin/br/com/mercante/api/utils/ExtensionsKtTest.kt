package br.com.mercante.api.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExtensionsKtTest {

    @Test
    fun removerAcentuacao() {
        val cnpj = "11.386.528/0001-67"
        val limpo = cnpj.replace(Regex("\\D"), "")

        assertEquals("11386528000167", limpo)
    }

    @Test
    fun altearExtensaoArquivo() {
        val arquivo = "arquivooutro.exe"
        val novo = arquivo.replaceAfterLast('.', "txt", "exe")

        assertEquals("arquivooutro.txt", novo)
    }
}
