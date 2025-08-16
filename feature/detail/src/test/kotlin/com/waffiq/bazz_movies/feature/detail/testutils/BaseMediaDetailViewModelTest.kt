package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.database.domain.usecase.localdatabase.LocalDatabaseUseCase
import com.waffiq.bazz_movies.core.database.utils.DbResult
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.domain.MediaState
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.movie.domain.usecase.postmethod.PostMethodUseCase
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMediaStateWithUserUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetMovieDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.composite.GetTvDataWithUserRegionUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail.GetMovieDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getOmdbDetail.GetOMDbDetailUseCase
import com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail.GetTvDetailUseCase
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.IMDB_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

abstract class BaseMediaDetailViewModelTest {

  protected lateinit var viewModel: MediaDetailViewModel

  protected val getMovieDetailUseCase: GetMovieDetailUseCase = mockk()
  protected val getTvDetailUseCase: GetTvDetailUseCase = mockk()
  protected val localDatabaseUseCase: LocalDatabaseUseCase = mockk()
  protected val postMethodUseCase: PostMethodUseCase = mockk()
  protected val getOMDbDetailUseCase: GetOMDbDetailUseCase = mockk()
  protected val getMediaStateWithUserUseCase: GetMediaStateWithUserUseCase = mockk()
  protected val getMovieDataWithUserPrefUseCase: GetMovieDataWithUserRegionUseCase = mockk()
  protected val getTvDataWithUserPrefUseCase: GetTvDataWithUserRegionUseCase = mockk()

  protected val imdbId = IMDB_ID
  protected val sessionId = "session ID"
  protected val movieId = 123
  protected val tvId = 456
  protected val usRegion = USER_REGION
  protected val errorMessage = ERROR_MESSAGE

  protected val mockLinkVideo = String()
  protected val mockMediaItem = mockk<MediaItem>()
  protected val mockMediaDetail = mockk<MediaDetail>()
  protected val mockMediaStated = mockk<MediaState>()
  protected val mockMediaCredits = mockk<MediaCredits>()
  protected val mockOmdb = mockk<OMDbDetails>()
  protected val mockTvExternalIds = mockk<TvExternalIds>()

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
  fun setup() {
    viewModel = MediaDetailViewModel(
      getMovieDetailUseCase,
      getTvDetailUseCase,
      localDatabaseUseCase,
      postMethodUseCase,
      getOMDbDetailUseCase,
      getMediaStateWithUserUseCase,
      getMovieDataWithUserPrefUseCase,
      getTvDataWithUserPrefUseCase,
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

  protected fun <T : Any> testViewModelFlow(
    runBlock: () -> Unit,
    liveData: LiveData<T>,
    expectedSuccess: T? = null,
    expectError: String? = null,
    verifyBlock: () -> Unit,
  ) = runTest {
    val collectedData = mutableListOf<T>()
    val observer = Observer<T> { collectedData.add(it) }
    val errorJob = launch {
      viewModel.errorState.collect { errorStates.add(it) }
    }

    liveData.observeForever(observer)

    runBlock()
    advanceUntilIdle()
    if (expectError != null) {
      advanceTimeBy(100)
    }

    liveData.removeObserver(observer)
    errorJob.cancel()

    expectedSuccess?.let {
      assertThat(collectedData).containsExactly(it)
      assertThat(liveData.value).isEqualTo(it)
    } ?: assertThat(collectedData).isEmpty()

    expectError?.let {
      assertThat(errorStates).containsExactly(it)
    } ?: run {
      assertThat(errorStates).isEmpty()
    }

    verifyBlock()
  }

  protected fun <T : Any> testSealedUiStateFlow(
    runBlock: () -> Unit,
    liveData: LiveData<T>,
    expectedState: T,
    verifyBlock: () -> Unit,
  ) = runTest {
    val collected = mutableListOf<T>()
    val observer = Observer<T> { collected.add(it) }

    liveData.observeForever(observer)
    runBlock()
    advanceUntilIdle()
    liveData.removeObserver(observer)

    assertThat(collected).containsExactly(expectedState)
    assertThat(liveData.value).isEqualTo(expectedState)

    verifyBlock()
  }

  protected fun <T : Any> testPagingLiveData(
    liveData: LiveData<PagingData<T>>,
    runBlock: () -> Unit,
    diffCallback: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
      override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
      override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    },
    itemAssertions: (List<T>) -> Unit = { /* do nothing */ },
  ) = runTest {
    val differ = AsyncPagingDataDiffer(
      diffCallback = diffCallback,
      updateCallback = TestListCallback(),
      mainDispatcher = Dispatchers.Main,
      workerDispatcher = Dispatchers.Main
    )

    runBlock()

    liveData.asFlow().test {
      val pagingData = awaitItem()

      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val snapshot = differ.snapshot().items
      itemAssertions(snapshot)

      job.cancel()
      cancelAndIgnoreRemainingEvents()
    }
  }

  protected fun <T : Any> testViewModelFlowEvent(
    runBlock: () -> Unit,
    liveData: LiveData<T>,
    expectedSuccess: T? = null,
    expectError: String? = null,
    checkLoading: Boolean = false,
    verifyBlock: () -> Unit,
  ) = runTest {
    val collectedData = mutableListOf<T>()
    val collectedLoadingStates = mutableListOf<Boolean>()

    val observer = Observer<T> { collectedData.add(it) }
    val loadingObserver = Observer<Boolean> { collectedLoadingStates.add(it) }
    val errorJob = launch {
      viewModel.errorState.collect { errorStates.add(it) }
    }

    // observe the test block
    liveData.observeForever(observer)
    if (checkLoading) {
      // capture initial loading state
      viewModel.loadingState.value?.let { initialState ->
        collectedLoadingStates.add(initialState)
      }
      viewModel.loadingState.observeForever(loadingObserver)
    }

    // trigger the test block
    runBlock()
    advanceUntilIdle()

    // stop observing
    liveData.removeObserver(observer)
    if (checkLoading) {
      viewModel.loadingState.removeObserver(loadingObserver)
    }
    errorJob.cancel()

    // perform success check
    checkEventSuccess(liveData, expectedSuccess, collectedData)

    // perform error check
    expectError?.let {
      assertThat(errorStates).containsExactly(it)
    } ?: assertThat(errorStates).isEmpty()

    // perform loading check
    if (checkLoading) {
      checkEventLoading(collectedLoadingStates, expectedSuccess, expectError)
    }

    verifyBlock()
  }

  private fun <T> checkEventSuccess(
    liveData: LiveData<T>,
    expectedSuccess: T? = null,
    collectedData: MutableList<T>,
  ) {
    expectedSuccess?.let { expected ->
      assertThat(collectedData).hasSize(1)
      val actual = collectedData.first()

      // special handling for Event objects
      if (expected is Event<*> && actual is Event<*>) {
        assertThat(actual.getContentIfNotHandled()).isEqualTo(expected.getContentIfNotHandled())
        val currentValue = liveData.value as Event<*>
        assertThat(currentValue.peekContent()).isEqualTo(expected.peekContent())
      } else if (actual is Event<*>) {
        assertThat(actual.getContentIfNotHandled()).isEqualTo(expected)
        val currentValue = liveData.value as Event<*>
        assertThat(currentValue.peekContent()).isEqualTo(expected)
      } else {
        assertThat(collectedData).containsExactly(expected)
        assertThat(liveData.value).isEqualTo(expected)
      }
    } ?: assertThat(collectedData).isEmpty()
  }

  private fun <T> checkEventLoading(
    collectedLoadingStates: MutableList<Boolean>,
    expectedSuccess: T? = null,
    expectError: String? = null,
  ) {
    when {
      expectedSuccess != null -> {
        // success case: should start loading, then stop
        assertThat(collectedLoadingStates).contains(true)
        assertThat(collectedLoadingStates.last()).isFalse()
        assertThat(viewModel.loadingState.value).isFalse()
      }

      expectError != null -> {
        // error case: should start loading, then stop
        assertThat(collectedLoadingStates).contains(true)
        assertThat(collectedLoadingStates.last()).isFalse()
        assertThat(viewModel.loadingState.value).isFalse()
      }

      else -> {
        // loading only case
        if (collectedLoadingStates.isNotEmpty()) {
          assertThat(collectedLoadingStates).contains(true)
        }
      }
    }
  }

  private class TestListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
      /* do nothing */
    }

    override fun onRemoved(position: Int, count: Int) {
      /* do nothing */
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      /* do nothing */
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
      /* do nothing */
    }
  }
}
