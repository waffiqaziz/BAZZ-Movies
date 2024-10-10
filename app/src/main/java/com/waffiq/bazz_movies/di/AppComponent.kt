package com.waffiq.bazz_movies.di

import com.waffiq.bazz_movies.core.di.CoreComponent
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.ui.activity.LoginActivity
import com.waffiq.bazz_movies.ui.activity.RoutingActivity
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.ui.activity.home.FeaturedFragment
import com.waffiq.bazz_movies.ui.activity.home.HomeFragment
import com.waffiq.bazz_movies.ui.activity.home.MovieFragment
import com.waffiq.bazz_movies.ui.activity.home.TvSeriesFragment
import com.waffiq.bazz_movies.ui.activity.more.MoreFragment
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteMoviesFragment
import com.waffiq.bazz_movies.ui.activity.myfavorite.MyFavoriteTvSeriesFragment
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistMoviesFragment
import com.waffiq.bazz_movies.ui.activity.mywatchlist.MyWatchlistTvSeriesFragment
import com.waffiq.bazz_movies.ui.activity.person.PersonActivity
import com.waffiq.bazz_movies.ui.activity.search.SearchFragment
import dagger.Component

@AppScope
@Component(
  dependencies = [CoreComponent::class],
  modules = [AppModule::class, ViewModelModule::class]
)
interface AppComponent {
  @Component.Factory
  interface Factory {
    fun create(coreComponent: CoreComponent): AppComponent
  }

  fun inject(fragment: FeaturedFragment)
  fun inject(fragment: HomeFragment)
  fun inject(fragment: MovieFragment)
  fun inject(fragment: TvSeriesFragment)
  fun inject(fragment: MoreFragment)
  fun inject(fragment: MyFavoriteMoviesFragment)
  fun inject(fragment: MyFavoriteTvSeriesFragment)
  fun inject(fragment: MyWatchlistMoviesFragment)
  fun inject(fragment: MyWatchlistTvSeriesFragment)
  fun inject(fragment: SearchFragment)
  fun inject(activity: LoginActivity)
  fun inject(activity: RoutingActivity)
  fun inject(activity: DetailMovieActivity)
  fun inject(activity: PersonActivity)
  fun inject(userRepository: UserRepository)
}