package com.waffiq.bazz_movies.data.local.datasource

import android.database.sqlite.SQLiteConstraintException
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_DUPLICATE_ENTRY
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.ERROR_UNKNOWN
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSourceInterface.Companion.SUCCESS
import com.waffiq.bazz_movies.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.data.local.room.FavoriteDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(private val favoriteDao: FavoriteDao) :
  LocalDataSourceInterface {

  override val getFavoriteMovies: Flow<List<FavoriteEntity>> = favoriteDao.getFavoriteMovies()

  override val getFavoriteTv: Flow<List<FavoriteEntity>> = favoriteDao.getFavoriteTv()

  override val getWatchlistMovies: Flow<List<FavoriteEntity>> = favoriteDao.getWatchlistMovies()

  override val getWatchlistTv: Flow<List<FavoriteEntity>> = favoriteDao.getWatchlistTv()

  override suspend fun insert(favoriteEntityList: FavoriteEntity): Int {
    return try {
      favoriteDao.insert(favoriteEntityList)
      SUCCESS
    } catch (e: SQLiteConstraintException) {
      ERROR_DUPLICATE_ENTRY
    } catch (e: Exception) {
      ERROR_UNKNOWN
    }
  }

  override suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): Int =
    favoriteDao.deleteItem(mediaId, mediaType)

  override suspend fun deleteAll(): Int {
    return try {
      favoriteDao.deleteALl()
      SUCCESS
    } catch (e: Exception) {
      ERROR_UNKNOWN
    }
  }

  override suspend fun isFavorite(id: Int, mediaType: String): Boolean =
    favoriteDao.isFavorite(id, mediaType)

  override suspend fun isWatchlist(id: Int, mediaType: String): Boolean =
    favoriteDao.isWatchlist(id, mediaType)

  override suspend fun update(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    id: Int,
    mediaType: String
  ): Int = favoriteDao.update(isFavorite, isWatchlist, id, mediaType)

  companion object {
    private var instance: LocalDataSource? = null

    fun getInstance(favoriteDao: FavoriteDao): LocalDataSource =
      instance ?: synchronized(this) { instance ?: LocalDataSource(favoriteDao) }
  }
}
