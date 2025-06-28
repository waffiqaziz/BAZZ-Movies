package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CountryProviderDataResponse(

  @Json(name = "link")
  val link: String?,

  @Json(name = "ads")
  val ads: List<ProviderResponse>?,

  @Json(name = "buy")
  val buy: List<ProviderResponse>?,

  @Json(name = "flatrate")
  val flatrate: List<ProviderResponse>?,

  @Json(name = "free")
  val free: List<ProviderResponse>?,

  @Json(name = "rent")
  val rent: List<ProviderResponse>?,
)
