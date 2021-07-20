package br.com.erivelton.rest.pix.shared.config.excecao

import java.lang.RuntimeException

class ChavePixDuplicadaException(mensagem: String = "Chave já está registrada no sistema") : RuntimeException(mensagem) {

}
