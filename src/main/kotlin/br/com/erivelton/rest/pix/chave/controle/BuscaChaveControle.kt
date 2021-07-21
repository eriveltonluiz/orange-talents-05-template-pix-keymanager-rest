package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.BuscaGrpcServiceGrpc
import br.com.erivelton.pix.DadosPixRequisicao
import br.com.erivelton.pix.PixRemovidoRequisicao
import br.com.erivelton.pix.RemovePixGrpcServiceGrpc
import br.com.erivelton.rest.pix.chave.dto.resposta.DadosChavePixResposta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.validation.constraints.NotBlank

@Validated
@Controller
class BuscaChaveControle(
    @Inject private val buscaPixGrpcClient: BuscaGrpcServiceGrpc.BuscaGrpcServiceBlockingStub
) {

    @Get("/api/chaves/{pixId}/clientes/{clienteId}")
    fun busca(@PathVariable pixId: Long, @PathVariable clienteId: String): HttpResponse<DadosChavePixResposta> {
        val respostaGrpc = buscaPixGrpcClient.buscaPix(
            DadosPixRequisicao.newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build()
        )

        val respostaRest = DadosChavePixResposta(respostaGrpc)

        return HttpResponse.ok(respostaRest)
    }

}