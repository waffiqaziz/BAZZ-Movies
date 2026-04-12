package com.waffiq.bazz_movies.core.network.data.remote.datasource.trending

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TrendingApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.PageHelper.createPager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrendingRemoteDataSource @Inject constructor(
  private val trendingApiService: TrendingApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TrendingRemoteDataSourceInterface {

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      trendingApiService.getTrendingThisWeek(region, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTrendingToday(region: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      trendingApiService.getTrendingToday(region, page).results
    }.flow.flowOn(ioDispatcher)
}
