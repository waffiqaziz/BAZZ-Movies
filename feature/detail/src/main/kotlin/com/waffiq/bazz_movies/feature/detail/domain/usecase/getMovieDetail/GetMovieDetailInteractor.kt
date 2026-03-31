package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.toLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetMovieDetailUseCase {

  override fun getMovieVideoLinks(movieId: Int): Flow<Outcome<String>> =
    detailRepository.getMovieTrailerLink(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    detailRepository.getMovieCredits(movieId)

  override fun getMovieRecommendationPagingData(movieId: Int): Flow<PagingData<MediaItem>> =
    detailRepository.getMovieRecommendationPagingData(movieId)
}
