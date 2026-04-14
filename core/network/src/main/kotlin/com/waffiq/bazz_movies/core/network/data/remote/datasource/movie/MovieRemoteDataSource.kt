package com.waffiq.bazz_movies.core.network.data.remote.datasource.movie

import androidx.annotation.VisibleForTesting
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.MovieKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.movie.DetailMovieResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.MovieApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(
  private val movieApiService: MovieApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MovieRemoteDataSourceInterface {

  @VisibleForTesting
  internal fun createPager(
    apiCall: suspend (Int) -> List<MediaResponseItem>,
  ): Pager<Int, MediaResponseItem> =
    Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { GenericPagingSource(apiCall) },
    )

  // region PAGING FUNCTION
  override fun getTopRatedMovies(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      movieApiService.getTopRatedMovies(page).results
    }.flow.flowOn(ioDispatcher)

  override fun getMovieRecommendation(movieId: Int): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      movieApiService.getMovieRecommendations(movieId, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      movieApiService.getUpcomingMovies(region, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      movieApiService.getNowPlayingMovies(region, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getPopularMovies(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      movieApiService.getPopularMovies(page).results
    }.flow.flowOn(ioDispatcher)
  // endregion PAGING FUNCTION

  // region DETAIL
  override fun getMovieCredits(movieId: Int): Flow<NetworkResult<MediaCreditsResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieCredits(movieId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getMovieVideo(movieId: Int): Flow<NetworkResult<VideoResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieVideo(movieId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getMovieDetail(id: Int): Flow<NetworkResult<DetailMovieResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieDetail(id) },
      ioDispatcher = ioDispatcher,
    )

  override fun getMovieState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieState(id, sessionId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getMovieWatchProviders(id: Int): Flow<NetworkResult<WatchProvidersResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieWatchProviders(id) },
      ioDispatcher = ioDispatcher,
    )

  override fun getMovieKeywords(movieId: String): Flow<NetworkResult<MovieKeywordsResponse>> =
    executeApiCall(
      apiCall = { movieApiService.getMovieKeywords(movieId) },
      ioDispatcher = ioDispatcher,
    )

  override fun postMovieRate(
    sessionId: String,
    rating: Float,
    movieId: Int,
  ): Flow<NetworkResult<PostResponse>> =
    executeApiCall(
      apiCall = { movieApiService.postMovieRate(movieId, sessionId, RatingRequest(rating)) },
      ioDispatcher = ioDispatcher,
    )
  // endregion DETAIL

  companion object {
    const val PAGE_SIZE = 10
  }
}
