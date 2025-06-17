package com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.ExternalTvID
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import kotlinx.coroutines.flow.Flow

interface GetDetailTvUseCase {
  suspend fun getDetailTv(tvId: Int, userRegion: String): Flow<Outcome<DetailMovieTvUsed>>
  suspend fun getExternalTvId(tvId: Int): Flow<Outcome<ExternalTvID>>
  suspend fun getTrailerLinkTv(tvId: Int): Flow<Outcome<String>>
  suspend fun getCreditTv(tvId: Int): Flow<Outcome<MovieTvCredits>>
  suspend fun getWatchProvidersTv(tvId: Int): Flow<Outcome<WatchProviders>>
  fun getPagingTvRecommendation(tvId: Int): Flow<PagingData<ResultItem>>
}
