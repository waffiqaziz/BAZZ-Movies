package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.helpers.AgeRatingHelper.getAgeRating
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformDuration
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.getTransformTMDBScore
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.toLink
import com.waffiq.bazz_movies.feature.detail.utils.helpers.ReleaseDateHelper.getReleaseDateRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDetailMovieInteractor @Inject constructor(
  private val detailRepository: IDetailRepository,
) : GetDetailMovieUseCase {

  override suspend fun getDetailMovie(
    movieId: Int,
    userRegion: String,
  ): Flow<Outcome<DetailMovieTvUsed>> =
    detailRepository.getDetailMovie(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> {
          val releaseDateRegion = getReleaseDateRegion(outcome.data, userRegion)

          Outcome.Success(
            DetailMovieTvUsed(
              id = outcome.data.id ?: 0,
              genre = transformListGenreToJoinString(outcome.data.listGenres), // for view
              genreId = transformToGenreIDs(outcome.data.listGenres),
              duration = getTransformDuration(outcome.data.runtime),
              imdbId = outcome.data.imdbId,
              ageRating = getAgeRating(outcome.data, releaseDateRegion.regionRelease),
              tmdbScore = getTransformTMDBScore(outcome.data.voteAverage),
              releaseDateRegion = releaseDateRegion
            )
          )
        }

        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getLinkVideoMovies(movieId: Int): Flow<Outcome<String>> =
    detailRepository.getTrailerLinkMovie(movieId).map { outcome ->
      when (outcome) {
        is Outcome.Success -> Outcome.Success(outcome.data.toLink())
        is Outcome.Error -> Outcome.Error(outcome.message)
        is Outcome.Loading -> Outcome.Loading
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<Outcome<MovieTvCredits>> =
    detailRepository.getCreditMovies(movieId)

  override suspend fun getWatchProvidersMovies(movieId: Int): Flow<Outcome<WatchProviders>> =
    detailRepository.getWatchProviders("movie", movieId)

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    detailRepository.getPagingMovieRecommendation(movieId)
}
