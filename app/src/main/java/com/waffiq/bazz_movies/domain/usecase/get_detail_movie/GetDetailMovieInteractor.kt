package com.waffiq.bazz_movies.domain.usecase.get_detail_movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.helpers.GenreHelper.getTransformGenreIDs
import com.waffiq.bazz_movies.utils.helpers.GenreHelper.getTransformGenreName
import com.waffiq.bazz_movies.utils.helpers.details.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.getTransformDuration
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.utils.helpers.details.DetailMovieTvHelper.transformLink
import com.waffiq.bazz_movies.utils.helpers.details.ReleaseDateHelper.getReleaseDateRegion
import com.waffiq.bazz_movies.utils.resultstate.NetworkResult
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
    getDetailMovieRepository.getTrailerLinkMovie(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(transformLink(networkResult.data))
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
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
