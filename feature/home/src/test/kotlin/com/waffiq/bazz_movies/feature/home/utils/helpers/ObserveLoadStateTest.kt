package com.waffiq.bazz_movies.feature.home.utils.helpers

import androidx.paging.LoadState
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.feature.home.testutils.BaseFragmentHelperTest
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ObserveLoadStateTest : BaseFragmentHelperTest() {

  @Test
  fun observeLoadState_whenRefreshIsLoadingAndEndOfPaginationReached_callsOnLoading() =
    runTest {
      var loadingCalled = false

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.Loading,
          append = LoadState.NotLoading(endOfPaginationReached = true),
        ),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = { loadingCalled = true },
        onSuccess = {},
        onError = {},
      )

      advanceUntilIdle()
      assert(loadingCalled)

      // refresh=NotLoading,
      // append=NotLoading(false)
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          append = LoadState.Loading,
        ),
      )
      advanceUntilIdle()

      // refresh=Loading
      // endOfPaginationReached=false
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.Loading,
          append = LoadState.NotLoading(endOfPaginationReached = false),
        ),
      )
      advanceUntilIdle()
    }

  @Test
  fun observeLoadState_whenAllStatesAreNotLoading_callsOnSuccess() =
    runTest {
      var successCalled = false

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.NotLoading(false),
        ),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = {},
        onSuccess = { successCalled = true },
        onError = {},
      )

      advanceUntilIdle()
      assert(successCalled)

      // prepend is NOT NotLoading
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.Loading,
          append = LoadState.NotLoading(false),
        ),
      )
      advanceUntilIdle()

      // prepend passes, append is NOT NotLoading
      loadStateFlow.emit(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.Loading,
        ),
      )
      advanceUntilIdle()
    }

  @Test
  fun observeLoadState_whenRefreshIsError_callsOnError() =
    runTest {
      var errorEvent: Event<String>? = null
      val exception = RuntimeException("network error")

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(refresh = LoadState.Error(exception)),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = {},
        onSuccess = {},
        onError = { errorEvent = it },
      )

      advanceUntilIdle()
      assert(errorEvent != null)
    }

  @Test
  fun observeLoadState_whenAppendIsError_doesNotCallOnSuccessOrOnError() =
    runTest {
      var successCalled = false
      var errorEvent: Event<String>? = null

      val loadStateFlow = MutableStateFlow(
        buildCombinedLoadStates(
          refresh = LoadState.NotLoading(false),
          prepend = LoadState.NotLoading(false),
          append = LoadState.Error(RuntimeException("append error")), // append is NOT NotLoading
        ),
      )

      lifecycleOwner.observeLoadState(
        loadStateFlow = loadStateFlow,
        onLoading = {},
        onSuccess = { successCalled = true },
        onError = { errorEvent = it },
      )

      advanceUntilIdle()

      assert(!successCalled) // check if not success
      assert(errorEvent == null)
    }
}
