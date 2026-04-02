package com.waffiq.bazz_movies.core.utils

import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.test.LifecycleOwnerRule
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.core.utils.testutils.FakePagingAdapter
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CollectAndSubmitDataTest {

  val samplePagingData = PagingData.from(listOf("Item 1", "Item 2"))

  @get:Rule
  val lifecycleOwnerRule = LifecycleOwnerRule()

  private val adapter = mockk<PagingDataAdapter<String, RecyclerView.ViewHolder>>(relaxed = true)
  private val pagingDataFlow = MutableStateFlow<PagingData<String>>(PagingData.empty())

  @Test
  fun collectAndSubmitData_whenLifecycleCreated_shouldSubmitPagingData() = runTest {
    val mockFragment = mockk<Fragment>(relaxed = true) {
      every { viewLifecycleOwner } returns lifecycleOwnerRule.lifecycleOwner
    }

    collectAndSubmitData(
      fragment = mockFragment,
      flowProvider = { pagingDataFlow },
      adapter = adapter
    )

    pagingDataFlow.value = samplePagingData
    advanceUntilIdle()

    // verify submitData was called with correct data
    verify(atLeast = 1) { adapter.submitData(lifecycleOwnerRule.lifecycleOwner.lifecycle, any()) }
  }

  @Test
  fun collectAndSubmitData_whenFlowIsEmpty_shouldNotSubmitData() = runTest {
    val mockFragment = mockk<Fragment>(relaxed = true) {
      every { viewLifecycleOwner } returns lifecycleOwnerRule.lifecycleOwner
    }

    val emptyFlowProvider: () -> Flow<PagingData<String>> = { emptyFlow() }

    collectAndSubmitData(
      fragment = mockFragment,
      flowProvider = emptyFlowProvider,
      adapter = adapter
    )
    advanceUntilIdle()

    verify(exactly = 0) { adapter.submitData(any(), any()) }
  }

  @Test
  fun collectAndSubmitData_whenRunningSimultaneously_shouldHandleMultipleEmissions() = runTest {
    val mockFragment = mockk<Fragment>(relaxed = true) {
      every { viewLifecycleOwner } returns lifecycleOwnerRule.lifecycleOwner
    }

    val pagingDataFlow = MutableSharedFlow<PagingData<String>>()
    val flowProvider: () -> Flow<PagingData<String>> = { pagingDataFlow }

    collectAndSubmitData(
      fragment = mockFragment,
      flowProvider = flowProvider,
      adapter = adapter
    )

    val firstPagingData = PagingData.from(listOf("Item 1"))
    pagingDataFlow.emit(firstPagingData)

    val secondPagingData = PagingData.from(listOf("Item 2", "Item 3"))
    pagingDataFlow.emit(secondPagingData)
    advanceUntilIdle()

    verify(exactly = 2) { adapter.submitData(any(), any()) }
  }

  @Test
  fun collectAndSubmitData_withLifecycle_shouldSubmitPagingData() = runTest {
    val mockFragment = mockk<Fragment>(relaxed = true) {
      every { viewLifecycleOwner } returns lifecycleOwnerRule.lifecycleOwner
    }

    collectAndSubmitData(
      lifecycleOwner = mockFragment.viewLifecycleOwner,
      flowProvider = { pagingDataFlow },
      adapter = adapter,
    )

    pagingDataFlow.value = samplePagingData
    advanceUntilIdle()

    coVerify(atLeast = 1) { adapter.submitData(any()) }
  }

  @Test
  fun collectAndSubmitData_whenLifecycleCreated_shouldContainExpectedItems() = runTest {
    Dispatchers.setMain(StandardTestDispatcher(testScheduler))

    val fakeAdapter = FakePagingAdapter()

    val mockFragment = mockk<Fragment>(relaxed = true) {
      every { viewLifecycleOwner } returns lifecycleOwnerRule.lifecycleOwner
    }

    collectAndSubmitData(
      fragment = mockFragment,
      flowProvider = { pagingDataFlow },
      adapter = fakeAdapter
    )

    pagingDataFlow.value = PagingData.from(listOf("Item 1", "Item 2"))
    advanceUntilIdle()

    assertEquals(listOf("Item 1", "Item 2"), fakeAdapter.snapshot().items)

    Dispatchers.resetMain()
  }
}
