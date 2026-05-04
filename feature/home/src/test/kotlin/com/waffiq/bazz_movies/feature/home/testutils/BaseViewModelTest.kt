package com.waffiq.bazz_movies.feature.home.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.assertEquals

abstract class BaseViewModelTest {

  protected val testData = MediaItem(title = "title", id = 1234, overview = "overview")
  protected val expectedFlow = flowOf(
    PagingData.from(
      listOf(testData),
    ),
  )
  val testDispatcher = UnconfinedTestDispatcher()

  @Before
  open fun setup() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  suspend fun thenEmitsCorrectItem(
    flowProvider: () -> Flow<PagingData<MediaItem>>,
    expected: MediaItem = testData, // default use tv as expected
  ) {
    PagingFlowHelperTest.testPagingFlowCancelRemaining(flowProvider()) {
      assertEquals(it[0].id, expected.id)
      assertEquals(it[0].title, expected.title)
      assertEquals(it[0].overview, expected.overview)
    }
  }
}
