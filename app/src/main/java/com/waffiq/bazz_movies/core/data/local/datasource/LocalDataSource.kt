package com.waffiq.bazz_movies.core.data.local.datasource

import com.waffiq.bazz_movies.core.data.local.model.FavoriteEntity
import com.waffiq.bazz_movies.core.data.local.room.FavoriteDao
import com.waffiq.bazz_movies.core.utils.result.DbResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
  private val favoriteDao: FavoriteDao
) :
  LocalDataSourceInterface {

  override val getFavoriteMovies: Flow<List<FavoriteEntity>> =
    favoriteDao.getFavoriteMovies().flowOn(Dispatchers.IO)

  override val getFavoriteTv: Flow<List<FavoriteEntity>> =
    favoriteDao.getFavoriteTv().flowOn(Dispatchers.IO)

  override val getWatchlistMovies: Flow<List<FavoriteEntity>> =
    favoriteDao.getWatchlistMovies().flowOn(Dispatchers.IO)

  override val getWatchlistTv: Flow<List<FavoriteEntity>> =
    favoriteDao.getWatchlistTv().flowOn(Dispatchers.IO)

  override suspend fun insert(favoriteEntityList: FavoriteEntity): DbResult<Int> =
    executeDbOperation { favoriteDao.insert(favoriteEntityList).toInt() }

  override suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): DbResult<Int> =
    executeDbOperation { favoriteDao.deleteItem(mediaId, mediaType) }

  override suspend fun deleteAll(): DbResult<Int> =
    executeDbOperation { favoriteDao.deleteALl() }

  override suspend fun isFavorite(id: Int, mediaType: String): DbResult<Boolean> =
    executeDbOperation { favoriteDao.isFavorite(id, mediaType) }

  override suspend fun isWatchlist(id: Int, mediaType: String): DbResult<Boolean> =
    executeDbOperation { favoriteDao.isWatchlist(id, mediaType) }

  override suspend fun update(
    isFavorite: Boolean,
    isWatchlist: Boolean,
    id: Int,
    mediaType: String
  ): DbResult<Int> =
    executeDbOperation { favoriteDao.update(isFavorite, isWatchlist, id, mediaType) }
}
