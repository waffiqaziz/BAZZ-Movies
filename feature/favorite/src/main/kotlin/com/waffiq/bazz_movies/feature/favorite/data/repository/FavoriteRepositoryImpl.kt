package com.waffiq.bazz_movies.feature.favorite.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toResultItem
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

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteMovies(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingFavoriteTv(sessionId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }
}
