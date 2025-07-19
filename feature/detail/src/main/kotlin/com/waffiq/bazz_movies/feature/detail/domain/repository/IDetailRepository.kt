package com.waffiq.bazz_movies.feature.detail.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  suspend fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>>
  suspend fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>>
  suspend fun getMovieTrailerLink(movieId: Int): Flow<Outcome<Video>>
  suspend fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>>
  suspend fun getTvDetail(tvId: Int): Flow<Outcome<DetailTv>>
  suspend fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>>
  suspend fun getTvTrailerLink(tvId: Int): Flow<Outcome<Video>>
  suspend fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>>
  fun getMovieRecommendationPagingData(movieId: Int): Flow<PagingData<MediaItem>>
  fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>>
  suspend fun getWatchProviders(params: String, id: Int): Flow<Outcome<WatchProviders>>
}
