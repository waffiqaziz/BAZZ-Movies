package com.waffiq.bazz_movies.core.database.data.repository

import com.waffiq.bazz_movies.core.database.data.datasource.FavoriteLocalDataSource
import com.waffiq.bazz_movies.core.database.domain.repository.IFavoriteLocalDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toFavorite
import com.waffiq.bazz_movies.core.database.utils.FavoriteMapper.toFavoriteEntity
import com.waffiq.bazz_movies.core.models.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteLocalDatabaseRepositoryImpl @Inject constructor(
  private val localDataSource: FavoriteLocalDataSource,
) : IFavoriteLocalDatabaseRepository {
  // region DATABASE
  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistMovies.map { list ->
      list.map { it.toFavorite() }
    }

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    localDataSource.getWatchlistTv.map { list ->
      list.map { it.toFavorite() }
    }

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    localDataSource.getFavoriteTv.map { list ->
      list.map { it.toFavorite() }
    }

  override suspend fun insertToDB(fav: Favorite): DbResult<Int> =
    localDataSource.insert(fav.toFavoriteEntity())

  override suspend fun deleteFromDB(fav: Favorite): DbResult<Int> =
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)

  override suspend fun deleteAll(): DbResult<Int> = localDataSource.deleteAll()

  override suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDataSource.isFavorite(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDataSource.isWatchlist(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Unit> =
    localDataSource.update(fav.toFavoriteEntity().copy(isFavorite = !isDelete))

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Unit> =
    localDataSource.update(fav.toFavoriteEntity().copy(isWatchlist = !isDelete))
}
