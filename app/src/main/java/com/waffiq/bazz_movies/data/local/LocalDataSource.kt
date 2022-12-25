package com.waffiq.bazz_movies.data.local

import androidx.paging.PagingSource
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

  fun getAllFavorite(): PagingSource<Int, Favorite> = favoriteDao.getFavorite()

  fun insertFavorite(favoriteList: Favorite) = favoriteDao.insertFavorite(favoriteList)

  fun deleteItemFavorite(favorite: Favorite) = favoriteDao.deleteItemFavorite(favorite)

  fun isFavorite(id: Int) = favoriteDao.isFavorite(id)
}