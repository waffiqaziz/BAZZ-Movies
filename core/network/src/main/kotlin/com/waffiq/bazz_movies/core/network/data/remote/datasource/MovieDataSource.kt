package com.waffiq.bazz_movies.core.network.data.remote.datasource

import androidx.annotation.VisibleForTesting
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
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDataSource @Inject constructor(
  private val tmdbApiService: TMDBApiService,
  private val omDbApiService: OMDbApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MovieDataSourceInterface {

  @VisibleForTesting
  internal fun createPager(
    apiCall: suspend (Int) -> List<MediaResponseItem>,
  ): Pager<Int, MediaResponseItem> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { GenericPagingSource(apiCall) }
    )
  }

  // region PAGING FUNCTION
  override fun getTopRatedMovies(): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTopRatedMovies(page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTrendingThisWeek(region, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getTrendingToday(region: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTrendingToday(region, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getPopularMovies(): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getPopularMovies(page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getFavoriteMovies(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getFavoriteMovies(sessionId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getFavoriteTv(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getFavoriteTv(sessionId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getWatchlistTv(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getWatchlistTv(sessionId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getWatchlistMovies(sessionId: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getWatchlistMovies(sessionId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getPopularTv(
    region: String,
    twoWeeksFromToday: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getPopularTv(page, region, twoWeeksFromToday).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getAiringThisWeekTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getAiringTodayTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTvAiring(region, airDateLte, airDateGte, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getMovieRecommendations(movieId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTvRecommendations(tvId, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getUpcomingMovies(region, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getNowPlayingMovies(region, page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun getTopRatedTv(): Flow<PagingData<MediaResponseItem>> {
    return createPager { page ->
      tmdbApiService.getTopRatedTv(page).results
    }.flow.flowOn(ioDispatcher)
  }

  override fun search(query: String): Flow<PagingData<MultiSearchResponseItem>> {
    return Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { SearchPagingSource(tmdbApiService, query) }
    ).flow.flowOn(ioDispatcher)
  }
  // endregion PAGING FUNCTION

  // region DETAIL
  override fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getMovieCredits(movieId) },
      ioDispatcher = ioDispatcher
    )

  override fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getTvCredits(tvId) },
      ioDispatcher = ioDispatcher
    )

  override fun getOMDbDetails(imdbId: String): Flow<NetworkResult<OMDbDetailsResponse>> =
    executeApiCall(
      apiCall = { omDbApiService.getOMDbDetails(imdbId) },
      ioDispatcher = ioDispatcher
    )

  override fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getMovieVideo(movieId) },
      ioDispatcher = ioDispatcher
    )

  override fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>> = executeApiCall(
    apiCall = { tmdbApiService.getTvVideo(tvId) },
    ioDispatcher = ioDispatcher
  )

  override fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getMovieDetail(id) },
      ioDispatcher = ioDispatcher
    )

  override fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>> = executeApiCall(
    apiCall = { tmdbApiService.getTvDetail(id) },
    ioDispatcher = ioDispatcher
  )

  override fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getTvExternalIds(id) },
      ioDispatcher = ioDispatcher
    )

  override fun getMovieState(
    sessionId: String,
    id: Int,
  ): Flow<NetworkResult<MediaStateResponse>> = executeApiCall(
    apiCall = { tmdbApiService.getMovieState(id, sessionId) },
    ioDispatcher = ioDispatcher
  )

  override fun getTvState(
    sessionId: String,
    id: Int,
  ): Flow<NetworkResult<MediaStateResponse>> = executeApiCall(
    apiCall = { tmdbApiService.getTvState(id, sessionId) },
    ioDispatcher = ioDispatcher
  )

  override fun getWatchProviders(
    params: String,
    id: Int,
  ): Flow<NetworkResult<WatchProvidersResponse>> = executeApiCall(
    apiCall = { tmdbApiService.getWatchProviders(params, id) },
    ioDispatcher = ioDispatcher
  )
  // endregion DETAIL

  // region PERSON
  override fun getPersonDetail(id: Int): Flow<NetworkResult<DetailPersonResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getDetailPerson(id) },
      ioDispatcher = ioDispatcher
    )

  override fun getPersonImage(id: Int): Flow<NetworkResult<ImagePersonResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getImagePerson(id) },
      ioDispatcher = ioDispatcher
    )

  override fun getPersonKnownFor(id: Int): Flow<NetworkResult<CombinedCreditResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getPersonCombinedCredits(id) },
      ioDispatcher = ioDispatcher
    )

  override fun getPersonExternalID(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>> =
    executeApiCall(
      apiCall = { tmdbApiService.getExternalIdPerson(id) },
      ioDispatcher = ioDispatcher
    )

  override fun postFavorite(
    sessionId: String,
    fav: FavoritePostModel,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> = executeApiCall(
    apiCall = { tmdbApiService.postFavoriteTMDB(userId, sessionId, fav) },
    ioDispatcher = ioDispatcher
  )

  override fun postWatchlist(
    sessionId: String,
    wtc: WatchlistPostModel,
    userId: Int,
  ): Flow<NetworkResult<PostFavoriteWatchlistResponse>> = executeApiCall(
    apiCall = { tmdbApiService.postWatchlistTMDB(userId, sessionId, wtc) },
    ioDispatcher = ioDispatcher
  )

  override fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<NetworkResult<PostResponse>> = executeApiCall(
    apiCall = { tmdbApiService.postTvRate(tvId, sessionId, RatePostModel(rating)) },
    ioDispatcher = ioDispatcher
  )

  override fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<NetworkResult<PostResponse>> = executeApiCall(
    apiCall = { tmdbApiService.postMovieRate(movieId, sessionId, RatePostModel(rating)) },
    ioDispatcher = ioDispatcher
  )
  // endregion PERSON

  companion object {
    const val PAGE_SIZE = 10
  }
}
