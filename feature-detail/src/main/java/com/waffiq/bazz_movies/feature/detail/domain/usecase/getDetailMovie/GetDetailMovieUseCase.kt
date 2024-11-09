package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import kotlinx.coroutines.flow.Flow

interface GetDetailMovieUseCase {
  suspend fun getDetailMovie(
    movieId: Int,
    userRegion: String
  ): Flow<NetworkResult<DetailMovieTvUsed>>

  suspend fun getLinkVideoMovies(movieId: Int): Flow<NetworkResult<String>>
  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
}
