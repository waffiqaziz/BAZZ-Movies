package com.waffiq.bazz_movies.domain.usecase.get_detail_movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.domain.model.detail.GenresItem
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDateRegion
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItem
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.Helper
import com.waffiq.bazz_movies.utils.Helper.convertRuntime
import com.waffiq.bazz_movies.utils.NetworkResult
import com.waffiq.bazz_movies.utils.Status
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
            networkResult.data?.let { Helper.transformLink(it) } ?: ""
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

  private fun getAgeRating(
    data: DetailMovie?,
    userRegion: String,
  ): String {

    // get age rating based on region
    val certification = getTransformAgeRating(data, userRegion)

    // if certification return empty, get age rating from US as default
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRating(data, "US")
  }

  private fun getTransformAgeRating(data: DetailMovie?, region: String): String {
    return data?.releaseDates?.listReleaseDatesItem
      ?.find { it?.iso31661 == region }
      ?.listReleaseDatesitemValue
      ?.find { it.certification?.isNotEmpty() == true }
      ?.certification ?: ""
  }

  private fun getReleaseDateRegion(data: DetailMovie?, userRegion: String): ReleaseDateRegion {
    /* match between release date and region
       Step 1. Check if release date and region is matching
       Step 2. If matching return as should be, if not get release date based region US
    */
    val isMatch = matchRegionAndReleaseDate(data, userRegion)

    return if (isMatch) {
      ReleaseDateRegion(
        regionRelease = userRegion,
        releaseDate = Helper.dateFormatterISO8601(
          getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion)
        )
      )
    } else {
      ReleaseDateRegion(
        regionRelease = "US",
        releaseDate = Helper.dateFormatterISO8601(
          getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, "US")
        )
      )
    }
  }

  private fun matchRegionAndReleaseDate(data: DetailMovie?, userRegion: String): Boolean {
    val releaseDate =
      getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion).isNotEmpty()
    val region =
      getTransformRegion(data?.releaseDates?.listReleaseDatesItem, userRegion).isNotEmpty()

    return releaseDate && region
  }

  private fun getTransformReleaseDate(data: List<ReleaseDatesItem?>?, region: String): String =
    data?.find { it?.iso31661 == region }
      ?.listReleaseDatesitemValue
      ?.firstOrNull()
      ?.releaseDate
      ?: ""

  private fun getTransformRegion(data: List<ReleaseDatesItem?>?, region: String): String =
    data?.find { it?.iso31661 == region }?.iso31661 ?: ""

  private fun getTransformGenreName(list: List<GenresItem>?): String? =
    list?.map { it.name }?.joinToString(separator = ", ")

  private fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  private fun getTransformGenreIDs(list: List<GenresItem>?): List<Int>? =
    list?.map { it.id ?: 0 }

  private fun getTransformDuration(runtime: Int?): String? =
    runtime?.let { convertRuntime(it) }
}