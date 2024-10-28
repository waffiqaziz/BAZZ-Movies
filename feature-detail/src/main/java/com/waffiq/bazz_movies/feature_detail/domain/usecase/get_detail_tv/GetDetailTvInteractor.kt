package com.waffiq.bazz_movies.feature_detail.domain.usecase.get_detail_tv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.core.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.core.domain.model.detail.ReleaseDateRegion
import com.waffiq.bazz_movies.core.domain.model.detail.tv.ExternalTvID
import com.waffiq.bazz_movies.core.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.utils.helpers.GenreHelper.getTransformGenreIDs
import com.waffiq.bazz_movies.core.utils.helpers.GenreHelper.getTransformGenreName
import com.waffiq.bazz_movies.core.utils.helpers.details.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.core.utils.helpers.details.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.core.utils.helpers.details.DetailMovieTvHelper.transformLink
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailTvInteractor @Inject constructor(
  private val getDetailTvRepository: IMoviesRepository
) : GetDetailTvUseCase {

  override suspend fun getDetailTv(
    tvId: Int,
    userRegion: String
  ): Flow<NetworkResult<DetailMovieTvUsed>> =
    getDetailTvRepository.getDetailTv(tvId).map { networkResult ->
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
    getDetailTvRepository.getExternalTvId(tvId)

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<String>> =
    getDetailTvRepository.getTrailerLinkTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(transformLink(networkResult.data))
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    getDetailTvRepository.getCreditTv(tvId)

  override suspend fun getStatedTv(sessionId: String, tvId: Int): Flow<NetworkResult<Stated>> =
    getDetailTvRepository.getStatedTv(sessionId, tvId)

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    getDetailTvRepository.getPagingTvRecommendation(tvId)
}
