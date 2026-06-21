package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.utils.mappers.BasicMediaDetailMapper.toMediaDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetMediaDetailInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
  private val userRepository: IUserRepository,
) : GetMediaDetailUseCase {

  private fun getRegion() = userRepository.getUserRegionPref().take(1)

  override fun getMovieDetailWithUserRegion(movieId: Int): Flow<Outcome<MediaDetail>> =
    getRegion().flatMapConcat { region ->
      detailRepository.getMovieDetail(movieId).map { outcome ->
        when (outcome) {
          is Outcome.Success -> Outcome.Success(
            outcome.data.toMediaDetail(getReleaseDateRegion(outcome.data, region)),
          )

          is Outcome.Error -> Outcome.Error(outcome.message)

          is Outcome.Loading -> Outcome.Loading
        }
      }
    }

  override fun getTvDetailWithUserRegion(tvId: Int): Flow<Outcome<MediaDetail>> =
    getRegion().flatMapConcat { region ->
      detailRepository.getTvDetail(tvId).map { outcome ->
        when (outcome) {
          is Outcome.Success -> Outcome.Success(
            outcome.data.toMediaDetail(userRegion = region),
          )

          is Outcome.Error -> Outcome.Error(outcome.message)

          is Outcome.Loading -> Outcome.Loading
        }
      }
    }
}
