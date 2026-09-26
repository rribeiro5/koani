package io.github.rribeiro5.koani.forum.dto

internal object ForumResponses {
    val FORUM_BOARDS = """
        {
          "categories": [
            {
              "title": "MyAnimeList",
              "boards": [
                {
                  "id": 1,
                  "title": "News",
                  "description": "MAL News and Site Updates",
                  "subboards": [
                    {
                      "id": 1,
                      "title": "Announcements"
                    }
                  ]
                }
              ]
            }
          ]
        }
    """.trimIndent()

    val FORUM_TOPICS = """
        {
          "data": [
            {
              "id": 1,
              "title": "Welcome to MAL",
              "created_at": "2023-08-22T00:00:00Z",
              "created_by": {
                "id": 1,
                "name": "Admin",
                "forum_avator": "https://cdn.myanimelist.net/images/useranimelist/admin.jpg"
              },
              "number_of_posts": 10,
              "last_post_created_at": "2023-08-23T00:00:00Z",
              "last_post_created_by": {
                "id": 2,
                "name": "User",
                "forum_avator": null
              },
              "is_locked": false
            }
          ],
          "paging": {
            "next": "https://api.myanimelist.net/v2/forum/topics?offset=10"
          }
        }
    """.trimIndent()

    val FORUM_TOPIC_DETAIL = """
        {
          "data": {
            "title": "Welcome to MAL",
            "posts": [
              {
                "id": 1,
                "number": 1,
                "created_at": "2023-08-22T00:00:00Z",
                "created_by": {
                  "id": 1,
                  "name": "Admin",
                  "forum_avator": "https://cdn.myanimelist.net/images/useranimelist/admin.jpg"
                },
                "body": "Welcome to our community!",
                "signature": "Sent from MAL"
              }
            ],
            "poll": {
              "id": 1,
              "question": "Do you like MAL?",
              "close": false,
              "options": [
                {
                  "id": 1,
                  "text": "Yes",
                  "votes": 100
                },
                {
                  "id": 2,
                  "text": "No",
                  "votes": 5
                }
              ]
            }
          },
          "paging": {
            "next": "https://api.myanimelist.net/v2/forum/topic/1?offset=10"
          }
        }
    """.trimIndent()
}
