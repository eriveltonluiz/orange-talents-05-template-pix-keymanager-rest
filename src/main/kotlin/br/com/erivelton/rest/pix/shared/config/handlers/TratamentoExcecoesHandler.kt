package br.com.erivelton.rest.pix.shared.config.handlers

import br.com.erivelton.rest.pix.shared.config.excecao.ChavePixDuplicadaException
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import java.lang.Exception
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Produces
@Singleton
@Requirements(Requires(classes = [StatusRuntimeException::class, ExceptionHandler::class]))
class TratamentoExcecoesHandler :
    ExceptionHandler<StatusRuntimeException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<*> {
        var status = HttpResponse.badRequest<Any>()

        val (httpStatus, message) = when (exception.status.code) {
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, "Chave pix já foi cadastrada")

            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, exception.message)

            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, exception.message)

            Status.FAILED_PRECONDITION.code -> Pair(HttpStatus.NOT_FOUND, "Cliente não foi encontrado na api do itaú")

            else -> Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível processar a requisição com o servidor GRPC")
        }

//        if(exception.status.code == Status.ALREADY_EXISTS.code)
//            status = HttpResponse.unprocessableEntity()
//
//        else if(exception.status.code == Status.INVALID_ARGUMENT.code)
//            status = HttpResponse.badRequest<Any>()

        return HttpResponse.status<JsonError>(httpStatus).body(JsonError(message))
//        errorResponseProcessor.processResponse(
//            ErrorContext.builder(request)
//                .cause(exception)
//                .errorMessage(message)
//                .build(), status
//        )
    }

}