package com.waffiq.bazz_movies.feature.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.data.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.core.domain.MediaItem
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

  fun getByGenre(mediaType: String, id: String) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListUseCase.getMovieByGenres(id)
      } else {
        getListUseCase.getTvByGenres(id)
      }
      ).cachedIn(viewModelScope)

  fun getByKeyword(mediaType: String, id: String) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListUseCase.getMovieByKeywords(id)
      } else {
        getListUseCase.getTvByKeywords(id)
      }
      ).cachedIn(viewModelScope)

  fun getNowPlaying(mediaType: String) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListMoviesUseCase.getPlayingNowMovies()
      } else {
        getListTvUseCase.getAiringTodayTv()
      }
      ).cachedIn(viewModelScope)

  fun getPopular(mediaType: String) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListMoviesUseCase.getPopularMovies()
      } else {
        getListTvUseCase.getPopularTv()
      }
      ).cachedIn(viewModelScope)

  fun getTopRated(mediaType: String) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListMoviesUseCase.getTopRatedMovies()
      } else {
        getListTvUseCase.getTopRatedTv()
      }
      ).cachedIn(viewModelScope)

  fun getRecommendation(mediaType: String, id: Int) =
    (
      if (mediaType == MOVIE_MEDIA_TYPE) {
        getListMoviesUseCase.getMovieRecommendation(id)
      } else {
        getListTvUseCase.getTvRecommendation(id)
      }
      ).cachedIn(viewModelScope)

  fun getUpcomingMovies(): Flow<PagingData<MediaItem>> =
    getListMoviesUseCase.getUpcomingMovies().cachedIn(viewModelScope)

  fun getAiringThisWeekTv(): Flow<PagingData<MediaItem>> =
    getListTvUseCase.getAiringThisWeekTv().cachedIn(viewModelScope)
}
