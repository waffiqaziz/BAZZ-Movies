package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.models.FavoritePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.RatePostModel
import com.waffiq.bazz_movies.core.network.data.remote.models.WatchlistPostModel
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.SearchPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostFavoriteWatchlistResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.OMDbApiService
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TMDBApiService
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
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MovieDataSourceInterface {

  // region PAGING FUNCTION
  override fun getTopRatedMovies(): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTopRatedMovies(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingThisWeek(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getTrendingToday(region: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTrendingToday(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPopularMovies(): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularMovies(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getFavoriteTv(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistTv(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getWatchlistMovies(sessionId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPopularTv(
    region: String,
    twoWeeksFromToday: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getPopularTv(page, region, twoWeeksFromToday).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getAiringThisWeekTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getAiringTodayTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getMovieRecommendations(movieId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTvRecommendations(tvId, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getUpcomingMovies(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getNowPlayingMovies(region, page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun getTopRatedTv(): Flow<PagingData<MediaResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = {
        GenericPagingSource { page ->
          tmdbApiService.getTopRatedTv(page).results
        }
      }
    ).flow.flowOn(ioDispatcher)
  }

  override fun search(query: String): Flow<PagingData<MultiSearchResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow.flowOn(ioDispatcher)
  }
  // endregion PAGING FUNCTION

  // region DETAIL
  override suspend fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getMovieCredits(movieId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getTvCredits(tvId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          omDbApiService.getOMDbDetails(imdbId)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getMovieVideo(movieId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getTvVideo(tvId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getMovieDetail(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getTvDetail(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getTvExternalIds(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getMovieState(
    sessionId: String,
    id: Int,
  ): Flow<NetworkResult<MediaStateResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getMovieState(id, sessionId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getTvState(
    sessionId: String,
    id: Int,
  ): Flow<NetworkResult<MediaStateResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getTvState(id, sessionId)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getWatchProviders(
    params: String,
    id: Int,
  ): Flow<NetworkResult<WatchProvidersResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getWatchProviders(params, id)
      }
    )
  }.flowOn(ioDispatcher)
  // endregion DETAIL

  // region PERSON
  override suspend fun getPersonDetail(id: Int): Flow<NetworkResult<DetailPersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getDetailPerson(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getPersonImage(id: Int): Flow<NetworkResult<ImagePersonResponse>> = flow {
    emit(NetworkResult.Loading)
    emit(
      safeApiCall {
        tmdbApiService.getImagePerson(id)
      }
    )
  }.flowOn(ioDispatcher)

  override suspend fun getPersonKnownFor(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    flow {
      emit(NetworkResult.Loading)
      emit(
        safeApiCall {
          tmdbApiService.getPersonCombinedCredits(id)
        }
      )
    }.flowOn(ioDispatcher)

  override suspend fun getPersonExternalID(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
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
    userId: Int,
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
    userId: Int,
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
    tvId: Int,
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
    movieId: Int,
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
