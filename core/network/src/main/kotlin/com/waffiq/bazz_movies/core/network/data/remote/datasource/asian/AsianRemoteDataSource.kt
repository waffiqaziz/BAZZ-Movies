package com.waffiq.bazz_movies.core.network.data.remote.datasource.asian

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.ANIMATION
import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.REALITY
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.BISEXUAL_MAN
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.BOYS_LOVE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.COSTUME_DRAMA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.DONGHUA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.ECCHI
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.EROTIC
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.GAY_RELATIONSHIP
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.GAY_ROMANCE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.GIRLS_LOVE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.HENTAI
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.LESBIAN
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.LESBIAN_RELATIONSHIP
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.ROMANCE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.SOFTCORE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.CHINA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.INDONESIA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.JAPAN
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.KOREA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.MALAYSIA
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.TAIWAN
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.THAILAND
import com.waffiq.bazz_movies.core.network.data.remote.query.DiscoverTvParams
import com.waffiq.bazz_movies.core.network.data.remote.query.toQueryMap
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
        DiscoverTvParams(
          genres = listOf(ANIMATION),
          originCountry = listOf(JAPAN),
          page = page,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getAnimeThisSeason(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        DiscoverTvParams(
          genres = listOf(ANIMATION),
          originCountry = listOf(JAPAN),
          page = page,
          firstAirDateGte = THREE_MONTHS.monthsAgo,
          firstAirDateLte = ONE_MONTH.monthsLater,
          withoutKeywords = ANIME_WITHOUT_KEYWORDS,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getDonghua(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        DiscoverTvParams(
          keywords = listOf(DONGHUA),
          page = page,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getAsianRomance(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        DiscoverTvParams(
          keywords = listOf(ROMANCE),
          originCountry = ASIAN_REGION,
          page = page,
          withoutGenres = ROMANCE_DRAMA_WITHOUT_GENRES,
          withoutKeywords = ROMANCE_DRAMA_WITHOUT_KEYWORDS,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getCostumeDrama(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        DiscoverTvParams(
          keywords = listOf(COSTUME_DRAMA),
          originCountry = ASIAN_REGION,
          page = page,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  override fun getRealityShow(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.discoverTv(
        DiscoverTvParams(
          originCountry = ASIAN_REGION,
          type = REALITY_SHOW_TYPE,
          page = page,
        ).toQueryMap(),
      ).results
    }.flow.flowOn(ioDispatcher)

  companion object {
    const val THREE_MONTHS = 3L
    const val ONE_MONTH = 1L
    const val REALITY_SHOW_TYPE = "3"

    val ASIAN_REGION = listOf(CHINA, INDONESIA, JAPAN, KOREA, MALAYSIA, TAIWAN, THAILAND)

    val ANIME_WITHOUT_KEYWORDS = listOf(ECCHI, EROTIC, HENTAI, SOFTCORE)

    val ROMANCE_DRAMA_WITHOUT_GENRES = listOf(ANIMATION, REALITY)
    val ROMANCE_DRAMA_WITHOUT_KEYWORDS = listOf(
      BISEXUAL_MAN,
      GAY_ROMANCE,
      GAY_RELATIONSHIP,
      BOYS_LOVE,
      GIRLS_LOVE,
      LESBIAN,
      LESBIAN_RELATIONSHIP,
    )
  }
}
