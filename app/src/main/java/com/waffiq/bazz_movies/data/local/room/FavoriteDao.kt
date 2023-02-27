package com.waffiq.bazz_movies.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME


@Dao
interface FavoriteDao {

  @Query("SELECT * FROM $TABLE_NAME WHERE is_favorited = 1")
  fun getFavorite(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE is_watchlist = 1")
  fun getWatchlist(): LiveData<List<FavoriteDB>>

  @Query("SELECT * FROM $TABLE_NAME WHERE title LIKE '%' || :name || '%'")
  fun getSearchFavorite(name: String): LiveData<List<FavoriteDB>>

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_favorited = 1)")
  fun isFavorite(id: Int): Boolean

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id and is_watchlist = 1)")
  fun isWatchlist(id: Int): Boolean

  @Delete
  fun deleteItemFavorite(favoriteDB: FavoriteDB) : Int // delete from table

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertFavorite(favoriteDB: FavoriteDB)

  @Query("UPDATE $TABLE_NAME SET is_favorited = :bool WHERE mediaId = :id")
  fun updateFavorite(bool: Boolean, id: Int): Int

  @Query("UPDATE $TABLE_NAME SET is_watchlist = :bool WHERE mediaId = :id")
  fun updateWatchlist(bool: Boolean, id: Int): Int
}
