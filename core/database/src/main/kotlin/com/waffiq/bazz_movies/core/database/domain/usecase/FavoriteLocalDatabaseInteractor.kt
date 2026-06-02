package com.waffiq.bazz_movies.core.database.domain.usecase

import com.waffiq.bazz_movies.core.database.domain.repository.IFavoriteLocalDatabaseRepository
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.models.Favorite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteLocalDatabaseInteractor @Inject constructor(
  private val favoriteLocalDatabaseRepository: IFavoriteLocalDatabaseRepository,
) : FavoriteLocalDatabaseUseCase {

  override val favoriteMoviesFromDB: Flow<List<Favorite>> =
    favoriteLocalDatabaseRepository.favoriteMoviesFromDB

  override val watchlistMovieFromDB: Flow<List<Favorite>> =
    favoriteLocalDatabaseRepository.watchlistMovieFromDB

  override val watchlistTvFromDB: Flow<List<Favorite>> =
    favoriteLocalDatabaseRepository.watchlistTvFromDB

  override val favoriteTvFromDB: Flow<List<Favorite>> =
    favoriteLocalDatabaseRepository.favoriteTvFromDB

  override suspend fun insertToDB(fav: Favorite): DbResult<Int> =
    favoriteLocalDatabaseRepository.insertToDB(fav)

  override suspend fun deleteFromDB(fav: Favorite): DbResult<Int> =
    favoriteLocalDatabaseRepository.deleteFromDB(fav)

  override suspend fun deleteAll(): DbResult<Int> = favoriteLocalDatabaseRepository.deleteAll()

  override suspend fun isFavoriteDB(id: Int, mediaType: String): DbResult<Boolean> =
    favoriteLocalDatabaseRepository.isFavoriteDB(id, mediaType)

  override suspend fun isWatchlistDB(id: Int, mediaType: String): DbResult<Boolean> =
    favoriteLocalDatabaseRepository.isWatchlistDB(id, mediaType)

  override suspend fun updateFavoriteItemDB(isDelete: Boolean, fav: Favorite): DbResult<Unit> =
    favoriteLocalDatabaseRepository.updateFavoriteItemDB(isDelete, fav)

  override suspend fun updateWatchlistItemDB(isDelete: Boolean, fav: Favorite): DbResult<Unit> =
    favoriteLocalDatabaseRepository.updateWatchlistItemDB(isDelete, fav)
}
