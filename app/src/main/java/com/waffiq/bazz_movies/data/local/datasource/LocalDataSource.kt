package com.waffiq.bazz_movies.data.local.datasource

import android.database.sqlite.SQLiteConstraintException
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_DUPLICATE_ENTRY
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_UNKNOWN
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.local.room.FavoriteDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(private val favoriteDao: FavoriteDao) :
  LocalDataSourceInterface {

  override val getFavoriteMovies = favoriteDao.getFavoriteMovies()

  override val getFavoriteTv = favoriteDao.getFavoriteTv()

  override val getWatchlistMovies = favoriteDao.getWatchlistMovies()

  override val getWatchlistTv = favoriteDao.getWatchlistTv()

  override fun getSpecificFavorite(name: String): Flow<List<FavoriteDB>> =
    favoriteDao.getSearchFavorite(name)

  override suspend fun insert(favoriteDBList: FavoriteDB): Int {
    return try {
      favoriteDao.insert(favoriteDBList)
      SUCCESS
    } catch (e: SQLiteConstraintException) {
      ERROR_DUPLICATE_ENTRY
    } catch (e: Exception) {
      ERROR_UNKNOWN
    }
  }

  override suspend fun deleteItemFromDB(mediaId: Int, mediaType: String) =
    favoriteDao.deleteItem(mediaId, mediaType)

  override suspend fun deleteAll(): Int {
    return try {
      favoriteDao.deleteALl()
      SUCCESS
    } catch (e: Exception) {
      ERROR_UNKNOWN
    }
  }

  override suspend fun isFavorite(id: Int, mediaType: String) = favoriteDao.isFavorite(id, mediaType)

  override suspend fun isWatchlist(id: Int, mediaType: String) = favoriteDao.isWatchlist(id, mediaType)

  override suspend fun update(isFavorite: Boolean, isWatchlist: Boolean, id: Int, mediaType: String) =
    favoriteDao.update(isFavorite, isWatchlist, id, mediaType)

  companion object {
    private var instance: LocalDataSource? = null

    fun getInstance(favoriteDao: FavoriteDao): LocalDataSource =
      instance ?: synchronized(this) { instance ?: LocalDataSource(favoriteDao) }
  }
}
