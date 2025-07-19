package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ProviderResponse(
  @Json(name = "logo_path")
  val logoPath: String?,

  @Json(name = "provider_id")
  val providerId: Int?,

  @Json(name = "provider_name")
  val providerName: String?,

  @Json(name = "display_priority")
  val displayPriority: Int?
)
