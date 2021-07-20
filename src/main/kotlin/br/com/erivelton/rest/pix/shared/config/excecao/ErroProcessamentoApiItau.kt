package br.com.erivelton.rest.pix.shared.config.excecao

class ErroProcessamentoApiItau(mensagem: String = "Erro ao processar dados na API do itaú") : RuntimeException(mensagem)
