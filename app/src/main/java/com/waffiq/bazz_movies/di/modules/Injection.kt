package com.waffiq.bazz_movies.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.waffiq.bazz_movies.data.repository.UserRepository
import com.waffiq.bazz_movies.di.providers.MovieUseCaseProvider
import com.waffiq.bazz_movies.di.providers.UserUseCaseProvider
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
import com.waffiq.bazz_movies.domain.usecase.multi_search.MultiSearchUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase

object Injection {

  fun provideGetDetailOMDbUseCase(context: Context): GetDetailOMDbUseCase =
    MovieUseCaseProvider.provideDetailOMDbUseCase(context)

  // region MOVIE
  fun provideGetListMoviesUseCase(context: Context): GetListMoviesUseCase =
    MovieUseCaseProvider.provideListMoviesUseCase(context)

  fun provideGetDetailMovieUseCase(context: Context): GetDetailMovieUseCase =
    MovieUseCaseProvider.provideDetailMovieUseCase(context)

  fun provideGetStatedMovieUseCase(context: Context): GetStatedMovieUseCase =
    MovieUseCaseProvider.provideStatedMovieUseCase(context)

  fun provideGetFavoriteMovieUseCase(context: Context): GetFavoriteMovieUseCase =
    MovieUseCaseProvider.provideFavoriteMovieUseCase(context)

  fun provideGetWatchlistMovieUseCase(context: Context): GetWatchlistMovieUseCase =
    MovieUseCaseProvider.provideWatchlistMovieUseCase(context)
  // endregion MOVIE

  // region TV
  fun provideGetListTvUseCase(context: Context): GetListTvUseCase =
    MovieUseCaseProvider.provideListTvUseCase(context)

  fun provideGetDetailTvUseCase(context: Context): GetDetailTvUseCase =
    MovieUseCaseProvider.provideDetailTvUseCase(context)

  fun provideGetStatedTvUseCase(context: Context): GetStatedTvUseCase =
    MovieUseCaseProvider.provideStatedTvUseCase(context)

  fun provideGetFavoriteTvUseCase(context: Context): GetFavoriteTvUseCase =
    MovieUseCaseProvider.provideFavoriteTvUseCase(context)

  fun provideGetWatchlistTvUseCase(context: Context): GetWatchlistTvUseCase =
    MovieUseCaseProvider.provideWatchlistTvUseCase(context)
  // endregion TV

  // region MULTI SEARCH
  fun provideGetMultiSearchUseCase(context: Context): MultiSearchUseCase =
    MovieUseCaseProvider.provideMultiSearchUseCase(context)
  // endregion MULTI SEARCH

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

  fun provideGetRegionUseCase(dataStore: DataStore<Preferences>): GetRegionUseCase =
    UserUseCaseProvider.provideGetRegionUseCase(dataStore)
  // endregion USER
}