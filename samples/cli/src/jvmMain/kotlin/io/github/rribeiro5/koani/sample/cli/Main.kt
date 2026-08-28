package io.github.rribeiro5.koani.sample.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import io.github.rribeiro5.koani.KoaniClient
import kotlinx.coroutines.runBlocking

class HelloKoani : CliktCommand() {
    val clientId by option(help = "MyAnimeList Client ID").prompt("Enter your Client ID")

    override fun run() = runBlocking {
        echo("Initializing KoaniClient...")
        val client = KoaniClient.Builder(clientId).build()
        
        echo("KoaniClient initialized successfully with Client ID: ${client.auth.clientId}")
        echo("Hello from Koani CLI Sample!")
    }
}

fun main(args: Array<String>) = HelloKoani().main(args)
