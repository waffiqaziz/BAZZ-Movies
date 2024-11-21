package com.waffiq.bazz_movies.core.database.domain.repository

import com.waffiq.bazz_movies.core.database.data.model.Favorite
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.flow.Flow

interface IDatabaseRepository {
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
