package br.com.erivelton.rest.pix.chave.dto.resposta

import br.com.erivelton.pix.DadosConta
import br.com.erivelton.rest.pix.chave.enums.TipoContaBcb

class DadosContaResposta (
    dadosConta: DadosConta
){
    val instituicao: String? = dadosConta.instituicao
    val agencia: String = dadosConta.agencia
    val numero: String = dadosConta.numero
    val tipoConta: TipoContaBcb = TipoContaBcb.valueOf(dadosConta.tipoContaBcb.name)
}