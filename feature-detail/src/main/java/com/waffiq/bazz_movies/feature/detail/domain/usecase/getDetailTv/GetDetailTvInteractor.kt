package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.model.utils.GenreHelper.getTransformGenreIDs
import com.waffiq.bazz_movies.core.model.utils.GenreHelper.getTransformGenreName
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.transformLink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailTvInteractor @Inject constructor(
  private val detailRepository: IDetailRepository
) : GetDetailTvUseCase {

  override suspend fun getDetailTv(
    tvId: Int,
    userRegion: String
  ): Flow<NetworkResult<DetailMovieTvUsed>> =
    detailRepository.getDetailTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> {
          NetworkResult.Success(
            DetailMovieTvUsed(
              id = networkResult.data.id ?: 0,
              genre = getTransformGenreName(networkResult.data.listGenres), // for view
              genreId = getTransformGenreIDs(networkResult.data.listGenres),
              duration = networkResult.data.status, // for tv duration set as status
              imdbId = "",
              ageRating = getAgeRating(networkResult.data, userRegion),
              tmdbScore = getTransformTMDBScore(networkResult.data.voteAverage),
              releaseDateRegion = ReleaseDateRegion("", "")
            )
          )
        }

        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    detailRepository.getExternalTvId(tvId)

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<String>> =
    detailRepository.getTrailerLinkTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(transformLink(networkResult.data))
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    detailRepository.getCreditTv(tvId)

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<com.waffiq.bazz_movies.core.model.ResultItem>> =
    detailRepository.getPagingTvRecommendation(tvId)
}
