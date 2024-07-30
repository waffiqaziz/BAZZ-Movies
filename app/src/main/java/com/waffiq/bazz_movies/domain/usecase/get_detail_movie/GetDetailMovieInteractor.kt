package com.waffiq.bazz_movies.domain.usecase.get_detail_movie

import androidx.paging.PagingData
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.MovieTvCredits
import com.waffiq.bazz_movies.domain.model.detail.Video
import com.waffiq.bazz_movies.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetDetailMovieInteractor(
  private val getDetailMovieRepository: IMoviesRepository
): GetDetailMovieUseCase {
  override suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>> =
    getDetailMovieRepository.getDetailMovie(movieId)

  override suspend fun getVideoMovies(movieId: Int): Flow<NetworkResult<Video>> =
    getDetailMovieRepository.getVideoMovies(movieId)

  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    getDetailMovieRepository.getCreditMovies(movieId)

  override suspend fun getStatedMovie(sessionId: String, movieId: Int): Flow<NetworkResult<Stated>> =
    getDetailMovieRepository.getStatedMovie(sessionId, movieId)

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    getDetailMovieRepository.getPagingMovieRecommendation(movieId)
}