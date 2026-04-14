package com.waffiq.bazz_movies.core.data.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface ITrendingRepository {
  fun getTrendingToday(region: String): Flow<PagingData<MediaItem>>
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>>
}
