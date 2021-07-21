package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.*
import br.com.erivelton.rest.pix.chave.GrpcClientFactory
import br.com.erivelton.rest.pix.chave.dto.requisicao.NovaChaveRequisicao
import br.com.erivelton.rest.pix.chave.enums.TipoChave
import br.com.erivelton.rest.pix.chave.enums.TipoConta
import br.com.erivelton.rest.pix.shared.config.handlers.TratamentoExcecoesHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChaveControleTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: PixGrpcServiceGrpc.PixGrpcServiceBlockingStub

    lateinit var novaChave: NovaChaveRequisicao

    lateinit var clienteIdTeste: String

    @BeforeEach
    fun setup() {
        clienteIdTeste = "5260263c-a3c1-4727-ae32-3bdb2538841b"
        novaChave = NovaChaveRequisicao(
            clienteIdTeste,
            "23432479238",
            TipoChave.CPF,
            TipoConta.CONTA_CORRENTE)
    }

    @Test
    internal fun `deve registrar chave pix`() {
        val respostaGrpc = PixResponse.newBuilder()
            .setClienteId(clienteIdTeste)
            .setPixID(1L)
            .build()

        given(grpcClient.registrarPix(Mockito.any())).willReturn(respostaGrpc)

        val requisicao = HttpRequest.POST("/api/chaves", novaChave)
        val resposta = client.toBlocking().exchange(requisicao, NovaChaveRequisicao::class.java)

        with(resposta){
            assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location").contains(1L.toString()))
        }
    }

    @Test
    internal fun `nao deve registrar chave pix caso cliente nao seja encontrado no itau`() {

        val exception = StatusRuntimeException(Status.FAILED_PRECONDITION.withDescription("Cliente não foi encontrado na api do itaú"))
        val resposta = TratamentoExcecoesHandler().handle(HttpRequest.GET<Any>("/"), exception)

        with(resposta){
            assertNotNull(this)
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertEquals("Cliente não foi encontrado na api do itaú", (body() as JsonError).message)
        }
    }

    @Test
    internal fun `nao deve registrar caso a chave cadastrada ja exista`() {
        val exception =
            StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("Chave pix já foi cadastrada"))

        val resposta = TratamentoExcecoesHandler().handle(HttpRequest.GET<Any>("/"), exception)

        with(resposta){
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
            assertEquals("Chave pix já foi cadastrada", (body() as JsonError).message)
            assertNotNull(body())
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() = Mockito.mock(PixGrpcServiceGrpc.PixGrpcServiceBlockingStub::class.java)
    }

}