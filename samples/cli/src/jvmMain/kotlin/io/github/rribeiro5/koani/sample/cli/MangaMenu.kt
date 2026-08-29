package io.github.rribeiro5.koani.sample.cli

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.prompt
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.manga.Manga
import io.github.rribeiro5.koani.manga.MangaField

class MangaMenu(private val client: KoaniClient, private val terminal: Terminal) {

    suspend fun start() {
        while (true) {
            terminal.println("\n--- Manga Search ---")
            val query = terminal.prompt("Enter search term (or 'b' to go back)")?.trim() ?: continue

            if (query.lowercase() == "b") break

            val results = client.manga.getMangaList(
                query = query,
                limit = 10,
                fields = listOf(MangaField.ID, MangaField.TITLE)
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

    private suspend fun displayResults(items: List<Manga>): Boolean {
        while (true) {
            terminal.println("\nSearch Results:")
            items.forEachIndexed { index, manga ->
                terminal.println("${index + 1}. ${manga.title}")
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
        val manga = client.manga.getMangaDetails(id, fields = MangaField.entries)
        terminal.println("\n--- ${manga.title} ---")
        terminal.println("ID: ${manga.id}")
        terminal.println("Mean Score: ${manga.mean ?: "N/A"}")
        terminal.println("Status: ${manga.status ?: "N/A"}")
        terminal.println("Volumes: ${manga.numVolumes ?: "N/A"}")
        terminal.println("Chapters: ${manga.numChapters ?: "N/A"}")
        terminal.println("Synopsis: ${manga.synopsis?.take(200)}...")
        terminal.prompt("\nPress Enter to go back to the list", default = "")
    }
}
