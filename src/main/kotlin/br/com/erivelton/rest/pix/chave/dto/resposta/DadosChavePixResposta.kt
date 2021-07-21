package br.com.erivelton.rest.pix.chave.dto.resposta

import br.com.erivelton.pix.DadosPixResposta
import br.com.erivelton.rest.pix.chave.enums.TipoChave
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DadosChavePixResposta(
    dadosPixResposta: DadosPixResposta
){
    val pixId: Long = dadosPixResposta.pixId.toLong()
    val clienteId: String = dadosPixResposta.clienteId
    val tipoChave: TipoChave = TipoChave.valueOf(dadosPixResposta.tipoChave.name)
    val valorChave: String = dadosPixResposta.valorChave
    val nome: String = dadosPixResposta.nome
    val cpf: String = dadosPixResposta.cpf
    val dadosContaResposta: DadosContaResposta = DadosContaResposta(dadosPixResposta.dadosConta)
    val momentoCriacao: String = converterTimestampEmString(dadosPixResposta.momento)

    private fun converterTimestampEmString(momento: Timestamp): String{
        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return LocalDateTime.ofEpochSecond(momento.seconds, momento.nanos, ZoneOffset.UTC).format(formatoData)
    }

}