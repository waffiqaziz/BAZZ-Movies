package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetListTvUseCase {
  fun getPopularTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>>
  fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaItem>>
}
