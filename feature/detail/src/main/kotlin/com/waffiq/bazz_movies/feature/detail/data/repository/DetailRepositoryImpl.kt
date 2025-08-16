package com.waffiq.bazz_movies.feature.detail.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
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
    movieDataSource.getOMDbDetails(imdbId).toOutcome { it.toOMDbDetails() }

  override suspend fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>> =
    movieDataSource.getMovieDetail(movieId).toOutcome { it.toDetailMovie() }

  override suspend fun getTvDetail(tvId: Int): Flow<Outcome<TvDetail>> =
    movieDataSource.getTvDetail(tvId).toOutcome { it.toDetailTv() }

  override suspend fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>> =
    movieDataSource.getTvExternalIds(tvId).toOutcome { it.toExternalTvID() }

  override suspend fun getMovieTrailerLink(movieId: Int): Flow<Outcome<Video>> =
    movieDataSource.getMovieVideo(movieId).toOutcome { it.toVideo() }

  override suspend fun getTvTrailerLink(tvId: Int): Flow<Outcome<Video>> =
    movieDataSource.getTvVideo(tvId).toOutcome { it.toVideo() }

  override suspend fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    movieDataSource.getMovieCredits(movieId).toOutcome { it.toMediaCredits() }

  override suspend fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>> =
    movieDataSource.getTvCredits(tvId).toOutcome { it.toMediaCredits() }

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
    movieDataSource.getWatchProviders(params, id).toOutcome { it.toWatchProviders() }
}
