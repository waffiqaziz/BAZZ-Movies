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

  fun getAllFavorite(): LiveData<List<FavoriteDB>> = favoriteDao.getFavorite()

  fun getSpecificFavorite(name: String): LiveData<List<FavoriteDB>> = favoriteDao.getSearchFavorite(name)

  fun insertFavorite(favoriteDBList: FavoriteDB) = favoriteDao.insertFavorite(favoriteDBList)

  fun deleteItemFavorite(favoriteDB: FavoriteDB) = favoriteDao.deleteItemFavorite(favoriteDB)

  fun isFavorite(id: Int) = favoriteDao.isFavorite(id)

  fun isWatchlist(id: Int) = favoriteDao.isWatchlist(id)

  fun updateFavorite(boolean: Boolean, id: Int) = favoriteDao.updateFavorite(boolean, id)

  fun updateWatchlist(boolean: Boolean, id: Int) = favoriteDao.updateWatchlist(boolean, id)
}
