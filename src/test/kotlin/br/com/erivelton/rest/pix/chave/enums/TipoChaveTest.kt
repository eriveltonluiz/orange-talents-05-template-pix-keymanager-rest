package br.com.erivelton.rest.pix.chave.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoChaveTest{

    @Nested
    inner class CPF{

        @Test
        internal fun `nao deve validar caso cpf esteja nulo ou vazio`() {
            with(TipoChave.CPF){
                assertFalse(validarChaves(""))
                assertFalse(validarChaves(null))
            }
        }

        @Test
        internal fun `nao deve validar caso formato cpf seja invalido`() {
            with(TipoChave.CPF){
                assertFalse(validarChaves("2721647811"))
            }
        }

        @Test
        internal fun `deve validar caso o cpf seja inserido no formato correto`() {
            with(TipoChave.CPF){
                assertTrue(validarChaves("27216478114"))
            }
        }

        @Test
        internal fun `deve invalidar quando houver embutido no valor da chave CPF letra(s)`() {
            with(TipoChave.CPF) {
                assertFalse(validarChaves("2132123232a"))
            }
        }

    }

    @Nested
    inner class EMAIL{
        @Test
        internal fun `deve validar caso o formato do email esteja correto`() {
            with(TipoChave.EMAIL){
                assertTrue(validarChaves("eri@email.com"))
            }
        }

        @Test
        internal fun `nao deve validar caso o valor seja nulo`() {
            with(TipoChave.EMAIL){
                assertFalse(validarChaves(""))
                assertFalse(validarChaves(null))
            }
        }

        @Test
        internal fun `nao deve validar caso o formato do email esteja incorreto`() {
            with(TipoChave.EMAIL){
                assertFalse(validarChaves("eriemail.com"))
            }
        }
    }

    @Nested
    inner class CELULAR{
        @Test
        internal fun `nao deve validar caso o celular esteja nulo ou vazio`() {
            with(TipoChave.PHONE){
                assertFalse(validarChaves(""))
                assertFalse(validarChaves(null))
            }
        }

        @Test
        internal fun `nao deve validar caso o celular esteja em um formato invalido`() {
            with(TipoChave.PHONE){
                assertFalse(validarChaves("55988121343"))
            }
        }

        @Test
        internal fun `deve validar caso o celular esteja no formato correto`() {
            with(TipoChave.PHONE){
                assertTrue(validarChaves("+55988121423"))
            }
        }

        @Test
        internal fun `deve invalidar quando houver embutido no valor da chave CELULAR letra(s)`() {
            with(TipoChave.PHONE) {
                assertFalse(validarChaves("+5598871091a"))
            }
        }
    }

    @Nested
    inner class RANDOM{
        @Test
        internal fun `deve invalidar quando o valor da chave ter sido inserido`() {
            with(TipoChave.RANDOM) {
                assertFalse(validarChaves("eriemail.com"))
            }
        }

        @Test
        internal fun `deve validar quando o valor da chave não for inserido`() {
            with(TipoChave.RANDOM) {
                assertTrue(validarChaves(""))
                assertTrue(validarChaves(null))
            }
        }
    }
}