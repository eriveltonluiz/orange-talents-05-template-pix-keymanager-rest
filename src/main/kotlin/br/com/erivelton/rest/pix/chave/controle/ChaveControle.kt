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
class ChaveControle(
    @Inject private val registraPixGrpcClient: PixGrpcServiceGrpc.PixGrpcServiceBlockingStub,
    @Inject private val removePixGrpcClient: RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub
) {

    @Post
    fun registra(@Valid @Body novaChaveRequisicao: NovaChaveRequisicao): HttpResponse<ChaveCriadaResposta> {
        val requisicao = novaChaveRequisicao.paraServidorGrpc()

        val resposta = registraPixGrpcClient.registrarPix(requisicao)
        return HttpResponse.created(HttpResponse.uri("/api/chaves/${resposta.pixID}"))
    }

    @Delete("/{pixId}")
    fun deleta(@PathVariable pixId: Long, @NotBlank(message = "Não pode ser nulo ou vazio") clienteId: String): String{
        val resposta = removePixGrpcClient.remova(
            PixRemovidoRequisicao.newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build()
        )

        return resposta.mensagem
    }
}