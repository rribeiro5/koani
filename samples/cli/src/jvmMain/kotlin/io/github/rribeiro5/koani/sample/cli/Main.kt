package io.github.rribeiro5.koani.sample.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.terminal
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import io.github.rribeiro5.koani.KoaniClient
import kotlinx.coroutines.runBlocking

class KoaniCli : CliktCommand() {
    val clientId by option(help = "MyAnimeList Client ID").prompt("Enter your Client ID")

    override fun run() = runBlocking {
        val client = KoaniClient.Builder(clientId).build()
        MainMenu(client, terminal).run()
    }
}

fun main(args: Array<String>) = KoaniCli().main(args)
