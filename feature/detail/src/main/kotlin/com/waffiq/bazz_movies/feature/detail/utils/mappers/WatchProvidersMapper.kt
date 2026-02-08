package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.ProviderResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponseItem
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem

object WatchProvidersMapper {

  fun WatchProvidersResponse.toWatchProviders(): WatchProviders =
    WatchProviders(
      id = id,
      results = results?.mapValues { (_, countryData) ->
        countryData.toWatchProviders()
      },
    )

  private fun WatchProvidersResponseItem.toWatchProviders(): WatchProvidersItem =
    WatchProvidersItem(
      link = link,
      ads = ads?.map { it.toProvider() },
      buy = buy?.map { it.toProvider() },
      flatrate = flatrate?.map { it.toProvider() },
      free = free?.map { it.toProvider() },
      rent = rent?.map { it.toProvider() },
    )

  private fun ProviderResponse.toProvider(): Provider =
    Provider(
      logoPath = logoPath,
      providerId = providerId,
      providerName = providerName,
      displayPriority = displayPriority,
    )
}
