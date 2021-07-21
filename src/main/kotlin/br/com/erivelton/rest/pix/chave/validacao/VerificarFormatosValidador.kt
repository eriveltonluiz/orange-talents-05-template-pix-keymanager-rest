package br.com.erivelton.rest.pix.chave.validacao

import br.com.erivelton.rest.pix.chave.dto.requisicao.NovaChaveRequisicao
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton

@Singleton
class VerificarFormatosValidador : ConstraintValidator<VerificarFormatos, NovaChaveRequisicao> {
    override fun isValid(
        value: NovaChaveRequisicao?,
        annotationMetadata: AnnotationValue<VerificarFormatos>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) {
            return false
        }
        return value.retornarValidacaoChaves()
    }
}