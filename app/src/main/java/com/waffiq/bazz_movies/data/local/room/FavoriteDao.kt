package com.waffiq.bazz_movies.data.local.room

import androidx.paging.PagingSource
import androidx.room.*
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.utils.Constants.TABLE_NAME


@Dao
interface FavoriteDao {

  @Query("SELECT * FROM $TABLE_NAME")
  fun getFavorite(): PagingSource<Int, Favorite>

  @Query("SELECT EXISTS(SELECT * FROM $TABLE_NAME WHERE mediaId = :id)")
  fun isFavorite(id: Int): Boolean

  @Delete
  fun deleteItemFavorite(favorite: Favorite) : Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insertFavorite(favorite: Favorite)
}
