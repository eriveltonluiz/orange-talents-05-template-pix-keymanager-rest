package br.com.erivelton.rest.pix.shared.config.handlers

import br.com.erivelton.rest.pix.shared.config.excecao.ChavePixDuplicadaException
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import java.lang.Exception
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Produces
@Singleton
@Requirements(Requires(classes = [StatusRuntimeException::class, ExceptionHandler::class]))
class TratamentoExcecoesHandler(private val errorResponseProcessor: ErrorResponseProcessor<Any>) :
    ExceptionHandler<StatusRuntimeException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<*> {
        var status = HttpResponse.badRequest<Any>()

        if(exception.status.code == Status.ALREADY_EXISTS.code)
            status = HttpResponse.unprocessableEntity()

        else if(exception.status.code == Status.INVALID_ARGUMENT.code)
            status = HttpResponse.badRequest<Any>()

        return errorResponseProcessor.processResponse(
            ErrorContext.builder(request)
                .cause(exception)
                .errorMessage(exception.message)
                .build(), status
        )
    }

}