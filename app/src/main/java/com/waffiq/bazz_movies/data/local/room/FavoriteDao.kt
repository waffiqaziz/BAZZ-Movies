package com.waffiq.bazz_movies.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME
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

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_favorited = 1 and mediaType = :mediaType)")
  suspend fun isFavorite(id: Int, mediaType: String): Boolean

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_watchlist = 1 and mediaType = :mediaType)")
  suspend fun isWatchlist(id: Int, mediaType: String): Boolean

  @Query("DELETE FROM $TABLE_NAME WHERE mediaId = :mediaId and mediaType = :mediaType")
  suspend fun deleteItem(mediaId : Int, mediaType: String) : Int // delete from table

  @Query("DELETE FROM $TABLE_NAME")
  suspend fun deleteALl() : Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(favoriteEntity: FavoriteEntity)

  @Query("UPDATE $TABLE_NAME SET is_favorited = :isFavorite, is_watchlist = :isWatchlist WHERE mediaType = :mediaType and mediaId = :id")
  suspend fun update(isFavorite: Boolean, isWatchlist: Boolean, id: Int, mediaType: String): Int
}
