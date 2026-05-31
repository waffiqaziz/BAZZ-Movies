package com.waffiq.bazz_movies.core.database.data.datasource

import android.database.sqlite.SQLiteException
import android.util.Log
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDataSourceInterface {
  val getFavoriteMovies: Flow<List<FavoriteEntity>>
  val getFavoriteTv: Flow<List<FavoriteEntity>>
  val getWatchlistMovies: Flow<List<FavoriteEntity>>
  val getWatchlistTv: Flow<List<FavoriteEntity>>

  // use integer to save memory, I don't think it will more than 2.1 billion rows
  suspend fun insert(favoriteEntityList: FavoriteEntity): DbResult<Int>
  suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): DbResult<Int>
  suspend fun deleteAll(): DbResult<Int>
  suspend fun isFavorite(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun isWatchlist(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun update(favoriteEntity: FavoriteEntity): DbResult<Unit>

  @Suppress("TooGenericExceptionCaught")
  suspend fun <T> executeDbOperation(operation: suspend () -> T): DbResult<T> =
    try {
      DbResult.Success(operation())
    } catch (e: SQLiteException) {
      Log.e("DatabaseError", "SQLite error: ${e.message}")
      DbResult.Error("Database error")
    } catch (e: Exception) {
      Log.e("DatabaseError", "Unexpected error: ${e.message}")
      DbResult.Error("Unknown error")
    }
}
