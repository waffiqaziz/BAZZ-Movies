package com.waffiq.bazz_movies.domain.usecase.get_detail_movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface GetDetailMovieUseCase {
  suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>>
  suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<Video>>
  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>>
  suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
}