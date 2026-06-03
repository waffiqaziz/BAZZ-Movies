package com.waffiq.bazz_movies.core.database.domain.usecase

import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.models.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteLocalDatabaseUseCase {
  val favoriteMoviesFromDB: Flow<List<Favorite>>
  val watchlistMovieFromDB: Flow<List<Favorite>>
  val watchlistTvFromDB: Flow<List<Favorite>>
  val favoriteTvFromDB: Flow<List<Favorite>>

  suspend fun insertToDB(fav: Favorite): DbResult<Int>
  suspend fun deleteFromDB(mediaId: Int, mediaType: String): DbResult<Int>
  suspend fun deleteAll(): DbResult<Int>
  suspend fun update(fav: Favorite): DbResult<Unit>
  suspend fun getByMedia(mediaId: Int, mediaType: String): Favorite?
}
