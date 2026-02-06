package com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostMethodInteractor @Inject constructor(
  private val postMethodRepository: IMoviesRepository
) : PostMethodUseCase {
  override fun postFavorite(
    sessionId: String,
    fav: UpdateFavoriteParams,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    postMethodRepository.postFavorite(sessionId, fav, userId)

  override fun postWatchlist(
    sessionId: String,
    wtc: UpdateWatchlistParams,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    postMethodRepository.postWatchlist(sessionId, wtc, userId)

  override fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<Outcome<PostResult>> =
    postMethodRepository.postMovieRate(sessionId, rating, movieId)

  override fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<Outcome<PostResult>> =
    postMethodRepository.postTvRate(sessionId, rating, tvId)
}
