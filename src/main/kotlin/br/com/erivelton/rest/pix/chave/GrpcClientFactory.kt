package br.com.erivelton.rest.pix.chave

import br.com.erivelton.pix.PixGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun pixClientStub(@GrpcChannel("registrospix") channel: ManagedChannel) : PixGrpcServiceGrpc.PixGrpcServiceBlockingStub{
        return PixGrpcServiceGrpc.newBlockingStub(channel)
    }
}