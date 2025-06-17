package com.waffiq.bazz_movies.feature.detail.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.mappers.ResultItemMapper.toResultItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toMovieTvCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.DetailMovieTvMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import com.waffiq.bazz_movies.feature.detail.utils.mappers.OMDbMapper.toOMDbDetails
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toDetailTv
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProviders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource,
) : IDetailRepository {
  override suspend fun getDetailOMDb(imdbId: String): Flow<Outcome<OMDbDetails>> =
    movieDataSource.getDetailOMDb(imdbId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toOMDbDetails())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getDetailMovie(movieId: Int): Flow<Outcome<DetailMovie>> =
    movieDataSource.getDetailMovie(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toDetailMovie())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getDetailTv(tvId: Int): Flow<Outcome<DetailTv>> =
    movieDataSource.getDetailTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toDetailTv())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getExternalTvId(tvId: Int): Flow<Outcome<ExternalTvID>> =
    movieDataSource.getExternalTvId(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toExternalTvID())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTrailerLinkMovie(movieId: Int): Flow<Outcome<Video>> =
    movieDataSource.getVideoMovies(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTrailerLinkTv(tvId: Int): Flow<Outcome<Video>> =
    movieDataSource.getVideoTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getCreditMovies(movieId: Int): Flow<Outcome<MovieTvCredits>> =
    movieDataSource.getCreditMovies(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMovieTvCredits())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getCreditTv(tvId: Int): Flow<Outcome<MovieTvCredits>> =
    movieDataSource.getCreditTv(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMovieTvCredits())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override fun getPagingMovieRecommendation(
    movieId: Int,
  ): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override fun getPagingTvRecommendation(
    tvId: Int,
  ): Flow<PagingData<ResultItem>> =
    movieDataSource.getPagingTvRecommendation(tvId).map { pagingData ->
      pagingData.map { it.toResultItem() }
    }

  override suspend fun getWatchProviders(
    params: String,
    id: Int,
  ): Flow<Outcome<WatchProviders>> =
    movieDataSource.getWatchProviders(params, id).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toWatchProviders())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }
}
