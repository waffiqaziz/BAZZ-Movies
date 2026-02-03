package com.waffiq.bazz_movies.feature.detail.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.MovieDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  fun getOMDbDetails(imdbId: String): Flow<Outcome<OMDbDetails>>
  fun getMovieDetail(movieId: Int): Flow<Outcome<MovieDetail>>
  fun getMovieTrailerLink(movieId: Int): Flow<Outcome<Video>>
  fun getMovieCredits(movieId: Int): Flow<Outcome<MediaCredits>>
  fun getTvDetail(tvId: Int): Flow<Outcome<TvDetail>>
  fun getTvExternalIds(tvId: Int): Flow<Outcome<TvExternalIds>>
  fun getTvTrailerLink(tvId: Int): Flow<Outcome<Video>>
  fun getTvCredits(tvId: Int): Flow<Outcome<MediaCredits>>
  fun getMovieRecommendationPagingData(movieId: Int): Flow<PagingData<MediaItem>>
  fun getTvRecommendationPagingData(tvId: Int): Flow<PagingData<MediaItem>>
  fun getWatchProviders(params: String, id: Int): Flow<Outcome<WatchProviders>>
  fun getMovieKeywords(movieId: String): Flow<Outcome<MediaKeywords>>
  fun getTvKeywords(tvId: String): Flow<Outcome<MediaKeywords>>
}
