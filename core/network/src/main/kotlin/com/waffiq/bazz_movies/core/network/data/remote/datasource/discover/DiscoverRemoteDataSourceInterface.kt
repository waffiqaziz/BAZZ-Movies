package com.waffiq.bazz_movies.core.network.data.remote.datasource.discover

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import kotlinx.coroutines.flow.Flow

interface DiscoverRemoteDataSourceInterface {
  fun getMovieByGenres(genres: String, region: String): Flow<PagingData<MediaResponseItem>>
  fun getTvByGenres(genres: String, region: String): Flow<PagingData<MediaResponseItem>>
  fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaResponseItem>>
  fun getTvByKeywords(keywords: String): Flow<PagingData<MediaResponseItem>>
}
