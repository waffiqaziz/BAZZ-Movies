package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.ResultItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.StatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.detail_movie_tv.video_media.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.TMDBApiService
import com.waffiq.bazz_movies.core.network.di.IoDispatcher
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSource @Inject constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieDataSourceInterface {

  // region PAGING FUNCTION
  override fun getPagingTopRatedMovies(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTopRatedMovies(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingWeek(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingDay(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularMovies(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteTv(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistTv(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingPopularTv(
    region: String,
    twoWeeksFromToday: String
  ): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularTv(page, region, twoWeeksFromToday).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingAiringThisWeekTv(
    region: String,
    airDateLte: String,
    airDateGte: String
  ): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingAiringTodayTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getRecommendedMovie(movieId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getRecommendedTv(tvId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getUpcomingMovies(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPlayingNowMovies(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTopRatedTv(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPagingSearch(query: String): Flow<PagingData<ResultsItemSearchResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow.flowOn(ioDispatcher)
  }
  // endregion PAGING FUNCTION

  // region DETAIL
  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getCreditMovies(movieId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getCreditTv(tvId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          omDbApiService.getMovieDetailOMDb(imdbId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getVideoMovies(movieId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getVideoTv(tvId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovieResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailMovie(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getDetailTv(id: Int): Flow<NetworkResult<DetailTvResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailTv(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getExternalTvId(id: Int): Flow<NetworkResult<ExternalIdResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getExternalId(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getStatedMovie(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getStatedMovie(id, sessionId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getStatedTv(
    sessionId: String,
    id: Int
  ): Flow<NetworkResult<StatedResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getStatedTv(id, sessionId)
      }
    )
  }.flowOn(ioDispatcher)
  // endregion DETAIL

  // region PERSON
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailPerson(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getImagePerson(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getKnownForPersonCombinedMovieTv(id)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getExternalIdPerson(id)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.postFavoriteTMDB(userId, sessionId, fav)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int
  ): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating))
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int
  ): Flow<NetworkResult<PostResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating))
        }
      )
    }.flowOn(ioDispatcher)
  // endregion PERSON

  companion object {
    const val PAGE_SIZE = 10
  }
}
