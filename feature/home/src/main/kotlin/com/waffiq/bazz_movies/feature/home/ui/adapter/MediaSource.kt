package com.waffiq.bazz_movies.feature.home.ui.adapter

sealed class MediaSource {
  object Trending : MediaSource()
  data class Typed(val mediaType: String) : MediaSource()
}
