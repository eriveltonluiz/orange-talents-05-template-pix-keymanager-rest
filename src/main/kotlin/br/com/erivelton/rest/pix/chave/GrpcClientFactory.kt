package br.com.erivelton.rest.pix.chave

import br.com.erivelton.pix.PixGrpcServiceGrpc
import br.com.erivelton.pix.RemovePixGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun registraPixClientStub(@GrpcChannel("pixGrpc") channel: ManagedChannel) : PixGrpcServiceGrpc.PixGrpcServiceBlockingStub{
        return PixGrpcServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removePixClientFactory(@GrpcChannel("pixGrpc") channel: ManagedChannel) : RemovePixGrpcServiceGrpc.RemovePixGrpcServiceBlockingStub{
        return RemovePixGrpcServiceGrpc.newBlockingStub(channel)
    }
}