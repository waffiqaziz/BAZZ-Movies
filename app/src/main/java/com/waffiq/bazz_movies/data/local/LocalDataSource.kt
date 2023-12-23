package com.waffiq.bazz_movies.data.local

import androidx.lifecycle.LiveData
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.room.FavoriteDao

class LocalDataSource private constructor(private val favoriteDao: FavoriteDao) {

  companion object {
    private var instance: LocalDataSource? = null

    fun getInstance(favoriteDao: FavoriteDao): LocalDataSource =
      instance ?: synchronized(this) {
        instance ?: LocalDataSource(favoriteDao)
      }
  }

  val getFavoriteMovies = favoriteDao.getFavoriteMovies()

  val getFavoriteTv = favoriteDao.getFavoriteTv()

  val getWatchlistMovies = favoriteDao.getWatchlistMovies()

  val getWatchlistTv = favoriteDao.getWatchlistTv()

  fun getSpecificFavorite(name: String): LiveData<List<FavoriteDB>> = favoriteDao.getSearchFavorite(name)

  fun insertFavorite(favoriteDBList: FavoriteDB) = favoriteDao.insertFavorite(favoriteDBList)

  fun deleteItemFromDB(favoriteDB: FavoriteDB) = favoriteDao.deleteItem(favoriteDB)

  fun deleteALl() = favoriteDao.deleteALl()

  fun isFavorite(id: Int) = favoriteDao.isFavorite(id)

  fun isWatchlist(id: Int) = favoriteDao.isWatchlist(id)

  fun updateFavorite(boolean: Boolean, id: Int) = favoriteDao.updateFavorite(boolean, id)

  fun updateWatchlist(boolean: Boolean, id: Int) = favoriteDao.updateWatchlist(boolean, id)
}
