package com.waffiq.bazz_movies.core.movie.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.domain.WatchlistParams
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
import com.waffiq.bazz_movies.core.movie.domain.model.post.PostFavoriteWatchlist
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.utils.Helper.getDateToday
import com.waffiq.bazz_movies.core.movie.utils.Helper.getDateTwoWeeksFromToday
import com.waffiq.bazz_movies.core.movie.utils.mappers.Mapper.toPostFavoriteWatchlist
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toFavoriteRequest
import com.waffiq.bazz_movies.core.network.utils.mappers.NetworkMapper.toWatchlistRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepositoryImpl @Inject constructor(private val movieDataSource: MovieDataSource) :
  IMoviesRepository {

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getTrendingThisWeek(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTrendingToday(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getTrendingToday(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    movieDataSource.getTopRatedMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    movieDataSource.getPopularMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getUpcomingMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getPlayingNowMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> {
    val date = getDateToday()
    return movieDataSource.getAiringTv(region, date, date)
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }
  }

  override fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getPopularTv(region, getDateTwoWeeksFromToday()).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    movieDataSource.getTopRatedTv().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getMovieState(sessionId: String, movieId: Int): Flow<Outcome<MediaState>> =
    movieDataSource.getMovieState(sessionId, movieId)
      .toOutcome { it.toMediaState() }

  override fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>> =
    movieDataSource.getTvState(sessionId, tvId)
      .toOutcome { it.toMediaState() }

  override fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaItem>> =
    movieDataSource.getMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaItem>> =
    movieDataSource.getTvRecommendation(tvId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  // region POST FAVORITE AND WATCHLIST
  override fun postFavorite(
    sessionId: String,
    fav: FavoriteParams,
    userId: Int,
  ): Flow<Outcome<PostFavoriteWatchlist>> =
    movieDataSource.postFavorite(sessionId, fav.toFavoriteRequest(), userId)
      .toOutcome { it.toPostFavoriteWatchlist() }

  override fun postWatchlist(
    sessionId: String,
    wtc: WatchlistParams,
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
