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

    val USER_MANGA_LIST = """
        {
          "data": [
            {
              "node": {
                "id": 1,
                "title": "One Piece"
              },
              "list_status": {
                "status": "reading",
                "score": 10,
                "num_volumes_read": 5,
                "num_chapters_read": 50,
                "is_rereading": false,
                "updated_at": "2024-01-01T00:00:00Z"
              }
            }
          ],
          "paging": {}
        }
    """.trimIndent()

    val UPDATE_USER_MANGA_LIST_STATUS = """
        {
          "status": "reading",
          "score": 10,
          "num_volumes_read": 5,
          "num_chapters_read": 50,
          "is_rereading": false,
          "updated_at": "2024-01-01T00:00:00Z"
        }
    """.trimIndent()
}
