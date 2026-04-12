package com.waffiq.bazz_movies.core.data.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import kotlinx.coroutines.flow.Flow

interface ITvRepository {
  fun getPopularTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaItem>>
  fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaItem>>
  fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>>
  fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<PostResult>>
}
