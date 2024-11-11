package com.waffiq.bazz_movies.feature.detail.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.model.ResultItem
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  suspend fun getDetailOMDb(imdbId: String): Flow<NetworkResult<OMDbDetails>>
  suspend fun getDetailMovie(movieId: Int): Flow<NetworkResult<DetailMovie>>
  suspend fun getDetailTv(tvId: Int): Flow<NetworkResult<DetailTv>>
  suspend fun getExternalTvId(tvId: Int): Flow<NetworkResult<ExternalTvID>>
  suspend fun getTrailerLinkMovie(movieId: Int): Flow<NetworkResult<Video>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<NetworkResult<Video>>
  suspend fun getCreditMovies(movieId: Int): Flow<NetworkResult<MovieTvCredits>>
  suspend fun getCreditTv(tvId: Int): Flow<NetworkResult<MovieTvCredits>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
}
