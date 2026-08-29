package io.github.rribeiro5.koani.sample.cli

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.anime.Anime
import io.github.rribeiro5.koani.anime.AnimeField

class AnimeMenu(private val client: KoaniClient, private val terminal: Terminal) {

    suspend fun start() {
        while (true) {
            terminal.println("\n--- Anime Search ---")
            val query = terminal.prompt("Enter search term (or 'b' to go back)")?.trim() ?: continue

            if (query.lowercase() == "b") break

            val results = client.anime.getAnimeList(
                query = query,
                limit = 10,
                fields = listOf(AnimeField.ID, AnimeField.TITLE)
            )
            if (results.data.isEmpty()) {
                terminal.println("No results found for '$query'")
                continue
            }

            if (displayResults(results.data)) {
                break // Go back to main menu
            }
        }
    }

    private suspend fun displayResults(items: List<Anime>): Boolean {
        while (true) {
            terminal.println("\nSearch Results:")
            items.forEachIndexed { index, anime ->
                terminal.println("${index + 1}. ${anime.title}")
            }
            terminal.println("m. Back to Main Menu")
            terminal.println("b. Back to Search")

            val choices = (1..items.size).map { it.toString() } + listOf("m", "b")
            val selection = terminal.prompt("\nSelect an option", choices = choices, default = "b")

            when (selection) {
                "m" -> return true
                "b" -> return false
                else -> {
                    val index = selection?.toIntOrNull()?.minus(1)
                    if (index != null && index in items.indices) {
                        showDetails(items[index].id)
                    }
                }
            }
        }
    }

    private suspend fun showDetails(id: Int) {
        val anime = client.anime.getAnimeDetails(id, fields = AnimeField.entries)
        terminal.println("\n--- ${anime.title} ---")
        terminal.println("ID: ${anime.id}")
        terminal.println("Mean Score: ${anime.mean ?: "N/A"}")
        terminal.println("Status: ${anime.status ?: "N/A"}")
        terminal.println("Episodes: ${anime.numEpisodes ?: "N/A"}")
        terminal.println("Synopsis: ${anime.synopsis?.take(200)}...")
        terminal.prompt("\nPress Enter to go back to the list", default = "")
    }
}
