package com.waffiq.bazz_movies.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME

@Dao
interface FavoriteDao {

  @Query("SELECT * FROM $TABLE_NAME WHERE is_favorited = 1 and mediaType = 'tv'")
  fun getFavoriteTv(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_favorited = 1 and mediaType = 'movie'")
  fun getFavoriteMovies(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'movie'")
  fun getWatchlistMovies(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_watchlist = 1 and mediaType = 'tv'")
  fun getWatchlistTv(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE title LIKE '%' || :name || '%'")
  fun getSearchFavorite(name: String): LiveData<List<FavoriteDB>>

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_favorited = 1 and mediaType = :mediaType)")
  fun isFavorite(id: Int, mediaType: String): Boolean

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_watchlist = 1 and mediaType = :mediaType)")
  fun isWatchlist(id: Int, mediaType: String): Boolean

  @Query("DELETE FROM $TABLE_NAME WHERE mediaId = :mediaId and mediaType = :mediaType")
  fun deleteItem(mediaId : Int, mediaType: String) : Int // delete from table

  @Query("DELETE FROM $TABLE_NAME")
  fun deleteALl() : Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(favoriteDB: FavoriteDB)

  @Query("UPDATE $TABLE_NAME SET is_favorited = :isFavorite, is_watchlist = :isWatchlist WHERE mediaType = :mediaType and mediaId = :id")
  fun update(isFavorite: Boolean, isWatchlist: Boolean, id: Int, mediaType: String): Int
}
