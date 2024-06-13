package com.waffiq.bazz_movies.domain.usecase.local_database

import com.waffiq.bazz_movies.domain.model.Favorite
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow

class LocalDatabaseInteractor(
  private val localDatabaseRepository: IMoviesRepository
) : LocalDatabaseUseCase {
  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.favoriteTvFromDB

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.watchlistTvFromDB

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.watchlistTvFromDB

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    localDatabaseRepository.favoriteTvFromDB

  override suspend fun insertToDB(fav: Favorite, callback: (Int) -> Unit) =
    localDatabaseRepository.insertToDB(fav, callback)

  override suspend fun deleteFromDB(fav: Favorite) =
    localDatabaseRepository.deleteFromDB(fav)

  override suspend fun deleteAll(callback: (Int) -> Unit) =
    localDatabaseRepository.deleteAll(callback)

  override suspend fun isFavoriteDB(id: Int, mediaType: String): Boolean =
    localDatabaseRepository.isFavoriteDB(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): Boolean =
    localDatabaseRepository.isWatchlistDB(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite) =
    localDatabaseRepository.updateFavoriteItemDB(isDelete, fav)

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite) =
    localDatabaseRepository.updateWatchlistItemDB(isDelete, fav)
}