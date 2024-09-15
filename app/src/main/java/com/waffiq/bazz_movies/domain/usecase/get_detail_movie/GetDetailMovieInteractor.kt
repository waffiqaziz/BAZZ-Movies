package com.waffiq.bazz_movies.domain.usecase.get_detail_movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
import com.waffiq.bazz_movies.utils.resultstate.Status
import com.waffiq.bazz_movies.utils.helpers.GenreHelper.getTransformGenreIDs
import com.waffiq.bazz_movies.utils.helpers.GenreHelper.getTransformGenreName
import com.waffiq.bazz_movies.utils.helpers.details.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.getTransformDuration
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.transformLink
import com.waffiq.bazz_movies.utils.helpers.details.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDetailMovieInteractor(
  private val getDetailMovieRepository: IMoviesRepository
) : GetDetailMovieUseCase {

  override suspend fun getDetailMovie(
    movieId: Int,
    userRegion: String
  ): Flow<NetworkResult<DetailMovieTvUsed>> =
    getDetailMovieRepository.getDetailMovie(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> {
          val releaseDateRegion = getReleaseDateRegion(networkResult.data, userRegion)

          NetworkResult.success(
            DetailMovieTvUsed(
              id = networkResult.data?.id ?: 0,
              genre = getTransformGenreName(networkResult.data?.listGenres),
              genreId = getTransformGenreIDs(networkResult.data?.listGenres),
              duration = getTransformDuration(networkResult.data?.runtime),
              imdbId = networkResult.data?.imdbId,
              ageRating = getAgeRating(networkResult.data, releaseDateRegion.regionRelease),
              tmdbScore = getTransformTMDBScore(networkResult.data?.voteAverage),
              releaseDateRegion = releaseDateRegion
            )
          )
        }

        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getLinkVideoMovies(movieId: Int): Flow<NetworkResult<String>> =
    getDetailMovieRepository.getTrailerLinkMovie(movieId).map { networkResult ->
      when (networkResult.status) {
        Status.SUCCESS -> {
          // Extract and transform the data
          NetworkResult.success(
            networkResult.data?.let { transformLink(it) } ?: ""
          )
        }

        Status.ERROR -> NetworkResult.error(networkResult.message ?: "Unknown error")
        Status.LOADING -> NetworkResult.loading()
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    getDetailMovieRepository.getCreditMovies(movieId)

  override suspend fun getStatedMovie(
    sessionId: String,
    movieId: Int
  ): Flow<NetworkResult<Stated>> =
    getDetailMovieRepository.getStatedMovie(sessionId, movieId)

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    getDetailMovieRepository.getPagingMovieRecommendation(movieId)
}
