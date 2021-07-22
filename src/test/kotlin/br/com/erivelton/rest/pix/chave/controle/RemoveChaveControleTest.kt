package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.PixRemovidoResposta
import br.com.erivelton.pix.RemovePixGrpcServiceGrpc
import br.com.erivelton.rest.pix.shared.config.factory.GrpcClientFactory
import br.com.erivelton.rest.pix.shared.config.handlers.TratamentoExcecoesHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChaveControleTest{

    @field:Inject
    lateinit var removeGrpcClient: RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    lateinit var clienteIdTeste: String

    @BeforeEach
    fun setup(){
        clienteIdTeste = "5260263c-a3c1-4727-ae32-3bdb2538841b"
    }

    @Test
    internal fun `deve excluir chave pix`() {
        val resposta = PixRemovidoResposta.newBuilder()
            .setMensagem("Chave removida com sucesso")
            .build()

        BDDMockito.given(removeGrpcClient.remova(Mockito.any())).willReturn(resposta)
        val pixId:Long = 1L

        val delete = HttpRequest.DELETE<Any>("/api/chaves/${pixId}/clientes/$clienteIdTeste")
        val respostaRest = client.toBlocking().exchange(delete, String::class.java)

        with(respostaRest){
            assertEquals("Chave removida com sucesso", body())
            assertEquals(HttpStatus.OK, status)
        }
    }

    @Test
    internal fun `nao deve remover chave pix caso a mesma nao for encontrada no servidor grpc`() {
        val exception = StatusRuntimeException(Status.NOT_FOUND.withDescription("Chave pix não foi encontrada ou usuário inválido!!"))

        val respostaRest = TratamentoExcecoesHandler().handle(HttpRequest.GET<Any>("/"), exception)

        with(respostaRest){
            assertNotNull(body())
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertEquals("NOT_FOUND: Chave pix não foi encontrada ou usuário inválido!!", (body() as JsonError).message)
        }
    }

    @Test
    internal fun `nao deve remover chave caso clienteId seja nulo ou vazio`() {
        val delete = HttpRequest.DELETE<Any>("/api/chaves/1/clientes/")

        val throwGerado = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(delete, String::class.java)
        }

        with(throwGerado){
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertEquals("Page Not Found", message)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class RemovePixMockitoStubFactory {

        @Singleton
        fun removePixStubMock() = Mockito.mock(RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub::class.java)
    }
}