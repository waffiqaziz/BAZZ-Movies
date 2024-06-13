package com.waffiq.bazz_movies.domain.usecase.local_database

import com.waffiq.bazz_movies.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface LocalDatabaseUseCase {
  val favoriteMoviesFromDB: Flow<List<Favorite>>
  val watchlistMovieFromDB: Flow<List<Favorite>>
  val watchlistTvFromDB: Flow<List<Favorite>>
  val favoriteTvFromDB: Flow<List<Favorite>>
  suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit)
  suspend fun deleteFromDB(fav: Favorite)
  suspend fun deleteAll(callback: (Int) -> Unit)
  suspend fun isFavoriteDB(id: Int, mediaType: String): Boolean
  suspend fun isWatchlistDB(id: Int, mediaType: String): Boolean
  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite)
  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite)
}