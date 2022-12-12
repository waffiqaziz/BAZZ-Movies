package com.waffiq.bazz_movies.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.di.Injection
import com.waffiq.bazz_movies.ui.activity.home.HomeViewModel
import com.waffiq.bazz_movies.ui.activity.search.SearchViewModel

class ViewModelFactory(
  private val context: Context
) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(ListTopRatedMoviesViewModel::class.java) -> {
        ListTopRatedMoviesViewModel(Injection.provideMovieRepository(context)) as T
      } modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
        HomeViewModel(Injection.provideMovieRepository(context)) as T
      } modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
        SearchViewModel(Injection.provideMovieRepository(context)) as T
      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}