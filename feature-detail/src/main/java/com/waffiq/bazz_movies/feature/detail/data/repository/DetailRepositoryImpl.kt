package com.waffiq.bazz_movies.feature.detail.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.utils.mappers.UniversalMapper.toResultItem
import com.waffiq.bazz_movies.core.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toDetailMovie
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toDetailTv
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toMovieTvCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toOMDbDetails
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toVideo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IDetailRepository {
  override suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>> =
    movieDataSource.getDetailOMDb(imdbId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toOMDbDetails())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>> =
    movieDataSource.getDetailMovie(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toDetailMovie())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>> =
    movieDataSource.getDetailTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toDetailTv())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>> =
    movieDataSource.getExternalTvId(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toExternalTvID())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getTrailerLinkMovie(movieId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoMovies(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<Video>> =
    movieDataSource.getVideoTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditMovies(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toMovieTvCredits())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>> =
    movieDataSource.getCreditTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> NetworkResult.Success(networkResult.data.toMovieTvCredits())
        is NetworkResult.Error -> NetworkResult.Error(networkResult.message)
        is NetworkResult.Loading -> NetworkResult.Loading
      }
    }

  override fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTvRecommendation(tvId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }
}
