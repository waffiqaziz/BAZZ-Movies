package com.waffiq.bazz_movies.core.movie.data.repository

import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPost
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoritePostModel
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistPostModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource,
) : IMoviesRepository {

  override suspend fun getMovieState(
    sessionId: String,
    movieId: Int,
  ): Flow<Outcome<MediaState>> =
    movieDataSource.getMovieState(sessionId, movieId)
      .toOutcome { it.toMediaState() }

  override suspend fun getTvState(
    sessionId: String,
    tvId: Int,
  ): Flow<Outcome<MediaState>> =
    movieDataSource.getTvState(sessionId, tvId)
      .toOutcome { it.toMediaState() }

  // region POST FAVORITE AND WATCHLIST
  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav.toFavoritePostModel(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postWatchlist(sessionId, wtc.toWatchlistPostModel(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<Post>> =
    movieDataSource.postMovieRate(sessionId, rating, movieId)
      .toOutcome { it.toPost() }

  override suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<Post>> =
    movieDataSource.postTvRate(sessionId, rating, tvId)
      .toOutcome { it.toPost() }
  // endregion POST FAVORITE AND WATCHLIST
}
