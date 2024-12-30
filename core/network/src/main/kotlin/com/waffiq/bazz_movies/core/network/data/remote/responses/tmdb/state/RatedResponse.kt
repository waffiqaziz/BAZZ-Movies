package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state

import com.squareup.moshi.JsonClass

sealed class RatedResponse {
  @JsonClass(generateAdapter = true)
  data class Value(val value: Double) : RatedResponse()

  object Unrated : RatedResponse()
}
