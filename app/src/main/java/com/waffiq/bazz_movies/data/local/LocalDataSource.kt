package com.waffiq.bazz_movies.data.local

import androidx.lifecycle.LiveData
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.room.FavoriteDao

class LocalDataSource private constructor(private val favoriteDao: FavoriteDao) {

  companion object {
    private var instance: LocalDataSource? = null

    fun getInstance(favoriteDao: FavoriteDao): LocalDataSource =
      instance ?: synchronized(this) {
        instance ?: LocalDataSource(favoriteDao)
      }
  }

  fun getAllFavorite(): LiveData<List<Favorite>> = favoriteDao.getFavorite()

  fun getSpecificFavorite(name: String): LiveData<List<Favorite>> = favoriteDao.getSearchFavorite(name)

  fun insertFavorite(favoriteList: Favorite) = favoriteDao.insertFavorite(favoriteList)

  fun deleteItemFavorite(favorite: Favorite) = favoriteDao.deleteItemFavorite(favorite)

  fun isFavorite(id: Int) = favoriteDao.isFavorite(id)
}