package com.waffiq.bazz_movies.data.local.datasource

import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {
  val getFavoriteMovies: Flow<List<FavoriteEntity>>
  val getFavoriteTv: Flow<List<FavoriteEntity>>
  val getWatchlistMovies: Flow<List<FavoriteEntity>>
  val getWatchlistTv: Flow<List<FavoriteEntity>>
  suspend fun insert(favoriteEntityList: FavoriteEntity): Int
  suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): Int
  suspend fun deleteAll(): Int
  suspend fun isFavorite(id: Int, mediaType: String): Boolean
  suspend fun isWatchlist(id: Int, mediaType: String): Boolean
  suspend fun update(isFavorite: Boolean, isWatchlist: Boolean, id: Int, mediaType: String): Int

  // Define error codes
  companion object {
    const val ERROR_DUPLICATE_ENTRY = -1
    const val ERROR_UNKNOWN = -2
    const val SUCCESS = 0
  }
}