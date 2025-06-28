package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.CountryProviderDataResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.ProviderResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detailmovietv.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.CountryProviderData
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders

object WatchProvidersMapper {

  fun WatchProvidersResponse.toWatchProviders(): WatchProviders = WatchProviders(
    id = id,
    results = results?.mapValues { (_, countryData) ->
      countryData.toDomain()
    }
  )

  private fun CountryProviderDataResponse.toDomain(): CountryProviderData = CountryProviderData(
    link = link,
    ads = ads?.map { it.toProvider() },
    buy = buy?.map { it.toProvider() },
    flatrate = flatrate?.map { it.toProvider() },
    free = free?.map { it.toProvider() },
    rent = rent?.map { it.toProvider() },
  )

  private fun ProviderResponse.toProvider(): Provider = Provider(
    logoPath = logoPath,
    providerId = providerId,
    providerName = providerName,
    displayPriority = displayPriority
  )
}
