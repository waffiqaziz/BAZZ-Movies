package com.waffiq.bazz_movies.feature.list.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.discover.DiscoverRemoteDataSource
import com.waffiq.bazz_movies.feature.list.domain.repository.IListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListRepositoryImpl @Inject constructor(
  private val discoverRemoteDataSource: DiscoverRemoteDataSource,
) : IListRepository {

  override fun getMovieByGenres(genres: String, region: String): Flow<PagingData<MediaItem>> =
    discoverRemoteDataSource.getMovieByGenres(genres, region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getTvByGenres(genres: String, region: String): Flow<PagingData<MediaItem>> =
    discoverRemoteDataSource.getTvByGenres(genres, region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    discoverRemoteDataSource.getMovieByKeywords(keywords).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getTvByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    discoverRemoteDataSource.getTvByKeywords(keywords).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }
}
