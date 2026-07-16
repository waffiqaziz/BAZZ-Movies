package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.data.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.core.database.domain.usecase.FavoriteLocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.models.MediaItem
import com.waffiq.bazz_movies.core.models.MediaState
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.core.models.Rated
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.collection.GetMovieCollectionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.RefreshMediaMetadataUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.favoriteMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.oMDbDetails
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.CollectionViewModel
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule

abstract class BaseMediaDetailViewModelTest {

  protected lateinit var viewModel: MediaDetailViewModel
  protected lateinit var collectionViewModel: CollectionViewModel

  protected val mockGetListMoviesUseCase: GetListMoviesUseCase = mockk()
  protected val mockGetListTvUseCase: GetListTvUseCase = mockk()
  protected val mockLocalDatabaseUseCase: FavoriteLocalDatabaseUseCase = mockk()
  protected val mockPostRateUseCase: PostRateUseCase = mockk()
  protected val mockPostActionUseCase: PostActionUseCase = mockk()
  protected val mockGetOMDbDetailUseCase: GetOMDbDetailUseCase = mockk()
  protected val mockMediaStateUseCase: MediaStateUseCase = mockk()
  protected val mockGetMediaDetailUseCase: GetMediaDetailUseCase = mockk()
  protected val mockUserPrefUseCase: UserPrefUseCase = mockk()
  protected val mockRefreshMediaMetadataUseCase: RefreshMediaMetadataUseCase = mockk()
  protected val mockGetMovieCollectionUseCase: GetMovieCollectionUseCase = mockk()

  protected val imdbId = IMDB_ID
  protected val movieId = MOVIE_ID
  protected val tvId = TV_ID
  protected val collectionId = 231
  protected val errorMessage = ERROR_MESSAGE

  protected val mockMediaItem = mockk<MediaItem>()
  protected val mockMediaDetail = movieMediaDetail
  protected val mockMediaStated = MediaState(
    id = 112,
    favorite = false,
    rated = Rated.Unrated,
    watchlist = false,
  )
  protected val mockMediaCredits = mockk<MediaCredits>()
  protected val mockOmdb = oMDbDetails

  protected val mockWatchProvider = WatchProvidersItem(
    ads = emptyList(),
    buy = emptyList(),
    flatrate = emptyList(),
    free = emptyList(),
    rent = emptyList(),
    link = String(),
  )
  protected val mockWatchProviderState = WatchProvidersUiState.Success(
    ads = mockWatchProvider.ads.orEmpty(),
    buy = mockWatchProvider.buy.orEmpty(),
    flatrate = mockWatchProvider.flatrate.orEmpty(),
    free = mockWatchProvider.free.orEmpty(),
    rent = mockWatchProvider.rent.orEmpty(),
  )
  protected val nullProvider = WatchProvidersItem(
    ads = null,
    buy = null,
    flatrate = null,
    free = null,
    rent = null,
    link = null,
  )

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  open fun setup() {
    errorStates.clear()
    viewModel = MediaDetailViewModel(
      mockGetListMoviesUseCase,
      mockGetListTvUseCase,
      mockLocalDatabaseUseCase,
      mockPostRateUseCase,
      mockPostActionUseCase,
      mockGetOMDbDetailUseCase,
      mockMediaStateUseCase,
      mockGetMediaDetailUseCase,
      mockRefreshMediaMetadataUseCase,
    )
    collectionViewModel = CollectionViewModel(mockGetMovieCollectionUseCase)
  }

  private val errorStates = mutableListOf<String>()

  protected fun <T> successFlow(data: T): Flow<Outcome.Success<T>> =
    flow { emit(Outcome.Success(data)) }

  protected fun <T> successDbResult(data: T): DbResult<T> = DbResult.Success(data)

  protected val errorDbResult = DbResult.Error(errorMessage)
  protected val loadingFlow = flowOf(Outcome.Loading)
  protected val errorFlow = flowOf(Outcome.Error(errorMessage))

  protected fun <T : Any> testPagingState(
    pagingFlow: StateFlow<PagingData<T>>,
    runBlock: () -> Unit,
    differ: AsyncPagingDataDiffer<T> = differ(),
    itemAssertions: (List<T>) -> Unit = { /* do nothing */ },
  ) = runTest {
    runBlock()
    advanceUntilIdle()

    val pagingData = pagingFlow.value

    val job = launch { differ.submitData(pagingData) }
    advanceUntilIdle()

    val snapshot = differ.snapshot().items
    itemAssertions(snapshot)

    job.cancel()
  }

  @Suppress("LongParameterList", "MaxLineLength")
  protected fun <T> testViewModelState(
    runBlock: () -> Unit,
    stateSelector: (MediaDetailUiState) -> T,
    expectedStates: List<T>? = null,
    expectedLoadingStates: List<Boolean>? = null,
    expectedErrors: List<String>? = null,
    verifyBlock: () -> Unit = {},
  ) = runTest {
    val collectedStates = mutableListOf<T>()
    val collectedLoadingStates = mutableListOf<Boolean>()
    val collectedErrors = mutableListOf<String>()

    val stateJob = launch {
      viewModel.uiState.mapNotNull {
        // remove null value from nullable value inside MediaDetailUiState
        stateSelector(it)
      }
        .distinctUntilChanged()
        .collect { collectedStates.add(it) }
    }

    val loadingJob = launch {
      viewModel.uiState
        .map { it.isLoading }
        .distinctUntilChanged()
        .collect { collectedLoadingStates.add(it) }
    }

    val errorJob = launch {
      viewModel.errorEvent.collect { collectedErrors.add(it) }
    }

    runBlock()
    advanceUntilIdle()

    stateJob.cancel()
    loadingJob.cancel()
    errorJob.cancel()

    // Assertions
    expectedStates?.let {
      assertEquals(it, collectedStates)
    }

    // first loading state always true because default value
    expectedLoadingStates?.let {
      assertEquals(it, collectedLoadingStates)
    }

    expectedErrors?.let {
      assertEquals(it, collectedErrors)
    } ?: run {
      assertTrue(collectedErrors.isEmpty())
    }

    verifyBlock()
  }

  fun setupGetOMDbDetailsMockReturnValue() {
    coEvery { mockGetOMDbDetailUseCase.getOMDbDetails(any()) } returns
      successFlow(mockOmdb)
  }

  protected fun stubState(isFavorite: Boolean, isWatchlist: Boolean) {
    coEvery { mockLocalDatabaseUseCase.getByMedia(any(), any()) } returns
      favoriteMovie.copy(isFavorite = isFavorite, isWatchlist = isWatchlist)

    viewModel.getByMedia(1, "1")
  }

  protected fun stubUpdateSuccess() {
    coEvery { mockLocalDatabaseUseCase.update(any<Favorite>()) } returns successDbResult(Unit)
  }
}
