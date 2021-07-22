package br.com.erivelton.rest.pix.chave.controle

import br.com.erivelton.pix.*
import br.com.erivelton.rest.pix.chave.dto.resposta.DetalhesChavePixResposta
import br.com.erivelton.rest.pix.shared.config.factory.GrpcClientFactory
import br.com.erivelton.rest.pix.shared.config.handlers.TratamentoExcecoesHandler
import com.google.protobuf.Timestamp
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
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class BuscaChaveControleTest{

    @field:Inject
    lateinit var grpcClient: BuscaGrpcServiceGrpc.BuscaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    lateinit var clienteIdTeste: String

    @BeforeEach
    fun setup(){
        clienteIdTeste = "5260263c-a3c1-4727-ae32-3bdb2538841b"
    }

    @Test
    internal fun `deve buscar detalhes da chave pix de acordo com o pix id e cliente id`() {
        val momento = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        val respostaGrpc = DadosPixResposta.newBuilder()
            .setPixId("1")
            .setClienteId(clienteIdTeste)
            .setTipoChave(TipoChave.CPF)
            .setValorChave("23432479282")
            .setNome("Yuri Matheus")
            .setCpf("23432479282")
            .setDadosConta(DadosConta.newBuilder()
                .setInstituicao("ITAÚ UNIBANCO S.A.")
                .setAgencia("0001")
                .setNumero("123455")
                .setTipoContaBcb(TipoContaBcb.CACC)
                .build()
            )
            .setMomento(Timestamp.newBuilder()
                .setNanos(momento.nano)
                .setSeconds(momento.epochSecond)
                .build()
            ).build()

        BDDMockito.given(grpcClient.buscaPix(Mockito.any())).willReturn(respostaGrpc)
        val get = HttpRequest.GET<DetalhesChavePixResposta>("/api/chaves/1/clientes/$clienteIdTeste")
        val respostaFinal = client.toBlocking().exchange(get, Any::class.java)


        with(respostaFinal){
            assertNotNull(respostaFinal)
            assertEquals(HttpStatus.OK, status)
//            assertEquals("1", body().pixId)
//            assertEquals(clienteIdTeste, body().clienteId)
        }



    }

    @Test
    internal fun `nao deve validar caso cliente Id nao seja inserido`() {
        val throwGerado = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange(HttpRequest.GET<Any>("/api/chaves/1/clientes/"), Any::class.java)
        }

        with(throwGerado){
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertEquals("Page Not Found", message)
        }
    }

    @Test
    internal fun `nao deve retornar a chave pix caso nao encontrada`() {
        val exception = StatusRuntimeException(Status.NOT_FOUND.withDescription("Chave pix não foi encontrada ou usuário inválido!!"))

        val resposta = TratamentoExcecoesHandler().handle(HttpRequest.GET<Any>("/"), exception)

        with(resposta){
            assertNotNull(resposta)
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertEquals("NOT_FOUND: Chave pix não foi encontrada ou usuário inválido!!", (body() as JsonError).message)
        }
    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class buscaPixBlockingStub{

        @Singleton
        fun BuscaPixMockStub() = Mockito.mock(BuscaGrpcServiceGrpc.BuscaGrpcServiceBlockingStub::class.java)
    }
}