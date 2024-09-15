package com.waffiq.bazz_movies.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.di.modules.Injection
import com.waffiq.bazz_movies.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_omdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_person.GetDetailPersonUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_tv.GetDetailTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_movies.GetListMoviesUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.domain.usecase.multi_search.MultiSearchUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieViewModel
import com.waffiq.bazz_movies.ui.activity.home.MovieViewModel
import com.waffiq.bazz_movies.ui.activity.home.TvSeriesViewModel
import com.waffiq.bazz_movies.ui.activity.more.MoreViewModelLocal
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteViewModel
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistViewModel
import com.waffiq.bazz_movies.ui.activity.person.PersonMovieViewModel
import com.waffiq.bazz_movies.ui.activity.search.SearchViewModel

class ViewModelFactory(
  private val getListMoviesUseCase: GetListMoviesUseCase,
  private val getListTvUseCase: GetListTvUseCase,
  private val getDetailMovieUseCase: GetDetailMovieUseCase,
  private val getDetailTvUseCase: GetDetailTvUseCase,
  private val localDatabaseUseCase: LocalDatabaseUseCase,
  private val postMethodUseCase: PostMethodUseCase,
  private val getDetailPersonUseCase: GetDetailPersonUseCase,
  private val getDetailOMDbUseCase: GetDetailOMDbUseCase,
  private val getStatedMovieUseCase: GetStatedMovieUseCase,
  private val getStatedTvUseCase: GetStatedTvUseCase,
  private val getFavoriteMovieUseCase: GetFavoriteMovieUseCase,
  private val getFavoriteTvUseCase: GetFavoriteTvUseCase,
  private val getWatchlistMovieUseCase: GetWatchlistMovieUseCase,
  private val getWatchlistTvUseCase: GetWatchlistTvUseCase,
  private val getMultiSearchUseCase: MultiSearchUseCase
) : ViewModelProvider.NewInstanceFactory() {

  companion object {
    @Volatile
    private var instance: ViewModelFactory? = null

    fun getInstance(context: Context): ViewModelFactory =
      instance ?: synchronized(this) {
        instance ?: ViewModelFactory(
          Injection.provideGetListMoviesUseCase(context),
          Injection.provideGetListTvUseCase(context),
          Injection.provideGetDetailMovieUseCase(context),
          Injection.provideGetDetailTvUseCase(context),
          Injection.provideLocalDatabaseUseCase(context),
          Injection.providePostMethodUseCase(context),
          Injection.provideGetDetailPersonUseCase(context),
          Injection.provideGetDetailOMDbUseCase(context),
          Injection.provideGetStatedMovieUseCase(context),
          Injection.provideGetStatedTvUseCase(context),
          Injection.provideGetFavoriteMovieUseCase(context),
          Injection.provideGetFavoriteTvUseCase(context),
          Injection.provideGetWatchlistMovieUseCase(context),
          Injection.provideGetWatchlistTvUseCase(context),
          Injection.provideGetMultiSearchUseCase(context)
        )
      }
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
        MovieViewModel(getListMoviesUseCase) as T
      }

      modelClass.isAssignableFrom(TvSeriesViewModel::class.java) -> {
        TvSeriesViewModel(getListTvUseCase) as T
      }

      modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
        SearchViewModel(getMultiSearchUseCase) as T
      }

      modelClass.isAssignableFrom(DetailMovieViewModel::class.java) -> {
        DetailMovieViewModel(
          getDetailMovieUseCase,
          getDetailTvUseCase,
          localDatabaseUseCase,
          postMethodUseCase,
          getDetailOMDbUseCase,
          getStatedMovieUseCase,
          getStatedTvUseCase
        ) as T
      }

      modelClass.isAssignableFrom(MyFavoriteViewModel::class.java) -> {
        MyFavoriteViewModel(
          getFavoriteMovieUseCase,
          getFavoriteTvUseCase,
          postMethodUseCase,
          localDatabaseUseCase,
          getStatedMovieUseCase,
          getStatedTvUseCase,
        ) as T
      }

      modelClass.isAssignableFrom(MyWatchlistViewModel::class.java) -> {
        MyWatchlistViewModel(
          getWatchlistMovieUseCase,
          getWatchlistTvUseCase,
          postMethodUseCase,
          localDatabaseUseCase,
          getStatedMovieUseCase,
          getStatedTvUseCase
        ) as T
      }

      modelClass.isAssignableFrom(MoreViewModelLocal::class.java) -> {
        MoreViewModelLocal(localDatabaseUseCase) as T
      }

      modelClass.isAssignableFrom(PersonMovieViewModel::class.java) -> {
        PersonMovieViewModel(getDetailPersonUseCase) as T
      }

      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}
