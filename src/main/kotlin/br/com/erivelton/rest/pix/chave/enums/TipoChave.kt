package br.com.erivelton.rest.pix.chave.enums

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator

enum class TipoChave {
    CPF {
        override fun validarChaves(valorChave: String?): Boolean {
            if (valorChave.isNullOrBlank())
                return false

            if (valorChave.matches("^[0-9]{11}$".toRegex()))
                return true

            return false
        }
    },
    PHONE {
        override fun validarChaves(valorChave: String?): Boolean {
            if (valorChave.isNullOrBlank())
                return false

            if (valorChave.matches("^\\+[1-9][0-9]\\d{1,14}$".toRegex()))
                return true

            return false
        }
    },
    EMAIL {
        override fun validarChaves(valorChave: String?): Boolean {
            if (valorChave.isNullOrBlank())
                return false

            return EmailValidator().run {
                initialize(null)
                isValid(valorChave, null)
            }
        }
    },
    RANDOM {
        override fun validarChaves(valorChave: String?): Boolean {
            return valorChave.isNullOrBlank()
        }
    };

    abstract fun validarChaves(valorChave: String?): Boolean
}
