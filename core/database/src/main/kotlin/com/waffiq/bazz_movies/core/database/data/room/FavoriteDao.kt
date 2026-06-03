package com.waffiq.bazz_movies.core.database.data.room

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.Constants.FAVORITE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

  @Query("SELECT * FROM $FAVORITE_TABLE_NAME")
  suspend fun getAllFavorites(): List<FavoriteEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(favorites: List<FavoriteEntity>)

  @Transaction
  suspend fun clearAndInsert(entities: List<FavoriteEntity>) {
    deleteAll()
    entities.chunked(MAX_ROWS).forEach { chunk ->
      insertAll(chunk)
    }
  }

  @Query("SELECT * FROM $FAVORITE_TABLE_NAME WHERE is_favorited = 1 and mediaType = 'tv'")
  fun getFavoriteTv(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $FAVORITE_TABLE_NAME WHERE is_favorited = 1 and mediaType = 'movie'")
  fun getFavoriteMovies(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $FAVORITE_TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'movie'")
  fun getWatchlistMovies(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $FAVORITE_TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'tv'")
  fun getWatchlistTv(): Flow<List<FavoriteEntity>>

  @Query(
    """
        SELECT * FROM $FAVORITE_TABLE_NAME
        WHERE mediaId = :mediaId
        AND mediaType = :mediaType
        LIMIT 1
    """,
  )
  suspend fun getByMedia(mediaId: Int, mediaType: String): FavoriteEntity?

  @Transaction
  suspend fun insertOrUpdate(entity: FavoriteEntity) {
    val inserted = insert(entity)

    if (inserted == -1L) {
      val existing = getByMedia(
        mediaId = entity.mediaId,
        mediaType = entity.mediaType,
      )

      // This code is defensive code when theres race condition or db corrupt
      // Basically insert() returning -1L guarantees a conflict exists in DB,
      // so getByMedia() will always return non-null here.
      if (existing != null) {
        val updated = update(entity.copy(id = existing.id))

        // Also update() on a confirmed existing entity will always affect 1 row.
        if (updated == 0) {
          throw SQLiteException("Failed to update entity")
        }
      }
    }
  }

  @Query("DELETE FROM $FAVORITE_TABLE_NAME WHERE mediaId = :mediaId and mediaType = :mediaType")
  suspend fun deleteItem(mediaId: Int, mediaType: String): Int // delete from table

  @Query("DELETE FROM $FAVORITE_TABLE_NAME")
  suspend fun deleteAll(): Int

  // used while a conflict occurs, returns -1, indicating the insertion was ignored.
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(favoriteEntity: FavoriteEntity): Long

  @Update
  suspend fun update(entity: FavoriteEntity): Int

  companion object {
    private const val MAX_ROWS = 500
  }
}
