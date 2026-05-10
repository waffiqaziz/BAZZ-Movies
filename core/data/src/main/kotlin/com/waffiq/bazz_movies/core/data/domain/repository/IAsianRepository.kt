package com.waffiq.bazz_movies.core.data.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface IAsianRepository {
  fun getAnimeAllTime(): Flow<PagingData<MediaItem>>
  fun getAnimeThisSeason(): Flow<PagingData<MediaItem>>
  fun getDonghua(): Flow<PagingData<MediaItem>>
  fun getAsianRomance(): Flow<PagingData<MediaItem>>
  fun getCostumeDrama(): Flow<PagingData<MediaItem>>
}
