package com.waffiq.bazz_movies.feature.detail.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  suspend fun getDetailOMDb(imdbId: String): Flow<Outcome<OMDbDetails>>
  suspend fun getDetailMovie(movieId: Int): Flow<Outcome<DetailMovie>>
  suspend fun getDetailTv(tvId: Int): Flow<Outcome<DetailTv>>
  suspend fun getExternalTvId(tvId: Int): Flow<Outcome<ExternalTvID>>
  suspend fun getTrailerLinkMovie(movieId: Int): Flow<Outcome<Video>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<Outcome<Video>>
  suspend fun getCreditMovies(movieId: Int): Flow<Outcome<MovieTvCredits>>
  suspend fun getCreditTv(tvId: Int): Flow<Outcome<MovieTvCredits>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
}
