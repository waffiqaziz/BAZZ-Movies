package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.utils.RemoteResponse
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

  suspend fun getDetailOMDb(imdbId: String) : RemoteResponse<OMDbDetailsResponse>
  suspend fun getCreditMovies(movieId: Int): RemoteResponse<MovieTvCreditsResponse>
  suspend fun getCreditTv(tvId: Int): RemoteResponse<MovieTvCreditsResponse>
  suspend fun getVideoMovies(movieId: Int): RemoteResponse<VideoResponse>
  suspend fun getVideoTv(tvId: Int): Flow<RemoteResponse<VideoResponse>>
  suspend fun getDetailMovie(id: Int): Flow<RemoteResponse<DetailMovieResponse>>
  suspend fun getDetailTv(id: Int): Flow<RemoteResponse<DetailTvResponse>>
  suspend fun getExternalTvId(id: Int): Flow<RemoteResponse<ExternalIdResponse>>
}