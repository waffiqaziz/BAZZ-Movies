package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
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
import com.waffiq.bazz_movies.data.remote.response.tmdb.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostRateResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.PostResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultsItemSearch
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.VideoResponse
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiService
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException

class RemoteDataSource private constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService
) : MovieDataSourceInterface {

  // region PAGING FUNCTION
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { TopRatedMoviePagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { MultiTrendingWeekPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { MultiTrendingDayPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PopularMoviePagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { FavoriteMoviePagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { FavoriteTvPagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { WatchlistTvPagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { WatchlistMoviePagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PopularTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingOnTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { OnTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { AiringTodayTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { RecommendationMoviePagingSource(movieId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { RecommendationTvPagingSource(tvId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { UpcomingMoviesPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { PlayingNowMoviesPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItem>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { TopRatedTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearch>> {
    return Pager(
      config = PagingConfig(pageSize = 5),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow.flowOn(Dispatchers.IO)
  }
  // endregion PAGING FUNCTION

  // region DETAIL
  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.getCreditMovies(movieId)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getCreditTv(tvId)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        omDbApiService.getMovieDetailOMDb(imdbId)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getVideoMovies(movieId)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getVideoTv(tvId)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovieResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getDetailMovie(id)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailTv(id: Int): Flow<NetworkResult<DetailTvResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getDetailTv(id)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getExternalTvId(id: Int): Flow<NetworkResult<ExternalIdResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getExternalId(id)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getStatedMovie(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getStatedMovie(id, sessionId)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getStatedTv(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getStatedTv(id, sessionId)
    })
  }.flowOn(Dispatchers.IO)
  // endregion DETAIL

  // region PERSON
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPersonResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getDetailPerson(id)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePersonResponse>> = flow {
    emit(NetworkResult.loading())
    emit(safeApiCall {
      tmdbApiService.getImagePerson(id)
    })
  }.flowOn(Dispatchers.IO)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.getKnownForPersonCombinedMovieTv(id)
      })
    }.flowOn(Dispatchers.IO)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(safeApiCall {
        tmdbApiService.getExternalIdPerson(id)
      })
    }.flowOn(Dispatchers.IO)

  override suspend  fun postFavorite(
    sessionId: String,
    fav: Favorite,
    userId: Int
  ): NetworkResult<PostResponse> {
    return try {
      val response = tmdbApiService.postFavoriteTMDB(userId, sessionId, fav)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.success(responseBody)
        } else {
          NetworkResult.error("Error in fetching data")
        }
      } else {
        val errorMessage = response.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error("Something went wrong")
    }
  }

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: Watchlist,
    userId: Int
  ): NetworkResult<PostResponse> {
    return try {
      val response = tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.success(responseBody)
        } else {
          NetworkResult.error("Error in fetching data")
        }
      } else {
        val errorMessage = response.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error("Something went wrong")
    }
  }

  override suspend fun postTvRate(
    sessionId: String,
    data: Rate,
    tvId: Int
  ): NetworkResult<PostRateResponse> {
    return try {
      val response = tmdbApiService.postTvRate(tvId, sessionId, data)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.success(responseBody)
        } else {
          NetworkResult.error("Error in fetching data")
        }
      } else {
        val errorMessage = response.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error("Something went wrong")
    }
  }

  override suspend fun postMovieRate(
    sessionId: String,
    data: Rate,
    movieId: Int
  ): NetworkResult<PostRateResponse> {
    return try {
      val response = tmdbApiService.postMovieRate(movieId, sessionId, data)
      if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
          NetworkResult.success(responseBody)
        } else {
          NetworkResult.error("Error in fetching data")
        }
      } else {
        val errorMessage = response.message() ?: "Unknown error"
        NetworkResult.error(errorMessage)
      }
    } catch (e: IOException) {
      NetworkResult.error("Please check your network connection")
    } catch (e: Exception) {
      NetworkResult.error("Something went wrong")
    }
  }


  // endregion PERSON

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