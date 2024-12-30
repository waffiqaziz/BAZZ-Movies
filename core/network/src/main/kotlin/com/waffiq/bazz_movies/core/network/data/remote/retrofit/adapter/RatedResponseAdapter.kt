package com.waffiq.bazz_movies.core.network.data.remote.retrofit.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.ToJson
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse

class RatedResponseAdapter {
  @FromJson
  fun fromJson(rated: Any?): RatedResponse {
    return when (rated) {
      is Map<*, *> -> {
        val value = rated["value"] as? Double
        if (value != null) {
          RatedResponse.Value(value)
        } else {
          throw JsonDataException("Invalid rated field")
        }
      }
      is Boolean -> RatedResponse.Unrated
      else -> throw JsonDataException("Unknown type for rated field")
    }
  }

  @ToJson
  fun toJson(rated: RatedResponse): Any {
    return when (rated) {
      is RatedResponse.Value -> mapOf("value" to rated.value)
      is RatedResponse.Unrated -> false
    }
  }
}
