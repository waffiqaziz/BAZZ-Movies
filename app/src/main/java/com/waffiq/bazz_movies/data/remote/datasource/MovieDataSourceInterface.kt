package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import kotlinx.coroutines.flow.Flow

interface MovieDataSourceInterface {

  // paging
  fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>>
  fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPopularMovies(): Flow<PagingData<ResultItem>>
  fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>>
  fun getPagingPopularTv(): Flow<PagingData<ResultItem>>
  fun getPagingOnTv(): Flow<PagingData<ResultItem>>
  fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
  fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>>
  fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>>
  fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>>
}