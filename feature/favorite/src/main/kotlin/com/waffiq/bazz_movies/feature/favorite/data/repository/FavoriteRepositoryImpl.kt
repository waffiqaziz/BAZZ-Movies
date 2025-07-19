package com.waffiq.bazz_movies.feature.favorite.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IFavoriteRepository {

  override fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getFavoriteMovies(sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getFavoriteTv(sessionId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }
}
