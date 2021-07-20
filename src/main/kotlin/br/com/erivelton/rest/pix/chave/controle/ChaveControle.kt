package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.PixGrpcServiceGrpc
import br.com.erivelton.rest.pix.chave.dto.requisicao.NovaChaveRequisicao
import br.com.erivelton.rest.pix.chave.dto.resposta.ChaveCriadaResposta
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller
class ChaveControle(
    @Inject private val grpcClient: PixGrpcServiceGrpc.PixGrpcServiceBlockingStub
) {

    @Post("/api/chaves")
    fun registra(@Valid @Body novaChaveRequisicao: NovaChaveRequisicao): ChaveCriadaResposta {
        val requisicao = novaChaveRequisicao.paraServidorGrpc()

        val resposta = grpcClient.registrarPix(requisicao)
        return ChaveCriadaResposta(resposta.pixID, resposta.clienteId)
    }
}