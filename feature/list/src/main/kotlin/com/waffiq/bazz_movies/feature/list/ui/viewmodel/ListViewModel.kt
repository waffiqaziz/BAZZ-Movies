package com.waffiq.bazz_movies.feature.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
  private val getListUseCase: GetListUseCase,
  private val getListMoviesUseCase: GetListMoviesUseCase,
  private val getListTvUseCase: GetListTvUseCase,
) : ViewModel() {

  fun getMovieByGenres(genres: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getMovieByGenres(genres).cachedIn(viewModelScope)

  fun getTvByGenres(genres: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getTvByGenres(genres).cachedIn(viewModelScope)

  fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getMovieByKeywords(keywords).cachedIn(viewModelScope)

  fun getTvByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getTvByKeywords(keywords).cachedIn(viewModelScope)

  fun getUpcomingMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getUpcomingMovies().cachedIn(viewModelScope)

  fun getPlayingNowMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPlayingNowMovies().cachedIn(viewModelScope)

  fun getPopularMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getPopularMovies().cachedIn(viewModelScope)

  fun getTopRatedMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getTopRatedMovies().cachedIn(viewModelScope)

  fun getPopularTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getPopularTv().cachedIn(viewModelScope)

  fun getTopRatedTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getTopRatedTv().cachedIn(viewModelScope)

  fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringThisWeekTv().cachedIn(viewModelScope)

  fun getAiringTodayTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringTodayTv().cachedIn(viewModelScope)
}
