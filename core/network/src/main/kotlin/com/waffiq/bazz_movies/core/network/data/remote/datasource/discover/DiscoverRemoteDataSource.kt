package com.waffiq.bazz_movies.core.network.data.remote.datasource.discover

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.DiscoverApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.PageHelper.createPager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DiscoverRemoteDataSource @Inject constructor(
  private val discoverApiService: DiscoverApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DiscoverRemoteDataSourceInterface {

  override fun getMovieByGenres(
    genres: String,
    region: String,
  ): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.getMovieByGenres(genres, region, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTvByGenres(genres: String, region: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.getTvByGenres(genres, region, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.getMovieByKeywords(keywords, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTvByKeywords(keywords: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      discoverApiService.getTvByKeywords(keywords, page).results
    }.flow.flowOn(ioDispatcher)
}
