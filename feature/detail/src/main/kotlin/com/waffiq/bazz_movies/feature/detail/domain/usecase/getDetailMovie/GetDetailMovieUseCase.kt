package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import kotlinx.coroutines.flow.Flow

interface GetDetailMovieUseCase {
  suspend fun getDetailMovie(
    movieId: Int,
    userRegion: String,
  ): Flow<Outcome<DetailMovieTvUsed>>

  suspend fun getLinkVideoMovies(movieId: Int): Flow<Outcome<String>>
  suspend fun getCreditMovies(movieId: Int): Flow<Outcome<MovieTvCredits>>
  suspend fun getWatchProvidersMovies(movieId: Int): Flow<Outcome<WatchProviders>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
}
