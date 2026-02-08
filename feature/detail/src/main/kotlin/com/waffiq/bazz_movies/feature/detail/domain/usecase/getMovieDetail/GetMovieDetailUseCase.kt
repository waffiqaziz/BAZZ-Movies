package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import kotlinx.coroutines.flow.Flow

interface GetMovieDetailUseCase {
  fun getMovieDetail(movieId: Int, userRegion: String): Flow<Outcome<MediaDetail>>
  fun getMovieVideoLinks(movieId: Int): Flow<Outcome<String>>
  fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>>
  fun getMovieWatchProviders(countryCode: String, movieId: Int): Flow<Outcome<WatchProvidersItem>>

  fun getMovieRecommendationPagingData(movieId: Int): Flow<PagingData<MediaItem>>
}
