package com.waffiq.bazz_movies.core.favoritewatchlist

import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteMoviesList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.favoriteTvList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistMovieList
import com.waffiq.bazz_movies.core.favoritewatchlist.testutils.DummyData.watchlistTvList
import com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel.SharedDBViewModel
import com.waffiq.bazz_movies.core.models.Favorite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

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
    assertTrue(observedFavoriteTvs.isNotEmpty())
    assertEquals(observedFavoriteTvs[0], favoriteTvList)

    assertTrue(observedFavoriteMovies.isNotEmpty())
    assertEquals(observedFavoriteMovies[0], favoriteMoviesList)

    assertTrue(observedWatchlistTvs.isNotEmpty())
    assertEquals(observedWatchlistTvs[0], watchlistTvList)

    assertTrue(observedWatchlistMovies.isNotEmpty())
    assertEquals(observedWatchlistMovies[0], watchlistMovieList)
  }
}
