package br.com.erivelton.rest.pix.chave.dto.requisicao

import br.com.erivelton.pix.PixRequest
import br.com.erivelton.rest.pix.chave.enums.TipoChave
import br.com.erivelton.rest.pix.chave.enums.TipoConta
import br.com.erivelton.rest.pix.chave.validacao.VerificarFormatos
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
@VerificarFormatos
data class NovaChaveRequisicao(
    @field:NotBlank val clienteId: String,
    @field:Size(min = 0, max = 77) val valorChave: String,
    @field:NotBlank val tipoChave: TipoChave,
    @field:NotBlank val tipoConta: TipoConta,
) {

    fun retornarValidacaoChaves(): Boolean {
        return tipoChave.validarChaves(valorChave)
    }

    fun paraServidorGrpc(): PixRequest {
        return PixRequest.newBuilder()
            .setClienteId(clienteId)
            .setValorChave(valorChave ?: "")
            .setTipoChave(br.com.erivelton.pix.TipoChave.valueOf(tipoChave.name))
            .setTipoConta(br.com.erivelton.pix.TipoConta.valueOf(tipoConta.name))
            .build()
    }
}
