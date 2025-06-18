package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

data class WatchProviders(
  val id: Int?,
  val results: Map<String, CountryProviderData>?,
)
