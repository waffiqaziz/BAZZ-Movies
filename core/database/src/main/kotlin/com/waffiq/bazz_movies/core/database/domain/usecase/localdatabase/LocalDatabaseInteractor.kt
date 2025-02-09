package com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase

import com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDatabaseInteractor @Inject constructor(
  private val localDatabaseRepository: IDatabaseRepository
) : LocalDatabaseUseCase {

  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.favoriteMoviesFromDB

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.watchlistMovieFromDB

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.watchlistTvFromDB

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.favoriteTvFromDB

  override suspend fun insertToDB(fav: Favorite): DbResult<Int> =
    localDatabaseRepository.insertToDB(fav)

  override suspend fun deleteFromDB(fav: Favorite): DbResult<Int> =
    localDatabaseRepository.deleteFromDB(fav)

  override suspend fun deleteAll(): DbResult<Int> =
    localDatabaseRepository.deleteAll()

  override suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDatabaseRepository.isFavoriteDB(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean> =
    localDatabaseRepository.isWatchlistDB(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int> =
    localDatabaseRepository.updateFavoriteItemDB(isDelete, fav)

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Int> =
    localDatabaseRepository.updateWatchlistItemDB(isDelete, fav)
}
