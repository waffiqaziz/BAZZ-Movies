package com.waffiq.bazz_movies.data.local.datasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.utils.result_state.DbResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface LocalDataSourceInterface {
  val getFavoriteMovies: Flow<List<FavoriteEntity>>
  val getFavoriteTv: Flow<List<FavoriteEntity>>
  val getWatchlistMovies: Flow<List<FavoriteEntity>>
  val getWatchlistTv: Flow<List<FavoriteEntity>>

  suspend fun insert(favoriteEntityList: FavoriteEntity): DbResult<Int> // use integer to save memory, i don't think it will more than 2.1 billion rows
  suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): DbResult<Int>
  suspend fun deleteAll(): DbResult<Int>
  suspend fun isFavorite(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun isWatchlist(id: Int, mediaType: String): DbResult<Boolean>
  suspend fun update(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    id: Int,
    mediaType: String
  ): DbResult<Int>

  suspend fun <T> executeDbOperation(
    operation: suspend () -> T
  ): DbResult<T> {
    return try {
      val result = withContext(Dispatchers.IO) { // Ensure operation runs on IO dispatcher
        operation()
      }
      DbResult.Success(result) // Return the result if the operation is successful
    } catch (e: SQLiteConstraintException) {
      Log.e("DatabaseError", "Operation failed due to unique constraint violation: ${e.message}")
      DbResult.Error("Unique constraint violation: ${e.message}")
    } catch (e: SQLiteFullException) {
      Log.e("DatabaseError", "Operation failed because the database is full: ${e.message}")
      DbResult.Error("Database is full: ${e.message}")
    } catch (e: SQLiteDiskIOException) {
      Log.e("DatabaseError", "Operation failed due to disk IO issue: ${e.message}")
      DbResult.Error("Disk IO issue: ${e.message}")
    } catch (e: SQLiteException) {
      Log.e("DatabaseError", "Operation failed due to SQLite exception: ${e.message}")
      DbResult.Error("SQLite exception: ${e.message}")
    } catch (e: Exception) {
      Log.e("DatabaseError", "Operation failed due to unknown error: ${e.message}")
      DbResult.Error("Unknown error: ${e.message}")
    }
  }
}