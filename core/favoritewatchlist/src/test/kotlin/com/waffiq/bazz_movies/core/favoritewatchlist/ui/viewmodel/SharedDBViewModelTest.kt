package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedDBViewModelTest {
  private lateinit var viewModel: SharedDBViewModel
  private val localDatabaseUseCase: LocalDatabaseUseCase = mockk()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule() // for LiveData testing

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()  // ensures coroutines run instantly

  @Before
  fun setup() {
    every { localDatabaseUseCase.favoriteTvFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.favoriteMoviesFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.watchlistMovieFromDB } returns flowOf(emptyList())
    every { localDatabaseUseCase.watchlistTvFromDB } returns flowOf(emptyList())

    viewModel = SharedDBViewModel(localDatabaseUseCase)
  }

  @Test
  fun retrieveFlowPropertiesAreMappedCorrectly() {
    // Then - verify that the LiveData properties correctly map to the Use Case Flow properties
    assertThat(viewModel.favoriteTvFromDB).isNotNull()
    assertThat(viewModel.favoriteMoviesFromDB).isNotNull()
    assertThat(viewModel.watchlistMoviesDB).isNotNull()
    assertThat(viewModel.watchlistTvSeriesDB).isNotNull()

    // Verify the correct flows are being used
    verify(exactly = 1) { localDatabaseUseCase.favoriteTvFromDB }
    verify(exactly = 1) { localDatabaseUseCase.favoriteMoviesFromDB }
    verify(exactly = 1) { localDatabaseUseCase.watchlistMovieFromDB }
    verify(exactly = 1) { localDatabaseUseCase.watchlistTvFromDB }
  }

  @Test
  fun insertToDB_emitsSuccess() = runTest {
    val favorite = Favorite(
      id = 1,
      mediaId = 1001,
      mediaType = "movie",
      genre = "Action",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "Movie overview",
      title = "Movie Title",
      releaseDate = "2025-01-01",
      popularity = 9.5,
      rating = 4.7f,
      isFavorite = true,
      isWatchlist = false
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.insertToDB(favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    viewModel.dbResult.observeForever { dbResults.add(it) }

    viewModel.insertToDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    coVerify { localDatabaseUseCase.insertToDB(favorite) }
  }

  @Test
  fun delFromFavoriteDB_emitsSuccessAndSetsUndo() = runTest {
    val favorite = Favorite(
      id = 2,
      mediaId = 2002,
      mediaType = "tv",
      genre = "Drama",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "TV Show overview",
      title = "TV Show Title",
      releaseDate = "2025-02-02",
      popularity = 8.7,
      rating = 4.5f,
      isFavorite = true,
      isWatchlist = false
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.deleteFromDB(favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    viewModel.delFromFavoriteDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(favorite)

    coVerify { localDatabaseUseCase.deleteFromDB(favorite) }
  }

  @Test
  fun updateToFavoriteDB_emitsSuccessAndSetsUndo() = runTest {
    val favorite = Favorite(
      id = 3,
      mediaId = 3003,
      mediaType = "movie",
      genre = "Sci-Fi",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "Movie overview",
      title = "Movie Title",
      releaseDate = "2025-03-03",
      popularity = 8.9,
      rating = 4.3f,
      isFavorite = true,
      isWatchlist = false
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.updateFavoriteItemDB(false, favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    viewModel.updateToFavoriteDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(favorite)

    coVerify { localDatabaseUseCase.updateFavoriteItemDB(false, favorite) }
  }

  @Test
  fun updateToWatchlistDB_emitsSuccessAndSetsUndo() = runTest {
    val favorite = Favorite(
      id = 4,
      mediaId = 4004,
      mediaType = "tv",
      genre = "Comedy",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "TV Show overview",
      title = "TV Show Title",
      releaseDate = "2025-04-04",
      popularity = 7.8,
      rating = 4.1f,
      isFavorite = false,
      isWatchlist = true
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.updateWatchlistItemDB(false, favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    viewModel.updateToWatchlistDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(favorite)

    coVerify { localDatabaseUseCase.updateWatchlistItemDB(false, favorite) }
  }

  @Test
  fun updateToRemoveFromWatchlistDB_emitsSuccessAndSetsUndo() = runTest {
    val favorite = Favorite(
      id = 5,
      mediaId = 5005,
      mediaType = "movie",
      genre = "Horror",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "Movie overview",
      title = "Movie Title",
      releaseDate = "2025-05-05",
      popularity = 6.7,
      rating = 3.9f,
      isFavorite = false,
      isWatchlist = true
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.updateWatchlistItemDB(true, favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    viewModel.updateToRemoveFromWatchlistDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(favorite)

    coVerify { localDatabaseUseCase.updateWatchlistItemDB(true, favorite) }
  }

  @Test
  fun updateToRemoveFromFavoriteDB_emitsSuccessAndSetsUndo() = runTest {
    val favorite = Favorite(
      id = 6,
      mediaId = 6006,
      mediaType = "tv",
      genre = "Documentary",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "TV Show overview",
      title = "TV Show Title",
      releaseDate = "2025-06-06",
      popularity = 5.6,
      rating = 4.0f,
      isFavorite = true,
      isWatchlist = false
    )
    val dbResult = DbResult.Success(1)

    coEvery { localDatabaseUseCase.updateFavoriteItemDB(true, favorite) } returns dbResult

    val dbResults = mutableListOf<Event<DbResult<Int>>>()
    val undoResults = mutableListOf<Event<Favorite>>()

    viewModel.dbResult.observeForever { dbResults.add(it) }
    viewModel.undoDB.observeForever { undoResults.add(it) }

    viewModel.updateToRemoveFromFavoriteDB(favorite)
    advanceUntilIdle()

    assertThat(dbResults).hasSize(1)
    assertThat(dbResults[0].peekContent()).isEqualTo(dbResult)

    assertThat(undoResults).hasSize(1)
    assertThat(undoResults[0].peekContent()).isEqualTo(favorite)

    coVerify { localDatabaseUseCase.updateFavoriteItemDB(true, favorite) }
  }

  @Test
  fun testFlowDataTransformsToLiveData() = runTest {
    val favorite = Favorite(
      id = 3,
      mediaId = 3003,
      mediaType = "movie",
      genre = "Sci-Fi",
      backDrop = "backdrop_url",
      poster = "poster_url",
      overview = "overview",
      title = "Title",
      releaseDate = "2025-03-03",
      popularity = 8.9,
      rating = 4.3f,
      isFavorite = true,
      isWatchlist = false
    )
    // Create test data for each flow
    val favoriteTvList = listOf(
      favorite.copy(
        id = 1, mediaId = 101, mediaType = "tv", title = "TV Show 1", isFavorite = true,
        isWatchlist = false
      ),
      favorite.copy(
        id = 2, mediaId = 102, mediaType = "tv", title = "TV Show 2", isFavorite = true,
        isWatchlist = false
      )
    )

    val favoriteMoviesList = listOf(
      favorite.copy(
        id = 3, mediaId = 201, mediaType = "movie", title = "Movie 1", isFavorite = true,
        isWatchlist = false
      ),
      favorite.copy(
        id = 4, mediaId = 202, mediaType = "movie", title = "Movie 2", isFavorite = true,
        isWatchlist = false
      )
    )

    val watchlistTvList = listOf(
      favorite.copy(
        id = 5, mediaId = 301, mediaType = "tv", title = "TV Show 3", isWatchlist = true,
        isFavorite = false
      ),
      favorite.copy(
        id = 6, mediaId = 302, mediaType = "tv", title = "TV Show 4", isWatchlist = true,
        isFavorite = false
      )
    )

    val watchlistMovieList = listOf(
      favorite.copy(
        id = 7, mediaId = 401, mediaType = "movie", title = "Movie 3", isWatchlist = true,
        isFavorite = false
      ),
      favorite.copy(
        id = 8, mediaId = 402, mediaType = "movie", title = "Movie 4", isWatchlist = true,
        isFavorite = false
      )
    )

    // Create mock flows that emit the test data
    val favoriteTvFlow = MutableStateFlow(favoriteTvList)
    val favoriteMoviesFlow = MutableStateFlow(favoriteMoviesList)
    val watchlistTvFlow = MutableStateFlow(watchlistTvList)
    val watchlistMoviesFlow = MutableStateFlow(watchlistMovieList)

    // Override the mock setup to return our flows with test data
    every { localDatabaseUseCase.favoriteTvFromDB } returns favoriteTvFlow
    every { localDatabaseUseCase.favoriteMoviesFromDB } returns favoriteMoviesFlow
    every { localDatabaseUseCase.watchlistTvFromDB } returns watchlistTvFlow
    every { localDatabaseUseCase.watchlistMovieFromDB } returns watchlistMoviesFlow

    // Create a new ViewModel instance with our updated mocks
    val viewModelWithData = SharedDBViewModel(localDatabaseUseCase)

    // Set up observers and collection lists
    val observedFavoriteTvs = mutableListOf<List<Favorite>>()
    val observedFavoriteMovies = mutableListOf<List<Favorite>>()
    val observedWatchlistTvs = mutableListOf<List<Favorite>>()
    val observedWatchlistMovies = mutableListOf<List<Favorite>>()

    viewModelWithData.favoriteTvFromDB.observeForever { observedFavoriteTvs.add(it) }
    viewModelWithData.favoriteMoviesFromDB.observeForever { observedFavoriteMovies.add(it) }
    viewModelWithData.watchlistTvSeriesDB.observeForever { observedWatchlistTvs.add(it) }
    viewModelWithData.watchlistMoviesDB.observeForever { observedWatchlistMovies.add(it) }

    // Let the coroutines process
    advanceUntilIdle()

    // Verify the data was correctly transformed from Flow to LiveData
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
