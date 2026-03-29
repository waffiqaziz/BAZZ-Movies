package com.waffiq.bazz_movies.feature.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getListUseCase: GetListUseCase) :
  ViewModel() {

  fun getMovieByGenres(genres: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getMovieByGenres(genres).cachedIn(viewModelScope)

  fun getTvByGenres(genres: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getTvByGenres(genres).cachedIn(viewModelScope)

  fun getMovieByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getMovieByKeywords(keywords).cachedIn(viewModelScope)

  fun getTvByKeywords(keywords: String): Flow<PagingData<MediaItem>> =
    getListUseCase.getTvByKeywords(keywords).cachedIn(viewModelScope)
}
