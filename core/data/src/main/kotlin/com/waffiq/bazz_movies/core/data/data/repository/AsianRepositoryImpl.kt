package com.waffiq.bazz_movies.core.data.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.repository.IAsianRepository
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.asian.AsianRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsianRepositoryImpl @Inject constructor(
  private val asianRemoteDataSource: AsianRemoteDataSource,
) : IAsianRepository {

  override fun getAnimeAllTime(): Flow<PagingData<MediaItem>> =
    asianRemoteDataSource.getAnimeAllTime().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getAnimeThisSeason(): Flow<PagingData<MediaItem>> =
    asianRemoteDataSource.getAnimeThisSeason().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getDonghua(): Flow<PagingData<MediaItem>> =
    asianRemoteDataSource.getDonghua().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getAsianRomance(): Flow<PagingData<MediaItem>> =
    asianRemoteDataSource.getAsianRomance().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getCostumeDrama(): Flow<PagingData<MediaItem>> =
    asianRemoteDataSource.getCostumeDrama().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }
}
