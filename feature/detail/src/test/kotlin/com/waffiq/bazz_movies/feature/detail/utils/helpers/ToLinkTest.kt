package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.video.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.video.VideoItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import org.junit.Assert.assertEquals
import org.junit.Test

class ToLinkTest {

  private fun videoItem(
    key: String,
    official: Boolean = false,
    type: String = "Trailer",
    name: String = "name",
  ) = VideoItem(
    official = official,
    type = type,
    name = name,
    key = key,
  )

  private fun video(vararg items: VideoItem) = Video(results = items.toList())

  @Test
  fun toLink_multipleCase_shouldReturnCorrectly() {
    val cases = listOf(
      // return official trailer
      video(
        videoItem("trailer_key", official = true),
      ) to "trailer_key",

      // no official, return any valid trailer
      video(
        videoItem("teaser_key", official = false),
        videoItem("clip_key", official = false),
      ) to "teaser_key",

      // null return empty
      video() to "",

      // return only trailer, event if its not official
      video(
        videoItem("unofficial_trailer", official = false, type = "Trailer"),
        videoItem("official_teaser", official = true, type = "Teaser"),
        videoItem("clip_key", official = false),
      ) to "unofficial_trailer",
    )

    cases.forEach { (input, expected) ->
      assertEquals(expected, input.toLink())
    }
  }
}
