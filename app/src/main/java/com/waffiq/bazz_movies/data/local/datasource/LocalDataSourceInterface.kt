package com.waffiq.bazz_movies.data.local.datasource

import androidx.lifecycle.LiveData
import com.waffiq.bazz_movies.data.local.model.FavoriteDB

interface LocalDataSourceInterface {
  val getFavoriteMovies: LiveData<List<FavoriteDB>>
  val getFavoriteTv: LiveData<List<FavoriteDB>>
  val getWatchlistMovies: LiveData<List<FavoriteDB>>
  val getWatchlistTv: LiveData<List<FavoriteDB>>
  fun getSpecificFavorite(name: String): LiveData<List<FavoriteDB>>
  fun insert(favoriteDBList: FavoriteDB): Int
  fun deleteItemFromDB(mediaId: Int, mediaType: String): Int
  fun deleteAll(): Int
  fun isFavorite(id: Int, mediaType: String): Boolean
  fun isWatchlist(id: Int, mediaType: String): Boolean
  fun update(isFavorite: Boolean, isWatchlist: Boolean, id: Int, mediaType: String): Int

  // Define error codes
  companion object {
    const val ERROR_DUPLICATE_ENTRY = -1
    const val ERROR_UNKNOWN = -2
    const val SUCCESS = 0
  }
}