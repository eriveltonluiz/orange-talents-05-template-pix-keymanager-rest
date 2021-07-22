package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.BuscaGrpcServiceGrpc
import br.com.erivelton.pix.DadosPixRequisicao
import br.com.erivelton.pix.InformacaoIdClienteRequisicao
import br.com.erivelton.rest.pix.chave.dto.resposta.DadosChavePixResposta
import br.com.erivelton.rest.pix.chave.dto.resposta.DetalhesChavePixResposta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import javax.inject.Inject

@Validated
@Controller
class BuscaChaveControle(
    @Inject private val buscaPixGrpcClient: BuscaGrpcServiceGrpc.BuscaGrpcServiceBlockingStub
) {

    @Get("/api/chaves/{pixId}/clientes/{clienteId}")
    fun busca(@PathVariable pixId: Long, @PathVariable clienteId: String): HttpResponse<DetalhesChavePixResposta> {
        val respostaGrpc = buscaPixGrpcClient.buscaPix(
            DadosPixRequisicao.newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build()
        )

        val respostaRest = DetalhesChavePixResposta(respostaGrpc)

        return HttpResponse.ok(respostaRest)
    }

    @Get("/api/clientes/{clienteId}")
    fun lista(@PathVariable clienteId: String): HttpResponse<List<DadosChavePixResposta>>{
        val listaChavesCliente = buscaPixGrpcClient.buscaTodosPixCliente(
            InformacaoIdClienteRequisicao.newBuilder()
                .setClienteId(clienteId)
                .build()
        )

        return HttpResponse.ok(listaChavesCliente.pixGeralRespostaList.map { DadosChavePixResposta(it) })
    }

}