package br.com.erivelton.rest.pix

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.erivelton.rest.pix")
		.start()
}

