package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

data class WatchProvidersItem(
  val link: String?,
  val ads: List<Provider>?,
  val buy: List<Provider>?,
  val flatrate: List<Provider>?, // a.k.a. streaming
  val free: List<Provider>?,
  val rent: List<Provider>?,
)
