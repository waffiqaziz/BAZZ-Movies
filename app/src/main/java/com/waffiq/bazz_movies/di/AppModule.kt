package com.waffiq.bazz_movies.di

import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountInteractor
import com.waffiq.bazz_movies.domain.usecase.auth_tmdb_account.AuthTMDbAccountUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_movie.GetDetailMovieInteractor
import com.waffiq.bazz_movies.domain.usecase.get_detail_movie.GetDetailMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_omdb.GetDetailOMDbInteractor
import com.waffiq.bazz_movies.domain.usecase.get_detail_omdb.GetDetailOMDbUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_person.GetDetailPersonInteractor
import com.waffiq.bazz_movies.domain.usecase.get_detail_person.GetDetailPersonUseCase
import com.waffiq.bazz_movies.domain.usecase.get_detail_tv.GetDetailTvInteractor
import com.waffiq.bazz_movies.domain.usecase.get_detail_tv.GetDetailTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteMovieInteractor
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteTvInteractor
import com.waffiq.bazz_movies.domain.usecase.get_favorite.GetFavoriteTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_movies.GetListMoviesInteractor
import com.waffiq.bazz_movies.domain.usecase.get_list_movies.GetListMoviesUseCase
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvInteractor
import com.waffiq.bazz_movies.domain.usecase.get_list_tv.GetListTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionInteractor
import com.waffiq.bazz_movies.domain.usecase.get_region.GetRegionUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieInteractor
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvInteractor
import com.waffiq.bazz_movies.domain.usecase.get_stated.GetStatedTvUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistMovieInteractor
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistMovieUseCase
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistTvInteractor
import com.waffiq.bazz_movies.domain.usecase.get_watchlist.GetWatchlistTvUseCase
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseInteractor
import com.waffiq.bazz_movies.domain.usecase.local_database.LocalDatabaseUseCase
import com.waffiq.bazz_movies.domain.usecase.multi_search.MultiSearchInteractor
import com.waffiq.bazz_movies.domain.usecase.multi_search.MultiSearchUseCase
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodInteractor
import com.waffiq.bazz_movies.domain.usecase.post_method.PostMethodUseCase
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefInteractor
import com.waffiq.bazz_movies.domain.usecase.user_pref.UserPrefUseCase
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class AppModule {

  // region USER
  @Binds
  abstract fun provideAuthTMDbAccountUseCase(
    authTMDbAccountInteractor: AuthTMDbAccountInteractor
  ): AuthTMDbAccountUseCase

  @Binds
  abstract fun provideUserPrefUseCase(
    userPrefInteractor: UserPrefInteractor
  ): UserPrefUseCase

  @Binds
  abstract fun provideGetRegionUseCase(
    getRegionInteractor: GetRegionInteractor
  ): GetRegionUseCase
  // endregion USER

  @Binds
  abstract fun provideDetailOMDbUseCase(
    getDetailOMDbInteractor: GetDetailOMDbInteractor
  ): GetDetailOMDbUseCase

  // region MOVIE
  @Binds
  abstract fun provideListMoviesUseCase(
    getListMoviesInteractor: GetListMoviesInteractor
  ): GetListMoviesUseCase

  @Binds
  abstract fun provideDetailMovieUseCase(
    getDetailMovieInteractor: GetDetailMovieInteractor
  ): GetDetailMovieUseCase

  @Binds
  abstract fun provideStatedMovieUseCase(
    getStatedMovieInteractor: GetStatedMovieInteractor
  ): GetStatedMovieUseCase

  @Binds
  abstract fun provideFavoriteMovieUseCase(
    getFavoriteMovieInteractor: GetFavoriteMovieInteractor
  )
    : GetFavoriteMovieUseCase

  @Binds
  abstract fun provideWatchlistMovieUseCase(
    getWatchlistMovieInteractor: GetWatchlistMovieInteractor
  ): GetWatchlistMovieUseCase
  // endregion MOVIE


  @Binds// region TV
  abstract fun provideListTvUseCase(
    getListTvInteractor: GetListTvInteractor
  ): GetListTvUseCase

  @Binds
  abstract fun provideDetailTvUseCase(
    getDetailTvInteractor: GetDetailTvInteractor
  ): GetDetailTvUseCase

  @Binds
  abstract fun provideStatedTvUseCase(
    getStatedTvInteractor: GetStatedTvInteractor
  ): GetStatedTvUseCase

  @Binds
  abstract fun provideFavoriteTvUseCase(
    getFavoriteTvInteractor: GetFavoriteTvInteractor
  ): GetFavoriteTvUseCase

  @Binds
  abstract fun provideWatchlistTvUseCase(
    getWatchlistTvInteractor: GetWatchlistTvInteractor
  ): GetWatchlistTvUseCase
  // endregion TV

  @Binds
  abstract fun provideMultiSearchUseCase(
    multiSearchInteractor: MultiSearchInteractor
  ): MultiSearchUseCase

  @Binds
  abstract fun provideDetailPersonUseCase(
    getDetailPersonInteractor: GetDetailPersonInteractor
  ): GetDetailPersonUseCase

  @Binds
  abstract fun providePostMethodUseCase(
    postMethodInteractor: PostMethodInteractor
  ): PostMethodUseCase

  @Binds
  abstract fun provideLocalDatabaseUseCase(
    localDatabaseInteractor: LocalDatabaseInteractor
  ): LocalDatabaseUseCase
}