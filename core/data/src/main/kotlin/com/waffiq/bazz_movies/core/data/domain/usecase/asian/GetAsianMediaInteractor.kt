package com.waffiq.bazz_movies.core.data.domain.usecase.asian

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.domain.repository.IAsianRepository
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAsianMediaInteractor @Inject constructor(private val asianRepository: IAsianRepository) :
  GetAsianMediaUseCase {

  override fun getAnimeAllTime(): Flow<PagingData<MediaItem>> = asianRepository.getAnimeAllTime()

  override fun getAnimeThisSeason(): Flow<PagingData<MediaItem>> =
    asianRepository.getAnimeThisSeason()

  override fun getDonghua(): Flow<PagingData<MediaItem>> = asianRepository.getDonghua()

  override fun getAsianRomance(): Flow<PagingData<MediaItem>> = asianRepository.getAsianRomance()

  override fun getCostumeDrama(): Flow<PagingData<MediaItem>> = asianRepository.getCostumeDrama()
}
