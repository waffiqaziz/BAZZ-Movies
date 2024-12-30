package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.tv

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ExternalIdResponse(

  @Json(name = "imdb_id")
  val imdbId: String? = null,

  @Json(name = "freebase_mid")
  val freebaseMid: String? = null,

  @Json(name = "tvdb_id")
  val tvdbId: Int? = null,

  @Json(name = "freebase_id")
  val freebaseId: String? = null,

  @Json(name = "id")
  val id: Int? = null,

  @Json(name = "twitter_id")
  val twitterId: String? = null,

  @Json(name = "tvrage_id")
  val tvrageId: Int? = null,

  @Json(name = "facebook_id")
  val facebookId: String? = null,

  @Json(name = "instagram_id")
  val instagramId: String? = null
)
