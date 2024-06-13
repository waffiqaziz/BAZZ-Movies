package com.waffiq.bazz_movies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.repository.MoviesRepository
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.di.MovieUseCaseProvider.provideListMoviesUseCase
import com.waffiq.bazz_movies.di.MovieUseCaseProvider.provideDetailOMDbUseCase
import com.waffiq.bazz_movies.di.MovieUseCaseProvider.provideDetailMovieUseCase
import com.waffiq.bazz_movies.di.MovieUseCaseProvider.provideStatedMovieUseCase
import com.waffiq.bazz_movies.di.MovieUseCaseProvider.provideListTvUseCase
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_omdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_person.GetDetailPersonUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_tv.GetDetailTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_movies.GetListMoviesUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase

object Injection {

  fun provideUseCaseProvider(context: Context) = MovieUseCaseProvider

  fun provideMovieRepository(context: Context): MoviesRepository =
    MovieUseCaseProvider.provideMovieRepository(context)

  fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository =
    UserUseCaseProvider.provideUserRepository(dataStore)

  fun provideGetDetailOMDb(context: Context): GetDetailOMDbUseCase =
    provideDetailOMDbUseCase(context)

  // region MOVIE
  fun provideGetListMoviesUseCase(context: Context): GetListMoviesUseCase =
    provideListMoviesUseCase(context)

  fun provideGetDetailMovieUseCase(context: Context): GetDetailMovieUseCase =
    provideDetailMovieUseCase(context)

  fun provideGetStatedMovieUseCase(context: Context): GetStatedMovieUseCase =
    provideStatedMovieUseCase(context)

  fun provideGetFavoriteMovieUseCase(context: Context): GetFavoriteMovieUseCase =
    MovieUseCaseProvider.provideFavoriteMovieUseCase(context)

  fun provideGetWatchlistMovieUseCase(context: Context): GetWatchlistMovieUseCase =
    MovieUseCaseProvider.provideWatchlistMovieUseCase(context)
  // endregion MOVIE

  // region TV
  fun provideGetListTvUseCase(context: Context): GetListTvUseCase =
    provideListTvUseCase(context)

  fun provideGetDetailTvUseCase(context: Context): GetDetailTvUseCase =
    MovieUseCaseProvider.provideDetailTvUseCase(context)

  fun provideGetStatedTvUseCase(context: Context): GetStatedTvUseCase =
    MovieUseCaseProvider.provideStatedTvUseCase(context)

  fun provideGetFavoriteTvUseCase(context: Context): GetFavoriteTvUseCase =
    MovieUseCaseProvider.provideFavoriteTvUseCase(context)

  fun provideGetWatchlistTvUseCase(context: Context): GetWatchlistTvUseCase =
    MovieUseCaseProvider.provideWatchlistTvUseCase(context)
  // endregion TV

  // region FAVORITE

  // endregion FAVORITE

  // region WATCHLIST

  // endregion WATCHLIST

  // region PERSON
  fun provideGetDetailPersonUseCase(context: Context): GetDetailPersonUseCase =
    MovieUseCaseProvider.provideDetailPersonUseCase(context)
  // endregion PERSON

  // region POST
  fun providePostMethodUseCase(context: Context): PostMethodUseCase =
    MovieUseCaseProvider.providePostMethodUseCase(context)
  // endregion POST

  // region LOCAL DATABASE
  fun provideLocalDatabaseUseCase(context: Context): LocalDatabaseUseCase =
    MovieUseCaseProvider.provideLocalDatabaseUseCase(context)
  // endregion LOCAL DATABASE

  // region USER
  fun provideAuthTMDbAccountUseCase(dataStore: DataStore<Preferences>): AuthTMDbAccountUseCase =
    UserUseCaseProvider.provideAuthTMDbAccountUseCase(dataStore)

  fun provideUserPrefUseCase(dataStore: DataStore<Preferences>): UserPrefUseCase =
    UserUseCaseProvider.provideUserPrefUseCase(dataStore)

  fun provideGetRegion(dataStore: DataStore<Preferences>): GetRegionUseCase =
    UserUseCaseProvider.provideGetRegion(dataStore)
  // endregion USER

}