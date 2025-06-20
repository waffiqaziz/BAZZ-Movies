package com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders

data class CountryProviderData(
  val link: String?,
  val flatrate: List<Provider>?, // a.k.a. streaming
  val rent: List<Provider>?,
  val buy: List<Provider>?,
  val free: List<Provider>?
)
