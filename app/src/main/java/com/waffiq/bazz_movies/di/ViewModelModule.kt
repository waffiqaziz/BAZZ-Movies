package com.waffiq.bazz_movies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieViewModel
import com.waffiq.bazz_movies.ui.activity.home.MovieViewModel
import com.waffiq.bazz_movies.ui.activity.home.TvSeriesViewModel
import com.waffiq.bazz_movies.ui.activity.more.MoreLocalViewModel
import com.waffiq.bazz_movies.ui.activity.more.MoreUserViewModel
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteViewModel
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistViewModel
import com.waffiq.bazz_movies.ui.activity.person.PersonMovieViewModel
import com.waffiq.bazz_movies.ui.activity.search.SearchViewModel
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.RegionViewModel
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(DetailMovieViewModel::class)
  abstract fun bindDetailMovieViewModel(viewModel: DetailMovieViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MovieViewModel::class)
  abstract fun bindMovieViewModel(viewModel: MovieViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(TvSeriesViewModel::class)
  abstract fun bindTvSeriesViewModel(viewModel: TvSeriesViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MoreUserViewModel::class)
  abstract fun bindMoreUserViewModel(viewModel: MoreUserViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MoreLocalViewModel::class)
  abstract fun bindMoreLocalViewModel(viewModel: MoreLocalViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MyFavoriteViewModel::class)
  abstract fun bindMyFavoriteViewModel(viewModel: MyFavoriteViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(MyWatchlistViewModel::class)
  abstract fun bindMyWatchlistViewModel(viewModel: MyWatchlistViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(PersonMovieViewModel::class)
  abstract fun bindPersonMovieViewModel(viewModel: PersonMovieViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SearchViewModel::class)
  abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(AuthenticationViewModel::class)
  abstract fun bindAuthViewModel(viewModel: AuthenticationViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(RegionViewModel::class)
  abstract fun bindRegionViewModel(viewModel: RegionViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(UserPreferenceViewModel::class)
  abstract fun bindUserPrefViewModel(viewModel: UserPreferenceViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}