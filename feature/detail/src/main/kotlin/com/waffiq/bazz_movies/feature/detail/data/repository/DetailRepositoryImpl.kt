package com.waffiq.bazz_movies.feature.detail.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
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
  override suspend fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>> =
    movieDataSource.getOMDbDetails(imdbId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toOMDbDetails())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>> =
    movieDataSource.getMovieDetail(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toDetailMovie())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTvDetail(tvId: Int): Flow<Outcome<DetailTv>> =
    movieDataSource.getTvDetail(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toDetailTv())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>> =
    movieDataSource.getTvExternalIds(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toExternalTvID())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getMovieTrailerLink(movieId: Int): Flow<Outcome<Video>> =
    movieDataSource.getMovieVideo(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTvTrailerLink(tvId: Int): Flow<Outcome<Video>> =
    movieDataSource.getTvVideo(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toVideo())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    movieDataSource.getMovieCredits(movieId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMediaCredits())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override suspend fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>> =
    movieDataSource.getTvCredits(tvId).map { networkResult ->
      when (networkResult) {
        is NetworkResult.Success -> Outcome.Success(networkResult.data.toMediaCredits())
        is NetworkResult.Error -> Outcome.Error(networkResult.message)
        is NetworkResult.Loading -> Outcome.Loading
      }
    }

  override fun getMovieRecommendationPagingData(
    movieId: Int,
  ): Flow<PagingData<MediaItem>> =
    movieDataSource.getMovieRecommendation(movieId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTvRecommendationPagingData(
    tvId: Int,
  ): Flow<PagingData<MediaItem>> =
    movieDataSource.getTvRecommendation(tvId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
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
