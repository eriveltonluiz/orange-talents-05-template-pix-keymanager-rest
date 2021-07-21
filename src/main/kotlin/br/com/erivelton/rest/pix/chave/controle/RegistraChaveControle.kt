package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.*
import br.com.erivelton.rest.pix.chave.dto.requisicao.NovaChaveRequisicao
import br.com.erivelton.rest.pix.chave.dto.resposta.ChaveCriadaResposta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@Controller("/api/chaves")
class RegistraChaveControle(
    @Inject private val registraPixGrpcClient: PixGrpcServiceGrpc.PixGrpcServiceBlockingStub
) {

    @Post
    fun registra(@Valid @Body novaChaveRequisicao: NovaChaveRequisicao): HttpResponse<ChaveCriadaResposta> {
        val requisicao = novaChaveRequisicao.paraServidorGrpc()

        val resposta = registraPixGrpcClient.registrarPix(requisicao)
        return HttpResponse.created(HttpResponse.uri("/api/chaves/${resposta.pixID}"))
    }

}