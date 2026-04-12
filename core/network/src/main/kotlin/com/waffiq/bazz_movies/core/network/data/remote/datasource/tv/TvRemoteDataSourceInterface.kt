package com.waffiq.bazz_movies.core.network.data.remote.datasource.tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCreditsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.keywords.TvKeywordsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.DetailTvResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv.ExternalIdResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.videomedia.VideoResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.watchproviders.WatchProvidersResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.post.PostResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
interface TvRemoteDataSourceInterface {
  fun getPopularTv(region: String, twoWeeksFromToday: String): Flow<PagingData<MediaResponseItem>>

  fun getAiringTv(
    region: String,
    airDateLte: String,
    airDateGte: String,
  ): Flow<PagingData<MediaResponseItem>>

  fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaResponseItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaResponseItem>>
  fun getTvCredits(tvId: Int): Flow<NetworkResult<MediaCreditsResponse>>
  fun getTvDetail(id: Int): Flow<NetworkResult<DetailTvResponse>>
  fun getTvVideo(tvId: Int): Flow<NetworkResult<VideoResponse>>
  fun getTvState(sessionId: String, id: Int): Flow<NetworkResult<MediaStateResponse>>
  fun getTvExternalIds(id: Int): Flow<NetworkResult<ExternalIdResponse>>
  fun getTvWatchProviders(id: Int): Flow<NetworkResult<WatchProvidersResponse>>
  fun getTvKeywords(tvId: String): Flow<NetworkResult<TvKeywordsResponse>>

  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<NetworkResult<PostResponse>>
}
