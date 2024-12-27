package com.waffiq.bazz_movies.core.database.data.repository

import com.waffiq.bazz_movies.core.database.data.datasource.LocalDataSource
import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.toFavorite
import com.waffiq.bazz_movies.core.database.utils.DatabaseMapper.toFavoriteEntity
import com.waffiq.bazz_movies.core.domain.Favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepositoryImpl @Inject constructor(
  private val localDataSource: LocalDataSource
) : IDatabaseRepository {
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

  override suspend fun insertToDB(fav: Favorite): com.waffiq.bazz_movies.core.database.utils.DbResult<Int> =
    localDataSource.insert(fav.toFavoriteEntity())

  override suspend fun deleteFromDB(fav: Favorite): com.waffiq.bazz_movies.core.database.utils.DbResult<Int> =
    localDataSource.deleteItemFromDB(fav.mediaId, fav.mediaType)

  override suspend fun deleteAll(): com.waffiq.bazz_movies.core.database.utils.DbResult<Int> =
    localDataSource.deleteAll()

  override suspend fun isFavoriteDB(
    id: Int,
    mediaType: String
  ): com.waffiq.bazz_movies.core.database.utils.DbResult<Boolean> =
    localDataSource.isFavorite(id, mediaType)

  override suspend fun isWatchlistDB(
    id: Int,
    mediaType: String
  ): com.waffiq.bazz_movies.core.database.utils.DbResult<Boolean> =
    localDataSource.isWatchlist(id, mediaType)

  override suspend fun updateFavoriteItemDB(
    isDelete: Boolean,
    fav: Favorite
  ): com.waffiq.bazz_movies.core.database.utils.DbResult<Int> =
    if (isDelete) {
      // update set is_favorite = false (item on favorite to delete)
      localDataSource.update(
        isFavorite = false,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else {
      // update set is_favorite = true (add favorite item already on watchlist)
      localDataSource.update(
        isFavorite = true,
        isWatchlist = fav.isWatchlist,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }

  override suspend fun updateWatchlistItemDB(
    isDelete: Boolean,
    fav: Favorite
  ): com.waffiq.bazz_movies.core.database.utils.DbResult<Int> =
    if (isDelete) { // update set is_watchlist = false (item on watchlist to delete)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = false,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    } else { // update set is_watchlist = true (add watchlist item already on favorite)
      localDataSource.update(
        isFavorite = fav.isFavorite,
        isWatchlist = true,
        id = fav.mediaId,
        mediaType = fav.mediaType
      )
    }
// endregion DATABASE
}