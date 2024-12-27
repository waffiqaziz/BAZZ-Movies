package com.waffiq.bazz_movies.core.movie.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Post
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.domain.Stated
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  // region PAGING FUNCTION
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>

  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
  // endregion PAGING FUNCTION

  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<Outcome<Stated>>

  suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<Outcome<Stated>>

  // region POST FAVORITE AND WATCHLIST
  suspend fun postFavorite(
    sessionId: String,
    fav: FavoriteModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistModel,
    userId: Int
  ): Flow<Outcome<PostFavoriteWatchlist>>

  suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<Outcome<Post>>

  suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<Outcome<Post>>
  // endregion POST FAVORITE AND WATCHLIST
}
