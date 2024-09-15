package com.waffiq.bazz_movies.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.data.paging_sources.AiringTodayTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.FavoriteMoviePagingSource
import com.waffiq.bazz_movies.data.paging_sources.FavoriteTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.MultiTrendingDayPagingSource
import com.waffiq.bazz_movies.data.paging_sources.MultiTrendingWeekPagingSource
import com.waffiq.bazz_movies.data.paging_sources.OnTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.PlayingNowMoviesPagingSource
import com.waffiq.bazz_movies.data.paging_sources.PopularMoviePagingSource
import com.waffiq.bazz_movies.data.paging_sources.PopularTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.RecommendationMoviePagingSource
import com.waffiq.bazz_movies.data.paging_sources.RecommendationTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.SearchPagingSource
import com.waffiq.bazz_movies.data.paging_sources.TopRatedMoviePagingSource
import com.waffiq.bazz_movies.data.paging_sources.TopRatedTvPagingSource
import com.waffiq.bazz_movies.data.paging_sources.UpcomingMoviesPagingSource
import com.waffiq.bazz_movies.data.paging_sources.WatchlistMoviePagingSource
import com.waffiq.bazz_movies.data.paging_sources.WatchlistTvPagingSource
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.DetailTvResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiService
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieDataSource private constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService
) : MovieDataSourceInterface {

  // region PAGING FUNCTION
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { TopRatedMoviePagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { MultiTrendingWeekPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { MultiTrendingDayPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { PopularMoviePagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { FavoriteMoviePagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { FavoriteTvPagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { WatchlistTvPagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { WatchlistMoviePagingSource(sessionId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularTv(twoWeeksFromToday: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { PopularTvPagingSource(twoWeeksFromToday, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingOnTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { OnTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { AiringTodayTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { RecommendationMoviePagingSource(movieId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { RecommendationTvPagingSource(tvId, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { UpcomingMoviesPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { PlayingNowMoviesPagingSource(region, tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { TopRatedTvPagingSource(tmdbApiService) }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearchResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow.flowOn(Dispatchers.IO)
  }
  // endregion PAGING FUNCTION

  // region DETAIL
  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCall {
          tmdbApiService.getCreditMovies(movieId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getCreditTv(tvId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCall {
          omDbApiService.getMovieDetailOMDb(imdbId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getVideoMovies(movieId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getVideoTv(tvId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovieResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getDetailMovie(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailTv(id: Int): Flow<NetworkResult<DetailTvResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getDetailTv(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getExternalTvId(id: Int): Flow<NetworkResult<ExternalIdResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getExternalId(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getStatedMovie(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getStatedMovie(id, sessionId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getStatedTv(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getStatedTv(id, sessionId)
      }
    )
  }.flowOn(Dispatchers.IO)
  // endregion DETAIL

  // region PERSON
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPersonResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getDetailPerson(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePersonResponse>> = flow {
    emit(NetworkResult.loading())
    emit(
      safeApiCall {
        tmdbApiService.getImagePerson(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCall {
          tmdbApiService.getKnownForPersonCombinedMovieTv(id)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCall {
          tmdbApiService.getExternalIdPerson(id)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCallPost {
          tmdbApiService.postFavoriteTMDB(userId, sessionId, fav)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCallPost {
          tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun postTvRate(
    sessionId: String,
    data: RatePostModel,
    tvId: Int
  ): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCallPost {
          tmdbApiService.postTvRate(tvId, sessionId, data)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun postMovieRate(
    sessionId: String,
    data: RatePostModel,
    movieId: Int
  ): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.loading())
      emit(
        safeApiCallPost {
          tmdbApiService.postMovieRate(movieId, sessionId, data)
        }
      )
    }.flowOn(Dispatchers.IO)
  // endregion PERSON

  companion object {
    const val TAG = "MovieDataSource"
    const val PAGE_SIZE = 5

    @Volatile
    private var instance: MovieDataSource? = null

    @JvmStatic
    fun getInstance(
      tmdbApiService: TMDBApiService,
      omDbApiService: OMDbApiService
    ): MovieDataSource = instance ?: synchronized(this) {
      instance ?: MovieDataSource(
        tmdbApiService, omDbApiService
      )
    }
  }
}
