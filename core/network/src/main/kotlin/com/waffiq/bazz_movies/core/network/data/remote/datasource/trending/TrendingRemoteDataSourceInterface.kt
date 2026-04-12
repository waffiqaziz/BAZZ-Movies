package com.waffiq.bazz_movies.core.network.data.remote.datasource.trending

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import kotlinx.coroutines.flow.Flow

interface TrendingRemoteDataSourceInterface {
  fun getTrendingThisWeek(region: String): Flow<PagingData<MediaResponseItem>>
  fun getTrendingToday(region: String): Flow<PagingData<MediaResponseItem>>
}
