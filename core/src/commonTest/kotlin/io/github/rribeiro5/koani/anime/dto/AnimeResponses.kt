package io.github.rribeiro5.koani.anime.dto

object AnimeResponses {
    val ANIME_LIST = """
        {
          "data": [
            {
              "node": {
                "id": 1,
                "title": "Cowboy Bebop",
                "main_picture": {
                  "medium": "https://api-cdn.myanimelist.net/images/anime/4/19644.jpg",
                  "large": "https://api-cdn.myanimelist.net/images/anime/4/19644l.jpg"
                }
              }
            }
          ],
          "paging": {
            "next": "https://api.myanimelist.net/v2/anime?offset=1"
          }
        }
    """.trimIndent()

    val ANIME_DETAILS = """
        {
          "id": 1,
          "title": "Cowboy Bebop",
          "main_picture": {
            "medium": "https://api-cdn.myanimelist.net/images/anime/4/19644.jpg",
            "large": "https://api-cdn.myanimelist.net/images/anime/4/19644l.jpg"
          }
        }
    """.trimIndent()
}
