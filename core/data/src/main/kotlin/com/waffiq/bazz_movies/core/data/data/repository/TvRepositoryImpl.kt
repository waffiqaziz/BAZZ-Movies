package com.waffiq.bazz_movies.core.data.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.repository.ITvRepository
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateToday
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateTwoWeeksFromToday
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.PostResult
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.mappers.NetworkResultMapper.toOutcome
import com.waffiq.bazz_movies.core.mappers.PostMapper.toPostResult
import com.waffiq.bazz_movies.core.network.data.remote.datasource.tv.TvRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvRepositoryImpl @Inject constructor(private val tvRemoteDataSource: TvRemoteDataSource) :
  ITvRepository {

  override fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> {
    val date = getDateToday()
    return tvRemoteDataSource.getAiringTv(region, date, date)
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }
  }

  override fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    tvRemoteDataSource.getPopularTv(region, getDateTwoWeeksFromToday()).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    tvRemoteDataSource.getAiringTv(region, getDateTwoWeeksFromToday(), getDateToday())
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    tvRemoteDataSource.getTopRatedTv().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getTvState(sessionId: String, tvId: Int): Flow<Outcome<MediaState>> =
    tvRemoteDataSource.getTvState(sessionId, tvId)
      .toOutcome { it.toMediaState() }

  override fun getTvRecommendation(tvId: Int): Flow<PagingData<MediaItem>> =
    tvRemoteDataSource.getTvRecommendation(tvId).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun postTvRate(
    sessionId: String,
    rating: Float,
    tvId: Int,
  ): Flow<Outcome<PostResult>> =
    tvRemoteDataSource.postTvRate(sessionId, rating, tvId)
      .toOutcome { it.toPostResult() }
}
