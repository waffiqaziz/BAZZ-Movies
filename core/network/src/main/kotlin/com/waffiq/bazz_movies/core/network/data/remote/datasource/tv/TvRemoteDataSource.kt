package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.coroutines.IoDispatcher
import com.waffiq.bazz_movies.core.network.data.remote.models.RatingRequest
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.TvApiService
import com.waffiq.bazz_movies.core.network.utils.helpers.PageHelper.createPager
import com.waffiq.bazz_movies.core.network.utils.helpers.SafeApiCallHelper.executeApiCall
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRemoteDataSource @Inject constructor(
  private val tvApiService: TvApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TvRemoteDataSourceInterface {

  override fun getPopularTv(
    region: String,
    twoWeeksFromToday: String,
  ): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      tvApiService.getPopularTv(region, twoWeeksFromToday, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getAiringTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      tvApiService.getAiringTv(region, airDateLte, airDateGte, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      tvApiService.getTvRecommendations(tvId, page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTopRatedTv(): Flow<PagingData<MediaResponseItem>> =
    createPager { page ->
      tvApiService.getTopRatedTv(page).results
    }.flow.flowOn(ioDispatcher)

  override fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvCredits(tvId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvVideo(tvId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvDetail(id) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvExternalIds(id) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvState(id, sessionId) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvWatchProviders(id: Int): Flow<NetworkResult<WatchProvidersResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvWatchProviders(id) },
      ioDispatcher = ioDispatcher,
    )

  override fun getTvKeywords(tvId: String): Flow<NetworkResult<TvKeywordsResponse>> =
    executeApiCall(
      apiCall = { tvApiService.getTvKeywords(tvId) },
      ioDispatcher = ioDispatcher,
    )

  override fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<NetworkResult<PostResponse>> =
    executeApiCall(
      apiCall = { tvApiService.postTvRate(tvId, sessionId, RatingRequest(rating)) },
      ioDispatcher = ioDispatcher,
    )
}
