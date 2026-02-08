package com.waffiq.bazz_movies.core.database.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.utils.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

  @Query("SELECT * FROM $TABLE_NAME WHERE is_favorited = 1 and mediaType = 'tv'")
  fun getFavoriteTv(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_favorited = 1 and mediaType = 'movie'")
  fun getFavoriteMovies(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'movie'")
  fun getWatchlistMovies(): Flow<List<FavoriteEntity>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'tv'")
  fun getWatchlistTv(): Flow<List<FavoriteEntity>>

  @Query(
    """
        SELECT EXISTS(
        SELECT *
        FROM $TABLE_NAME
        WHERE mediaId = :id
        AND is_favorited = 1
        AND mediaType = :mediaType)
    """,
  )
  suspend fun isFavorite(id: Int, mediaType: String): Boolean

  @Query(
    """
        SELECT EXISTS(
        SELECT *
        FROM $TABLE_NAME
        WHERE mediaId = :id
        AND is_watchlist = 1
        AND mediaType = :mediaType)
    """,
  )
  suspend fun isWatchlist(id: Int, mediaType: String): Boolean

  @Query("DELETE FROM $TABLE_NAME WHERE mediaId = :mediaId and mediaType = :mediaType")
  suspend fun deleteItem(mediaId: Int, mediaType: String): Int // delete from table

  @Query("DELETE FROM $TABLE_NAME")
  suspend fun deleteALl(): Int

  // used while a conflict occurs, returns -1, indicating the insertion was ignored.
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(favoriteEntity: FavoriteEntity): Long

  @Query(
    """
        UPDATE $TABLE_NAME
        SET is_favorited = :isFavorite, is_watchlist = :isWatchlist
        WHERE mediaType = :mediaType
        AND mediaId = :id
    """,
  )
  suspend fun update(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    id: Int,
    mediaType: String,
  ): Int
}
