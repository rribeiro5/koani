package io.github.rribeiro5.koani.user.dto

internal object UserResponses {
    val USER_DETAILS = """
        {
          "id": 1,
          "name": "testuser",
          "picture": "https://cdn.myanimelist.net/images/userimages/1.jpg",
          "gender": "male",
          "birthday": "1990-01-01",
          "location": "Tokyo",
          "joined_at": "2020-01-01T00:00:00+00:00",
          "anime_statistics": {
            "num_items_watching": 1,
            "num_items_completed": 2,
            "num_items_on_hold": 3,
            "num_items_dropped": 4,
            "num_items_plan_to_watch": 5,
            "num_items": 15,
            "num_days_watched": 1.5,
            "num_days_watching": 0.5,
            "num_days_completed": 1.0,
            "num_days_on_hold": 0.0,
            "num_days_dropped": 0.0,
            "num_days": 3.0,
            "num_episodes": 100,
            "num_times_rewatched": 10,
            "mean_score": 8.5
          },
          "time_zone": "Asia/Tokyo",
          "is_supporter": true
        }
    """.trimIndent()
}
