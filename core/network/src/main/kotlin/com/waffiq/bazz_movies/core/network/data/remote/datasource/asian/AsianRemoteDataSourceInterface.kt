package com.waffiq.bazz_movies.core.network.data.remote.datasource.asian

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import kotlinx.coroutines.flow.Flow

interface AsianRemoteDataSourceInterface {
  fun getAnimeAllTime(): Flow<PagingData<MediaResponseItem>>
  fun getAnimeThisSeason(): Flow<PagingData<MediaResponseItem>>
  fun getDonghua(): Flow<PagingData<MediaResponseItem>>
  fun getAsianRomance(): Flow<PagingData<MediaResponseItem>>
  fun getCostumeDrama(): Flow<PagingData<MediaResponseItem>>
}
