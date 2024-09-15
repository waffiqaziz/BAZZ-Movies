package com.waffiq.bazz_movies.domain.usecase.local_database

import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.utils.resultstate.DbResult
import kotlinx.coroutines.flow.Flow

interface LocalDatabaseUseCase {
  val favoriteMoviesFromDB: Flow<List<Favorite>>
  val watchlistMovieFromDB: Flow<List<Favorite>>
  val watchlistTvFromDB: Flow<List<Favorite>>
  val favoriteTvFromDB: Flow<List<Favorite>>

  suspend fun insertToDB(fav: Favorite): DbResult<Int>
  suspend fun deleteFromDB(fav: Favorite): DbResult<Int>
  suspend fun deleteAll(): DbResult<Int>
  suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int>
  suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int>
}
