package com.waffiq.bazz_movies.core.movie.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {

  // region LIST
  fun getTrendingToday(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedMovies(): Flow<PagingData<MediaItem>>
  fun getPopularMovies(): Flow<PagingData<MediaItem>>
  fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>>
  fun getPopularTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>>
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaItem>>
  // endregion LIST

  fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>>

  fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>

  // region POST FAVORITE AND WATCHLIST
  fun postFavorite(
    sessionId: String,
    fav: FavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postWatchlist(
    sessionId: String,
    wtc: WatchlistParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>>

  fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<Outcome<PostResult>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<PostResult>>
  // endregion POST FAVORITE AND WATCHLIST
}
