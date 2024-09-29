package com.waffiq.bazz_movies.di.providers

import android.content.Context
import com.waffiq.bazz_movies.data.local.datasource.LocalDataSource
import com.waffiq.bazz_movies.data.local.room.FavoriteDatabase
import com.waffiq.bazz_movies.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.data.remote.retrofit.OMDbApiConfig
import com.waffiq.bazz_movies.data.remote.retrofit.TMDBApiConfig
import com.waffiq.bazz_movies.data.repository.MoviesRepository
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

object MovieUseCaseProvider {
  private fun provideMovieRepository(context: Context): MoviesRepository {
    val database = FavoriteDatabase.getInstance(context)
    val movieDataSource = MovieDataSource.getInstance(
      TMDBApiConfig.getApiService(),
      OMDbApiConfig.getOMDBApiService()
    )
    val localDataSource = LocalDataSource.getInstance(database.favoriteDao())
    return MoviesRepository(localDataSource, movieDataSource)
  }

  fun provideDetailOMDbUseCase(context: Context): GetDetailOMDbUseCase {
    val repository = provideMovieRepository(context)
    return GetDetailOMDbInteractor(repository)
  }

  // region MOVIE
  fun provideListMoviesUseCase(context: Context): GetListMoviesUseCase {
    val repository = provideMovieRepository(context)
    return GetListMoviesInteractor(repository)
  }

  fun provideDetailMovieUseCase(context: Context): GetDetailMovieUseCase {
    val repository = provideMovieRepository(context)
    return GetDetailMovieInteractor(repository)
  }

  fun provideStatedMovieUseCase(context: Context): GetStatedMovieUseCase {
    val repository = provideMovieRepository(context)
    return GetStatedMovieInteractor(repository)
  }

  fun provideFavoriteMovieUseCase(context: Context): GetFavoriteMovieUseCase {
    val repository = provideMovieRepository(context)
    return GetFavoriteMovieInteractor(repository)
  }

  fun provideWatchlistMovieUseCase(context: Context): GetWatchlistMovieUseCase {
    val repository = provideMovieRepository(context)
    return GetWatchlistMovieInteractor(repository)
  }
  // endregion MOVIE

  // region TV
  fun provideListTvUseCase(context: Context): GetListTvUseCase {
    val repository = provideMovieRepository(context)
    return GetListTvInteractor(repository)
  }

  fun provideDetailTvUseCase(context: Context): GetDetailTvUseCase {
    val repository = provideMovieRepository(context)
    return GetDetailTvInteractor(repository)
  }

  fun provideStatedTvUseCase(context: Context): GetStatedTvUseCase {
    val repository = provideMovieRepository(context)
    return GetStatedTvInteractor(repository)
  }

  fun provideFavoriteTvUseCase(context: Context): GetFavoriteTvUseCase {
    val repository = provideMovieRepository(context)
    return GetFavoriteTvInteractor(repository)
  }

  fun provideWatchlistTvUseCase(context: Context): GetWatchlistTvUseCase {
    val repository = provideMovieRepository(context)
    return GetWatchlistTvInteractor(repository)
  }
  // endregion TV

  // region FAVORITE

  // endregion FAVORITE

  // region WATCHLIST

  // endregion WATCHLIST

  // region SEARCH
  fun provideMultiSearchUseCase(context: Context): MultiSearchUseCase {
    val repository = provideMovieRepository(context)
    return MultiSearchInteractor(repository)
  }
  // endregion SEARCH

  // region PERSON
  fun provideDetailPersonUseCase(context: Context): GetDetailPersonUseCase {
    val repository = provideMovieRepository(context)
    return GetDetailPersonInteractor(repository)
  }
  // endregion PERSON

  // region POST
  fun providePostMethodUseCase(context: Context): PostMethodUseCase {
    val repository = provideMovieRepository(context)
    return PostMethodInteractor(repository)
  }
  // endregion POST

  // region LOCAL DATABASE
  fun provideLocalDatabaseUseCase(context: Context): LocalDatabaseUseCase {
    val repository = provideMovieRepository(context)
    return LocalDatabaseInteractor(repository)
  }
  // endregion LOCAL DATABASE
}