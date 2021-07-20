package br.com.erivelton.rest.pix.shared.config.handlers

import br.com.erivelton.rest.pix.shared.config.excecao.ChavePixDuplicadaException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError

@Controller("/notfound")
class trata {

    @Error(exception = ChavePixDuplicadaException::class, status = HttpStatus.UNPROCESSABLE_ENTITY, global = true)
    fun chaveDuplicadaHandler(request: HttpRequest<*>, ex: ChavePixDuplicadaException): HttpResponse<JsonError> {
        println("teas")
        val erro = JsonError(ex.message)
        return HttpResponse.unprocessableEntity<JsonError>().body(erro)
    }
}