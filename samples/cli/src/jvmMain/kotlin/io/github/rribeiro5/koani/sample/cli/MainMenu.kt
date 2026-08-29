package io.github.rribeiro5.koani.sample.cli

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt
import io.github.rribeiro5.koani.KoaniClient

class MainMenu(private val client: KoaniClient, private val terminal: Terminal) {

    suspend fun run() {
        terminal.println("Welcome to Koani Search Engine!")
        
        while (true) {
            terminal.println("\nMain Menu:")
            terminal.println("1. Anime Search")
            terminal.println("2. Manga Search")
            terminal.println("q. Quit")

            val selection = terminal.prompt("\nSelect an option", choices = listOf("1", "2", "q"), default = "q")

            when (selection) {
                "1" -> AnimeMenu(client, terminal).start()
                "2" -> MangaMenu(client, terminal).start()
                "q" -> {
                    terminal.println("Goodbye!")
                    break
                }
            }
        }
    }
}
