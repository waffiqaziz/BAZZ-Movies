package com.waffiq.bazz_movies.core.movie.domain.usecase.listtv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface GetListTvUseCase {
  fun getPopularTv(): Flow<PagingData<MediaItem>>
  fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>>
  fun getAiringTodayTv(): Flow<PagingData<MediaItem>>
  fun getTopRatedTv(): Flow<PagingData<MediaItem>>
}
