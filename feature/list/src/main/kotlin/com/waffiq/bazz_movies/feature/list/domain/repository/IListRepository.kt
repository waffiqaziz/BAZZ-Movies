package com.waffiq.bazz_movies.feature.list.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import kotlinx.coroutines.flow.Flow

interface IListRepository {
  fun getMovieByGenres(genres: String, region: String): Flow<PagingData<MediaItem>>
  fun getTvByGenres(genres: String, region: String): Flow<PagingData<MediaItem>>
  fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaItem>>
  fun getTvByKeywords(keywords: String): Flow<PagingData<MediaItem>>
}
