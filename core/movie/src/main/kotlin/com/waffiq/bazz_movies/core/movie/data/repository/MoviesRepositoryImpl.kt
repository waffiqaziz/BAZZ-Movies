package com.waffiq.bazz_movies.core.movie.data.repository

import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.UpdateFavoriteParams
import com.waffiq.bazz_movies.core.domain.UpdateWatchlistParams
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource,
) : IMoviesRepository {

  override fun getMovieState(
    sessionId: String,
    movieId: Int,
  ): Flow<Outcome<MediaState>> =
    movieDataSource.getMovieState(sessionId, movieId)
      .toOutcome { it.toMediaState() }

  override fun getTvState(
    sessionId: String,
    tvId: Int,
  ): Flow<Outcome<MediaState>> =
    movieDataSource.getTvState(sessionId, tvId)
      .toOutcome { it.toMediaState() }

  // region POST FAVORITE AND WATCHLIST
  override fun postFavorite(
    sessionId: String,
    fav: UpdateFavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav.toFavoriteRequest(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override fun postWatchlist(
    sessionId: String,
    wtc: UpdateWatchlistParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postWatchlist(sessionId, wtc.toWatchlistRequest(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<PostResult>> =
    movieDataSource.postMovieRate(sessionId, rating, movieId)
      .toOutcome { it.toPostResult() }

  override fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<PostResult>> =
    movieDataSource.postTvRate(sessionId, rating, tvId)
      .toOutcome { it.toPostResult() }
  // endregion POST FAVORITE AND WATCHLIST
}
