package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.PixRemovidoRequisicao
import br.com.erivelton.pix.RemovePixGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.constraints.NotBlank

@Validated
@Controller
class RemoveChaveControle(
    @Inject private val removePixGrpcClient: RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub
) {

    @Delete("/api/chaves/{pixId}/clientes/{clienteId}")
    fun deleta(@PathVariable pixId: Long, @PathVariable clienteId: String): HttpResponse<String> {
        val resposta = removePixGrpcClient.remova(
            PixRemovidoRequisicao.newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build()
        )

        return HttpResponse.ok(resposta.mensagem)
    }

}