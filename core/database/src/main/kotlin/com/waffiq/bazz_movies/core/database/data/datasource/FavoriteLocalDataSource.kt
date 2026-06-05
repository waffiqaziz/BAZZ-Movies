package com.waffiq.bazz_movies.core.database.data.datasource

import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.database.data.model.FavoriteEntity
import com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
import com.waffiq.bazz_movies.core.database.utils.DbResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteLocalDataSource @Inject constructor(
  private val favoriteDao: FavoriteDao,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FavoriteLocalDataSourceInterface {

  override val getFavoriteMovies: Flow<List<FavoriteEntity>> =
    favoriteDao.getFavorites(MOVIE_MEDIA_TYPE).flowOn(ioDispatcher)

  override val getFavoriteTv: Flow<List<FavoriteEntity>> =
    favoriteDao.getFavorites(TV_MEDIA_TYPE).flowOn(ioDispatcher)

  override val getWatchlistMovies: Flow<List<FavoriteEntity>> =
    favoriteDao.getWatchlist(MOVIE_MEDIA_TYPE).flowOn(ioDispatcher)

  override val getWatchlistTv: Flow<List<FavoriteEntity>> =
    favoriteDao.getWatchlist(TV_MEDIA_TYPE).flowOn(ioDispatcher)

  override suspend fun insert(favoriteEntityList: FavoriteEntity): DbResult<Int> =
    executeDbOperation { favoriteDao.insert(favoriteEntityList).toInt() }

  override suspend fun deleteItemFromDB(mediaId: Int, mediaType: String): DbResult<Int> =
    executeDbOperation { favoriteDao.deleteItem(mediaId, mediaType) }

  override suspend fun deleteAll(): DbResult<Int> = executeDbOperation { favoriteDao.deleteAll() }

  override suspend fun update(favoriteEntity: FavoriteEntity): DbResult<Unit> =
    executeDbOperation { favoriteDao.insertOrUpdate(favoriteEntity) }

  override suspend fun getByMedia(mediaId: Int, mediaType: String): FavoriteEntity? =
    favoriteDao.getByMedia(mediaId, mediaType)
}
