package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.paging_sources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.paging_sources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.post_body.WatchlistPostModel
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
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.safeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSource @Inject constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService
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
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingWeek(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingWeek(region, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTrendingDay(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingDay(region, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularMovies(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularMovies(page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingFavoriteTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteTv(sessionId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistTv(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistTv(sessionId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingWatchlistMovies(sessionId: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPopularTv(twoWeeksFromToday: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularTv(page, twoWeeksFromToday).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingOnTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvOnTheAir(page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingAiringTodayTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvAiringToday(page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getRecommendedMovie(movieId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getRecommendedTv(tvId, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingUpcomingMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getUpcomingMovies(region, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingPlayingNowMovies(region: String): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPlayingNowMovies(region, page).results
        }
      }
    ).flow.flowOn(Dispatchers.IO)
  }

  override fun getPagingTopRatedTv(): Flow<PagingData<ResultItemResponse>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTopRatedTv(page).results
        }
      }
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
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getCreditMovies(movieId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCreditsResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getCreditTv(tvId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          omDbApiService.getMovieDetailOMDb(imdbId)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getVideoMovies(movieId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getVideoTv(tvId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getVideoTv(tvId)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailMovie(id: Int): Flow<NetworkResult<DetailMovieResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailMovie(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getDetailTv(id: Int): Flow<NetworkResult<DetailTvResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailTv(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getExternalTvId(id: Int): Flow<NetworkResult<ExternalIdResponse>> = flow {
    emit(NetworkResult.Loading)
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
    emit(NetworkResult.Loading)
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
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getStatedTv(id, sessionId)
      }
    )
  }.flowOn(Dispatchers.IO)
  // endregion DETAIL

  // region PERSON
  override suspend fun getDetailPerson(id: Int): Flow<NetworkResult<DetailPersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailPerson(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getImagePerson(id: Int): Flow<NetworkResult<ImagePersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getImagePerson(id)
      }
    )
  }.flowOn(Dispatchers.IO)

  override suspend fun getKnownForPerson(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getKnownForPersonCombinedMovieTv(id)
        }
      )
    }.flowOn(Dispatchers.IO)

  override suspend fun getExternalIDPerson(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
    flow {
      emit(NetworkResult.Loading)
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
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
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
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
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
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
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
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.postMovieRate(movieId, sessionId, data)
        }
      )
    }.flowOn(Dispatchers.IO)
  // endregion PERSON

  companion object {
    const val PAGE_SIZE = 5
  }
}
