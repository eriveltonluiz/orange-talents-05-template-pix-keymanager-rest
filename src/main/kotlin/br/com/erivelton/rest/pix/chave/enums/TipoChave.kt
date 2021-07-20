package br.com.erivelton.rest.pix.chave.enums

import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class TipoChave {
    CPF {
        override fun validarChaves(valorChave: String?, context: ConstraintValidatorContext): Boolean {
            if (valorChave.isNullOrBlank()) {
                println("teste null cpf")
                context.messageTemplate("Valor da chave CPF não pode ser vazio ou nulo")
                return false
            }

            if (valorChave.matches("^[0-9]{11}$".toRegex()) && CPFValidator().run {
                    initialize(null)
                    isValid(valorChave, null)
                })
                return true

            context.messageTemplate("Formato CPF inválido - 99999999999")
            return false
        }
    },
    PHONE {
        override fun validarChaves(valorChave: String?, context: ConstraintValidatorContext): Boolean {
            if (valorChave.isNullOrBlank()) {
                context.messageTemplate("Valor da chave celular não pode ser vazio ou nulo")
                return false
            }

            if (valorChave.matches("^\\+[1-9][0-9]\\d{1,14}$".toRegex()))
                return true

            context.messageTemplate("Formato celular inválido - +9999999999999")
            return false
        }
    },
    EMAIL {
        override fun validarChaves(valorChave: String?, context: ConstraintValidatorContext): Boolean {
            if (valorChave.isNullOrBlank()) {
                context.messageTemplate("Valor da chave email não pode ser vazio ou nulo")
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(valorChave, null)
            }
        }
    },
    RANDOM {
        override fun validarChaves(valorChave: String?, context: ConstraintValidatorContext): Boolean {
            if(valorChave.isNullOrBlank()){
               return true
            }

            context.messageTemplate("Chave aleatória será gerada pelo sistema e não deve ser informada")
            return false
        }
    };

    abstract fun validarChaves(valorChave: String?, context: ConstraintValidatorContext): Boolean
}
