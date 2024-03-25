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
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiService
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.RemoteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource private constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService
) :
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

  override suspend fun getCreditMovies(movieId: Int): RemoteResponse<MovieTvCreditsResponse> {
    return try {
      RemoteResponse.Loading
      val response = tmdbApiService.getCreditMovies(movieId)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) RemoteResponse.Success(responseBody)
        else RemoteResponse.Error(Exception("Empty response body"))
      } else RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}"))
    } catch (e: Exception) {
      RemoteResponse.Error(e)
    }
  }

  override suspend fun getCreditTv(tvId: Int): RemoteResponse<MovieTvCreditsResponse> {
    return try {
      RemoteResponse.Loading
      val response = tmdbApiService.getCreditTv(tvId)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) RemoteResponse.Success(responseBody)
        else RemoteResponse.Error(Exception("Empty response body"))
      } else RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}"))
    } catch (e: Exception) {
      RemoteResponse.Error(e)
    }
  }

  override suspend fun getDetailOMDb(imdbId: String): RemoteResponse<OMDbDetailsResponse> {
    return try {
      RemoteResponse.Loading
      val response = omDbApiService.getMovieDetailOMDb(imdbId)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) RemoteResponse.Success(responseBody)
        else RemoteResponse.Error(Exception("Empty response body"))
      } else RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}"))
    } catch (e: Exception) {
      RemoteResponse.Error(e)
    }
  }

  override suspend fun getVideoMovies(movieId: Int): RemoteResponse<VideoResponse> {
    return try {
      RemoteResponse.Loading
      val response = tmdbApiService.getVideoMovies(movieId)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) RemoteResponse.Success(responseBody)
        else RemoteResponse.Error(Exception("Empty response body"))
      } else RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}"))
    } catch (e: Exception) {
      RemoteResponse.Error(e)
    }
  }

  override suspend fun getVideoTv(tvId: Int): Flow<RemoteResponse<VideoResponse>> = flow {
    emit(RemoteResponse.Loading)
    val response = tmdbApiService.getVideoTv(tvId)
    if (response.isSuccessful) {
      val responseBody = response.body()
      if (responseBody != null) emit(RemoteResponse.Success(responseBody))
      else emit(RemoteResponse.Error(Exception("Empty response body")))
    } else emit(RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}")))
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailMovie(id: Int): Flow<RemoteResponse<DetailMovieResponse>> = flow {
    emit(RemoteResponse.Loading)
    val response = tmdbApiService.getDetailMovie(id)
    if (response.isSuccessful) {
      val responseBody = response.body()
      if (responseBody != null) emit(RemoteResponse.Success(responseBody))
      else emit(RemoteResponse.Error(Exception("Empty response body")))
    } else emit(RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}")))
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailTv(id: Int): Flow<RemoteResponse<DetailTvResponse>> = flow {
    emit(RemoteResponse.Loading)
    val response = tmdbApiService.getDetailTv(id)
    if (response.isSuccessful) {
      val responseBody = response.body()
      if (responseBody != null) emit(RemoteResponse.Success(responseBody))
      else emit(RemoteResponse.Error(Exception("Empty response body")))
    } else emit(RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}")))
  }.flowOn(Dispatchers.IO)

  override suspend fun getExternalTvId(id: Int): Flow<RemoteResponse<ExternalIdResponse>> = flow {
    emit(RemoteResponse.Loading)
    val response = tmdbApiService.getExternalId(id)
    if (response.isSuccessful) {
      val responseBody = response.body()
      if (responseBody != null) emit(RemoteResponse.Success(responseBody))
      else emit(RemoteResponse.Error(Exception("Empty response body")))
    } else emit(RemoteResponse.Error(Exception("Unsuccessful response: ${response.code()}")))
  }.flowOn(Dispatchers.IO)

  companion object {
    const val TAG = "RemoteDataSource"

    @Volatile
    private var instance: RemoteDataSource? = null

    fun getInstance(
      tmdbApiService: TMDBApiService,
      omDbApiService: OMDbApiService
    ): RemoteDataSource =
      instance ?: synchronized(this) {
        instance ?: RemoteDataSource(
          tmdbApiService, omDbApiService
        )
      }
  }
}