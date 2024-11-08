package com.waffiq.bazz_movies.feature.detail.domain.repository

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.movie.DetailMovie
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.Video
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.DetailTv
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import kotlinx.coroutines.flow.Flow

interface IDetailRepository {
  suspend fun getDetailOMDb(imdbId: String): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<OMDbDetails>>
  suspend fun getDetailMovie(movieId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<DetailMovie>>
  suspend fun getDetailTv(tvId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<DetailTv>>
  suspend fun getExternalTvId(tvId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<ExternalTvID>>
  suspend fun getTrailerLinkMovie(movieId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<Video>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<Video>>
  suspend fun getCreditMovies(movieId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<MovieTvCredits>>
  suspend fun getCreditTv(tvId: Int): Flow<com.waffiq.bazz_movies.core.network.utils.result.NetworkResult<MovieTvCredits>>
  fun getPagingMovieRecommendation(movieId: Int): Flow<PagingData<ResultItem>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
}
