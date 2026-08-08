package io.github.rribeiro5.koani.manga.dto

object MangaResponses {
    val MANGA_LIST = """
        {
          "data": [
            {
              "node": {
                "id": 1,
                "title": "One Piece",
                "main_picture": {
                  "medium": "https://api-cdn.myanimelist.net/images/manga/2/253146.jpg",
                  "large": "https://api-cdn.myanimelist.net/images/manga/2/253146l.jpg"
                }
              }
            }
          ],
          "paging": {
            "next": "https://api.myanimelist.net/v2/manga?offset=1"
          }
        }
    """.trimIndent()

    val MANGA_DETAILS = """
        {
          "id": 1,
          "title": "One Piece",
          "main_picture": {
            "medium": "https://api-cdn.myanimelist.net/images/manga/2/253146.jpg",
            "large": "https://api-cdn.myanimelist.net/images/manga/2/253146l.jpg"
          }
        }
    """.trimIndent()

    val MANGA_RANKING = """
        {
          "data": [
            {
              "node": {
                "id": 1,
                "title": "One Piece"
              },
              "ranking": {
                "rank": 1,
                "previous_rank": 2
              }
            }
          ],
          "paging": {}
        }
    """.trimIndent()
}
