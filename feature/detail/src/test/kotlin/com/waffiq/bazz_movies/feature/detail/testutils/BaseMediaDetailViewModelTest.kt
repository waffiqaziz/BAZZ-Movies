package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.MediaStateUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.composite.PostActionUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.PostRateUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.ui.state.MediaDetailUiState
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

abstract class BaseMediaDetailViewModelTest {

  protected lateinit var viewModel: MediaDetailViewModel

  protected val mockGetListMoviesUseCase: GetListMoviesUseCase = mockk()
  protected val mockGetListTvUseCase: GetListTvUseCase = mockk()
  protected val mockLocalDatabaseUseCase: LocalDatabaseUseCase = mockk()
  protected val mockPostRateUseCase: PostRateUseCase = mockk()
  protected val mockPostActionUseCase: PostActionUseCase = mockk()
  protected val mockGetOMDbDetailUseCase: GetOMDbDetailUseCase = mockk()
  protected val mockMediaStateUseCase: MediaStateUseCase = mockk()
  protected val mockGetMediaDetailUseCase: GetMediaDetailUseCase = mockk()
  protected val mockUserPrefUseCase: UserPrefUseCase = mockk()

  protected val imdbId = IMDB_ID
  protected val movieId = 123
  protected val tvId = 456
  protected val errorMessage = ERROR_MESSAGE

  protected val mockVideoLink = String()
  protected val mockMediaItem = mockk<MediaItem>()
  protected val mockMediaDetail = mockk<MediaDetail>()
  protected val mockMediaStated = MediaState(
    id = 112,
    favorite = false,
    rated = Rated.Unrated,
    watchlist = false
  )
  protected val mockMediaCredits = mockk<MediaCredits>()
  protected val mockOmdb = mockk<OMDbDetails>()

  protected val mockWatchProvider = WatchProvidersItem(
    ads = listOf(mockk()),
    buy = listOf(mockk()),
    flatrate = listOf(mockk()),
    free = listOf(),
    rent = listOf(mockk()),
    link = String()
  )
  protected val mockWatchProviderState = WatchProvidersUiState.Success(
    ads = mockWatchProvider.ads.orEmpty(),
    buy = mockWatchProvider.buy.orEmpty(),
    flatrate = mockWatchProvider.flatrate.orEmpty(),
    free = mockWatchProvider.free.orEmpty(),
    rent = mockWatchProvider.rent.orEmpty()
  )
  protected val nullProvider = WatchProvidersItem(
    ads = null,
    buy = null,
    flatrate = null,
    free = null,
    rent = null,
    link = null
  )
  protected val fullProvider = WatchProvidersItem(
    ads = listOf(mockk()),
    buy = listOf(mockk()),
    flatrate = listOf(mockk()),
    free = listOf(mockk()),
    rent = listOf(mockk()),
    link = String()
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
    )
  }

  private val errorStates = mutableListOf<String>()

  protected fun <T> successFlow(data: T): Flow<Outcome.Success<T>> =
    flow { emit(Outcome.Success(data)) }

  protected fun <T> successDbResult(data: T): DbResult<T> =
    DbResult.Success(data)

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

  protected fun <T> testViewModelState(
    runBlock: () -> Unit,
    stateSelector: (MediaDetailUiState) -> T,
    expectedStates: List<T>? = null,
    expectedLoadingStates: List<Boolean>? = null,
    expectedErrors: List<String>? = null,
    verifyBlock: () -> Unit = {}
  ) = runTest {

    val collectedStates = mutableListOf<T>()
    val collectedLoadingStates = mutableListOf<Boolean>()
    val collectedErrors = mutableListOf<String>()

    val stateJob = launch {
      viewModel.uiState
        .map { stateSelector(it) }
        .filterNotNull() // remove null value from nullable value inside MediaDetailUiState
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
      assertThat(collectedStates)
        .containsExactlyElementsIn(it)
        .inOrder()
    }

    // first loading state always true because default value
    expectedLoadingStates?.let {
      assertThat(collectedLoadingStates)
        .containsExactlyElementsIn(it)
        .inOrder()
    }

    expectedErrors?.let {
      assertThat(collectedErrors)
        .containsExactlyElementsIn(it)
        .inOrder()
    } ?: run {
      assertThat(collectedErrors).isEmpty()
    }

    verifyBlock()
  }
}
