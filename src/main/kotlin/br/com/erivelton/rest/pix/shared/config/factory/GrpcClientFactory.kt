package br.com.erivelton.rest.pix.shared.config.factory

import br.com.erivelton.pix.BuscaGrpcServiceGrpc
import br.com.erivelton.pix.PixGrpcServiceGrpc
import br.com.erivelton.pix.RemovePixGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("pixGrpc") val channel: ManagedChannel) {

    @Singleton
    fun registraPixClientStub() : PixGrpcServiceGrpc.PixGrpcServiceBlockingStub{
        return PixGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removePixClientFactory() : RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub{
        return RemovePixGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun buscaPixClientFactory() : BuscaGrpcServiceGrpc.BuscaGrpcServiceBlockingStub{
        return BuscaGrpcServiceGrpc.newBlockingStub(channel)
    }
}