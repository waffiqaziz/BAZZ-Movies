package com.waffiq.bazz_movies.core.network.data.remote.datasource.asian

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.DiscoverApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.DateHelper.monthsAgo
import com.waffiq.bazz_movies.core.network.utils.helpers.DateHelper.monthsLater
import com.waffiq.bazz_movies.core.network.utils.helpers.PageHelper.createPager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AsianRemoteDataSource @Inject constructor(
  private val discoverApiService: DiscoverApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AsianRemoteDataSourceInterface {

  override fun getAnimeAllTime(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        genres = "16",
        country = "JP",
        page = page,
        withoutKeywords = ANIME_WITHOUT_KEYWORDS,
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getAnimeThisSeason(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        genres = "16",
        country = "JP",
        page = page,
        firstAirDateGte = THREE_MONTHS.monthsAgo,
        firstAirDateLte = ONE_MONTH.monthsLater,
        withoutKeywords = ANIME_WITHOUT_KEYWORDS,
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getDonghua(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        keywords = "315535",
        page = page,
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getAsianRomance(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        keywords = "9840",
        country = ASIAN_REGION,
        page = page,
        withoutGenres = "16|10764",
        withoutKeywords = "168812|240305|265777|258533|289844",
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getCostumeDrama(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        keywords = "195013",
        country = ASIAN_REGION,
        page = page,
      ).results
    }.flow.flowOn(ioDispatcher)

  companion object {
    const val THREE_MONTHS = 3L
    const val ONE_MONTH = 1L
    const val ASIAN_REGION = "CN|TW|KR|TH|ID|MY|JP"
    const val ANIME_WITHOUT_KEYWORDS = "195669|198385|256466|155477"
  }
}
