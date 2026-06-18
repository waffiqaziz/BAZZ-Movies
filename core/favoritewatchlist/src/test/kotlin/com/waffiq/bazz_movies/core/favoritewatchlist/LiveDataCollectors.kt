package com.waffiq.bazz_movies.core.favoritewatchlist

import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteMoviesList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteTvList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistMovieList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistTvList
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.models.Favorite

/**
 * Collects LiveData from the [SharedDBViewModel] for testing purposes.
 * It observes the LiveData properties and stores the emitted values in lists for assertions.
 */
class LiveDataCollectors(viewModel: SharedDBViewModel) {
  private val observedFavoriteTvs = mutableListOf<List<Favorite>>()
  private val observedFavoriteMovies = mutableListOf<List<Favorite>>()
  private val observedWatchlistTvs = mutableListOf<List<Favorite>>()
  private val observedWatchlistMovies = mutableListOf<List<Favorite>>()

  init {
    viewModel.favoriteTvFromDB.observeForever { observedFavoriteTvs.add(it) }
    viewModel.favoriteMoviesFromDB.observeForever { observedFavoriteMovies.add(it) }
    viewModel.watchlistTvSeriesDB.observeForever { observedWatchlistTvs.add(it) }
    viewModel.watchlistMoviesDB.observeForever { observedWatchlistMovies.add(it) }
  }

  fun assertAllDataTransformed() {
    assertThat(observedFavoriteTvs).isNotEmpty()
    assertThat(observedFavoriteTvs[0]).isEqualTo(favoriteTvList)

    assertThat(observedFavoriteMovies).isNotEmpty()
    assertThat(observedFavoriteMovies[0]).isEqualTo(favoriteMoviesList)

    assertThat(observedWatchlistTvs).isNotEmpty()
    assertThat(observedWatchlistTvs[0]).isEqualTo(watchlistTvList)

    assertThat(observedWatchlistMovies).isNotEmpty()
    assertThat(observedWatchlistMovies[0]).isEqualTo(watchlistMovieList)
  }
}
