package com.waffiq.bazz_movies.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.di.Injection
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieViewModel
import com.waffiq.bazz_movies.ui.activity.home.HomeViewModel
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModel
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteViewModel
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistViewModel
import com.waffiq.bazz_movies.ui.activity.search.SearchViewModel

class ViewModelFactory(
  private val moviesRepository: MoviesRepository
) :  ViewModelProvider.NewInstanceFactory() {

  companion object {
    @Volatile
    private var instance: ViewModelFactory? = null

    fun getInstance(context: Context): ViewModelFactory =
      instance
        ?: synchronized(this) {
          instance
            ?: ViewModelFactory(
              Injection.provideMovieRepository(
                context
              )
            )
        }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
        HomeViewModel(moviesRepository) as T
      }
      modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
        SearchViewModel(moviesRepository) as T
      }
      modelClass.isAssignableFrom(DetailMovieViewModel::class.java) -> {
        DetailMovieViewModel(moviesRepository) as T
      }
      modelClass.isAssignableFrom(MyFavoriteViewModel::class.java) -> {
        MyFavoriteViewModel(moviesRepository) as T
      }
      modelClass.isAssignableFrom(MyWatchlistViewModel::class.java) -> {
        MyWatchlistViewModel(moviesRepository) as T
      }
      modelClass.isAssignableFrom(MoreViewModel::class.java) -> {
        MoreViewModel(moviesRepository) as T
      }
      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}