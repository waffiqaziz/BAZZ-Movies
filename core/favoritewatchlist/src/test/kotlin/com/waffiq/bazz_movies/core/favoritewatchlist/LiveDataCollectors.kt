package com.waffiq.bazz_movies.core.favoritewatchlist

import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.TestData
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel

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

  fun assertAllDataTransformed(testData: TestData) {
    assertThat(observedFavoriteTvs).isNotEmpty()
    assertThat(observedFavoriteTvs[0]).isEqualTo(testData.favoriteTvList)

    assertThat(observedFavoriteMovies).isNotEmpty()
    assertThat(observedFavoriteMovies[0]).isEqualTo(testData.favoriteMoviesList)

    assertThat(observedWatchlistTvs).isNotEmpty()
    assertThat(observedWatchlistTvs[0]).isEqualTo(testData.watchlistTvList)

    assertThat(observedWatchlistMovies).isNotEmpty()
    assertThat(observedWatchlistMovies[0]).isEqualTo(testData.watchlistMovieList)
  }
}
