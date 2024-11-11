package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.model.utils.GenreHelper.getTransformGenreIDs
import com.waffiq.bazz_movies.core.model.utils.GenreHelper.getTransformGenreName
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.transformLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailMovieInteractor @Inject constructor(
  private val detailRepository: IDetailRepository
) : GetDetailMovieUseCase {

  override suspend fun getDetailMovie(
    movieId: Int,
    userRegion: String
  ): Flow<NetworkResult<DetailMovieTvUsed>> =
    detailRepository.getDetailMovie(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> {
          val releaseDateRegion = getReleaseDateRegion(networkResult.data, userRegion)

          NetworkResult.Success(
            DetailMovieTvUsed(
              id = networkResult.data.id ?: 0,
              genre = getTransformGenreName(networkResult.data.listGenres), // for view
              genreId = getTransformGenreIDs(networkResult.data.listGenres),
              duration = getTransformDuration(networkResult.data.runtime),
              imdbId = networkResult.data.imdbId,
              ageRating = getAgeRating(networkResult.data, releaseDateRegion.regionRelease),
              tmdbScore = getTransformTMDBScore(networkResult.data.voteAverage),
              releaseDateRegion = releaseDateRegion
            )
          )
        }

        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getLinkVideoMovies(movieId: Int): Flow<NetworkResult<String>> =
    detailRepository.getTrailerLinkMovie(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(transformLink(networkResult.data))
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    detailRepository.getCreditMovies(movieId)

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<com.waffiq.bazz_movies.core.model.ResultItem>> =
    detailRepository.getPagingMovieRecommendation(movieId)
}
