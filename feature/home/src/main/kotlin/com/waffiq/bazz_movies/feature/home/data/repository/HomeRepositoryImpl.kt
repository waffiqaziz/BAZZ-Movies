package com.waffiq.bazz_movies.feature.home.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.mappers.MediaItemMapper.toMediaItem
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getDateToday
import com.waffiq.bazz_movies.feature.home.utils.helpers.Helper.getDateTwoWeeksFromToday
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
  private val movieDataSource: MovieDataSource
) : IHomeRepository {

  override fun getTrendingThisWeek(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getTrendingThisWeek(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTrendingToday(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getTrendingToday(region).map { pagingData ->
      pagingData.map { it.toMediaItem() }
    }

  override fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    movieDataSource.getTopRatedMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    movieDataSource.getPopularMovies().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getUpcomingMovies(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getUpcomingMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getPlayingNowMovies(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getPlayingNowMovies(region).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = MOVIE_MEDIA_TYPE) }
    }

  override fun getAiringTodayTv(region: String): Flow<PagingData<MediaItem>> {
    val date = getDateToday()
    return movieDataSource.getAiringTodayTv(region, date, date)
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }
  }

  override fun getPopularTv(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getPopularTv(region, getDateTwoWeeksFromToday()).map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }

  override fun getAiringThisWeekTv(region: String): Flow<PagingData<MediaItem>> =
    movieDataSource.getAiringThisWeekTv(region, getDateTwoWeeksFromToday(), getDateToday())
      .map { pagingData ->
        pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
      }

  override fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    movieDataSource.getTopRatedTv().map { pagingData ->
      pagingData.map { it.toMediaItem().copy(mediaType = TV_MEDIA_TYPE) }
    }
}
