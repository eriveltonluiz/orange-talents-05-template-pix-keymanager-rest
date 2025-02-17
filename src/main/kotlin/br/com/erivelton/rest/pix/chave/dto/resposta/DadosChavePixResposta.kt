package br.com.erivelton.rest.pix.chave.dto.resposta

import br.com.erivelton.pix.DadosPixGeralResposta
import br.com.erivelton.pix.DadosPixResposta
import br.com.erivelton.rest.pix.chave.enums.TipoChave
import br.com.erivelton.rest.pix.chave.enums.TipoConta
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DadosChavePixResposta(
    dadosPixResposta: DadosPixGeralResposta.PixGeralResposta
){
    val pixId: Long = dadosPixResposta.pixId.toLong()
    val clienteId: String = dadosPixResposta.clienteId
    val valorChave: String = dadosPixResposta.valorChave
    val tipoChave: TipoChave = TipoChave.valueOf(dadosPixResposta.tipoChave.name)
    val tipoConta: TipoConta = TipoConta.valueOf(dadosPixResposta.tipoConta.name)
    val momentoCriacao: String = converterTimestampEmString(dadosPixResposta.momento)

    private fun converterTimestampEmString(momento: Timestamp): String{
        val formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return LocalDateTime.ofEpochSecond(momento.seconds, momento.nanos, ZoneOffset.UTC).format(formatoData)
    }

}