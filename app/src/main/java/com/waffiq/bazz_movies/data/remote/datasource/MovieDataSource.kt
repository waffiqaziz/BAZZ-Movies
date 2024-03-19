package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.paging.AiringTodayTvPagingSource
import com.waffiq.bazz_movies.data.paging.FavoriteMoviePagingSource
import com.waffiq.bazz_movies.data.paging.FavoriteTvPagingSource
import com.waffiq.bazz_movies.data.paging.MultiTrendingDayPagingSource
import com.waffiq.bazz_movies.data.paging.MultiTrendingWeekPagingSource
import com.waffiq.bazz_movies.data.paging.OnTvPagingSource
import com.waffiq.bazz_movies.data.paging.PlayingNowMoviesPagingSource
import com.waffiq.bazz_movies.data.paging.PopularMoviePagingSource
import com.waffiq.bazz_movies.data.paging.PopularTvPagingSource
import com.waffiq.bazz_movies.data.paging.RecommendationMoviePagingSource
import com.waffiq.bazz_movies.data.paging.RecommendationTvPagingSource
import com.waffiq.bazz_movies.data.paging.SearchPagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedMoviePagingSource
import com.waffiq.bazz_movies.data.paging.TopRatedTvPagingSource
import com.waffiq.bazz_movies.data.paging.UpcomingMoviesPagingSource
import com.waffiq.bazz_movies.data.paging.WatchlistMoviePagingSource
import com.waffiq.bazz_movies.data.paging.WatchlistTvPagingSource
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import kotlinx.coroutines.flow.Flow

class MovieDataSource private constructor(private val tmdbApiService: TMDBApiService) :
  MovieDataSourceInterface {

  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { TopRatedMoviePagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { MultiTrendingWeekPagingSource(region, tmdbApiService) }
    ).flow
  }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { MultiTrendingDayPagingSource(region, tmdbApiService) }
    ).flow
  }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PopularMoviePagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { FavoriteMoviePagingSource(sessionId, tmdbApiService) }
    ).flow
  }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { FavoriteTvPagingSource(sessionId, tmdbApiService) }
    ).flow
  }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { WatchlistTvPagingSource(sessionId, tmdbApiService) }
    ).flow
  }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { WatchlistMoviePagingSource(sessionId, tmdbApiService) }
    ).flow
  }

  override fun getPagingPopularTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PopularTvPagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingOnTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { OnTvPagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { AiringTodayTvPagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { RecommendationMoviePagingSource(movieId, tmdbApiService) }
    ).flow
  }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { RecommendationTvPagingSource(tvId, tmdbApiService) }
    ).flow
  }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { UpcomingMoviesPagingSource(region, tmdbApiService) }
    ).flow
  }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PlayingNowMoviesPagingSource(region, tmdbApiService) }
    ).flow
  }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { TopRatedTvPagingSource(tmdbApiService) }
    ).flow
  }

  override fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow
  }

  companion object {
    const val TAG = "MovieDataSource"

    @Volatile
    private var instance: MovieDataSource? = null

    fun getInstance(service: TMDBApiService): MovieDataSource =
      instance ?: synchronized(this) {
        instance ?: MovieDataSource(service)
      }
  }
}