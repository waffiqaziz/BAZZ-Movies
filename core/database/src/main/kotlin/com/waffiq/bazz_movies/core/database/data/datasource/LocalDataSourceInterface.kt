package com.waffiq.bazz_movies.core.database.data.datasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
  val getFavoriteMovies: Flow<List<FavoriteEntity>>
  val getFavoriteTv: Flow<List<FavoriteEntity>>
  val getWatchlistMovies: Flow<List<FavoriteEntity>>
  val getWatchlistTv: Flow<List<FavoriteEntity>>

  // use integer to save memory, i don't think it will more than 2.1 billion rows
  suspend fun insert(favoriteEntityList: FavoriteEntity): DbResult<Int>
  suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): DbResult<Int>
  suspend fun deleteAll(): DbResult<Int>
  suspend fun isFavorite(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun isWatchlist(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun update(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    id: Int,
    mediaType: String,
  ): DbResult<Int>

  @Suppress("TooGenericExceptionCaught")
  suspend fun <T> executeDbOperation(operation: suspend () -> T): DbResult<T> =
    try {
      // Directly execute the Room operation
      DbResult.Success(operation())
    } catch (e: SQLiteConstraintException) {
      Log.e("DatabaseError", "Unique constraint violation: ${e.message}")
      DbResult.Error("Unique constraint violation")
    } catch (e: SQLiteFullException) {
      Log.e("DatabaseError", "Database is full: ${e.message}")
      DbResult.Error("Database is full")
    } catch (e: SQLiteDiskIOException) {
      Log.e("DatabaseError", "Disk IO issue: ${e.message}")
      DbResult.Error("Disk IO issue")
    } catch (e: SQLiteException) {
      Log.e("DatabaseError", "SQLite exception: ${e.message}")
      DbResult.Error("SQLite exception")
    } catch (e: Exception) {
      Log.e("DatabaseError", "Unknown error: ${e.message}")
      DbResult.Error("Unknown error")
    }
}
