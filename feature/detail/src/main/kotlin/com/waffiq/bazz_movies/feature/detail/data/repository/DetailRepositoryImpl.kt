package com.waffiq.bazz_movies.feature.detail.data.repository

import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.network.data.remote.datasource.movie.MovieRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.omdb.OmdbRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.video.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toMediaCredits
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaDetailMapper.toVideo
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MediaKeywordsMapper.toMediaKeywords
import com.waffiq.bazz_movies.feature.detail.utils.mappers.MovieMapper.toDetailMovie
import com.waffiq.bazz_movies.feature.detail.utils.mappers.OMDbMapper.toOMDbDetails
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toExternalTvID
import com.waffiq.bazz_movies.feature.detail.utils.mappers.TvMapper.toTvDetail
import com.waffiq.bazz_movies.feature.detail.utils.mappers.WatchProvidersMapper.toWatchProviders
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieRemoteDataSource,
  private val tvRemoteDataSource: TvRemoteDataSource,
  private val omdbRemoteDataSource: OmdbRemoteDataSource,
) : IDetailRepository {

  override fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>> =
    omdbRemoteDataSource.getOMDbDetails(imdbId).toOutcome { it.toOMDbDetails() }

  override fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>> =
    movieDataSource.getMovieDetail(movieId).toOutcome { it.toDetailMovie() }

  override fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>> =
    movieDataSource.getMovieCredits(movieId).toOutcome { it.toMediaCredits() }

  override fun getMovieWatchProviders(id: Int): Flow<Outcome<WatchProviders>> =
    movieDataSource.getMovieWatchProviders(id).toOutcome { it.toWatchProviders() }

  override fun getMovieKeywords(movieId: String): Flow<Outcome<MediaKeywords>> =
    movieDataSource.getMovieKeywords(movieId).toOutcome { it.toMediaKeywords() }

  override fun getMovieTrailerLink(movieId: Int): Flow<Outcome<Video>> =
    movieDataSource.getMovieVideo(movieId).toOutcome { it.toVideo() }

  override fun getTvDetail(tvId: Int): Flow<Outcome<TvDetail>> =
    tvRemoteDataSource.getTvDetail(tvId).toOutcome { it.toTvDetail() }

  override fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>> =
    tvRemoteDataSource.getTvExternalIds(tvId).toOutcome { it.toExternalTvID() }

  override fun getTvTrailerLink(tvId: Int): Flow<Outcome<Video>> =
    tvRemoteDataSource.getTvVideo(tvId).toOutcome { it.toVideo() }

  override fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>> =
    tvRemoteDataSource.getTvCredits(tvId).toOutcome { it.toMediaCredits() }

  override fun getTvWatchProviders(id: Int): Flow<Outcome<WatchProviders>> =
    tvRemoteDataSource.getTvWatchProviders(id).toOutcome { it.toWatchProviders() }

  override fun getTvKeywords(tvId: String): Flow<Outcome<MediaKeywords>> =
    tvRemoteDataSource.getTvKeywords(tvId).toOutcome { it.toMediaKeywords() }
}
