-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepattributes *Annotation*

# LOG
-assumenosideeffects class android.util.Log {
  public static *** v(...);
  public static *** d(...);
  public static *** i(...);
  public static *** w(...);
  public static *** e(...);
}

##---------------Begin: proguard configuration for Firebase crashlytics  ----------
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
##---------------End: proguard configuration for Firebase crashlytics  ----------

##---------------Begin: proguard configuration for Others  ----------
-keep class io.github.glailton.expandabletextview.ExpandableTextView { <init>(android.content.Context, android.util.AttributeSet); }
##---------------End: proguard configuration for Others  ----------

# Keep generated Hilt components
-keep class dagger.** { *; }
-dontwarn dagger.**
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class * extends dagger.hilt.android.HiltWrapper_** { *; }


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
#-dontwarn com.waffiq.bazz_movies.core.database.data.datasource.LocalDataSource
#-dontwarn com.waffiq.bazz_movies.core.database.data.repository.DatabaseRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.core.database.data.room.FavoriteDao
#-dontwarn com.waffiq.bazz_movies.core.database.di.DatabaseModule
#-dontwarn com.waffiq.bazz_movies.core.database.di.DatabaseModule_ProvideDatabaseFactory
#-dontwarn com.waffiq.bazz_movies.core.database.di.DatabaseModule_ProvideFavoriteDaoFactory
#-dontwarn com.waffiq.bazz_movies.core.database.di.DatabaseRepositoryModule
#-dontwarn com.waffiq.bazz_movies.core.database.domain.di.LocalDatabaseModule
#-dontwarn com.waffiq.bazz_movies.core.database.domain.repository.IDatabaseRepository
#-dontwarn com.waffiq.bazz_movies.core.database.domain.usecase.local_database.LocalDatabaseInteractor
#-dontwarn com.waffiq.bazz_movies.core.database.domain.usecase.local_database.LocalDatabaseUseCase
#-dontwarn com.waffiq.bazz_movies.core.movie.data.repository.MoviesRepository
#-dontwarn com.waffiq.bazz_movies.core.movie.di.MovieRepositoryModule
#-dontwarn com.waffiq.bazz_movies.core.movie.di.PostMethodModule
#-dontwarn com.waffiq.bazz_movies.core.movie.di.StatedModule
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieInteractor
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedMovieUseCase
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvInteractor
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.getstated.GetStatedTvUseCase
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodInteractor
#-dontwarn com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
#-dontwarn com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
#-dontwarn com.waffiq.bazz_movies.core.network.data.remote.datasource.UserDataSource
#-dontwarn com.waffiq.bazz_movies.core.network.data.remote.retrofit.CountryIPApiService
#-dontwarn com.waffiq.bazz_movies.core.network.data.remote.retrofit.OMDbApiService
#-dontwarn com.waffiq.bazz_movies.core.network.data.remote.retrofit.TMDBApiService
#-dontwarn com.waffiq.bazz_movies.core.network.di.NetworkModule
#-dontwarn com.waffiq.bazz_movies.core.network.di.NetworkModule_ProvideCountryIPApiServiceFactory
#-dontwarn com.waffiq.bazz_movies.core.network.di.NetworkModule_ProvideOMDBApiServiceFactory
#-dontwarn com.waffiq.bazz_movies.core.network.di.NetworkModule_ProvideOkHttpClientFactory
#-dontwarn com.waffiq.bazz_movies.core.network.di.NetworkModule_ProvideTMDBApiServiceFactory
#-dontwarn com.waffiq.bazz_movies.core.uihelper.utils.UIController
#-dontwarn com.waffiq.bazz_movies.core.user.data.model.UserModel
#-dontwarn com.waffiq.bazz_movies.core.user.data.model.UserPreference
#-dontwarn com.waffiq.bazz_movies.core.user.data.repository.UserRepository
#-dontwarn com.waffiq.bazz_movies.core.user.di.DatastoreModule
#-dontwarn com.waffiq.bazz_movies.core.user.di.DatastoreModule_ProvideDataStoreFactory
#-dontwarn com.waffiq.bazz_movies.core.user.di.DatastoreModule_ProvideUserPreferenceFactory
#-dontwarn com.waffiq.bazz_movies.core.user.di.UserModule
#-dontwarn com.waffiq.bazz_movies.core.user.di.UserRepositoryModule
#-dontwarn com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.auth_tmdb_account.AuthTMDbAccountInteractor
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.get_region.GetRegionInteractor
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.get_region.GetRegionUseCase
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.user_pref.UserPrefInteractor
#-dontwarn com.waffiq.bazz_movies.core.user.domain.usecase.user_pref.UserPrefUseCase
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.RegionViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.core.user.ui.viewmodel.UserPreferenceViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.detail.di.DetailModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.di.DetailRepositoryModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieInteractor
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieUseCase
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbInteractor
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbUseCase
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvInteractor
#-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvUseCase
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieViewModel
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailUserPrefViewModel
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailUserPrefViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailUserPrefViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.favorite.di.FavoriteModule
#-dontwarn com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieInteractor
#-dontwarn com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteMovieUseCase
#-dontwarn com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvInteractor
#-dontwarn com.waffiq.bazz_movies.feature.favorite.domain.usecase.GetFavoriteTvUseCase
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteMoviesFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteTvSeriesFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteViewModel
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.favorite.ui.MyFavoriteViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.home.data.repository.HomeRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.home.di.HomeModule
#-dontwarn com.waffiq.bazz_movies.feature.home.di.HomeRepositoryModule
#-dontwarn com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
#-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesInteractor
#-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesUseCase
#-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvInteractor
#-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvUseCase
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.FeaturedFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.HomeFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.MovieFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.TvSeriesFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.login.di.AuthModule
#-dontwarn com.waffiq.bazz_movies.feature.login.ui.AuthenticationViewModel
#-dontwarn com.waffiq.bazz_movies.feature.login.ui.AuthenticationViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.login.ui.AuthenticationViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.login.ui.LoginActivity
#-dontwarn com.waffiq.bazz_movies.feature.login.ui.LoginActivity_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreLocalViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.more.ui.MoreUserViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.person.data.repository.PersonRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.person.di.PersonModule
#-dontwarn com.waffiq.bazz_movies.feature.person.di.PersonRepositoryModule
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonInteractor
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonActivity_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.search.data.repository.SearchRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.search.di.SearchModule
#-dontwarn com.waffiq.bazz_movies.feature.search.di.SearchRepositoryModule
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchInteractor
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchFragment
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchViewModel
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.di.WatchlistModule
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieInteractor
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistMovieUseCase
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvInteractor
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.domain.usecase.GetWatchlistTvUseCase
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistMoviesFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistTvSeriesFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistViewModel
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistViewModel_HiltModules$BindsModule
#-dontwarn com.waffiq.bazz_movies.feature.watchlist.ui.MyWatchlistViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.navigation.Navigator