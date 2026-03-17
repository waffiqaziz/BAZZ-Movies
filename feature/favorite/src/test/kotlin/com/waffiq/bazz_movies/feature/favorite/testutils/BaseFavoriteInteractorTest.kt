package com.waffiq.bazz_movies.feature.favorite.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowAwaitComplete
import com.waffiq.bazz_movies.feature.favorite.domain.repository.IFavoriteRepository
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

abstract class BaseFavoriteInteractorTest : ShouldSpec() {

  protected lateinit var repository: IFavoriteRepository

  init {
    beforeTest {
      repository = mockk(relaxed = true)
      Dispatchers.setMain(UnconfinedTestDispatcher())
    }
  }

  suspend fun testPagingFlow(
    flow: Flow<PagingData<MediaItem>>,
    expectedAssertions: (List<MediaItem>) -> Unit,
  ) {
    testPagingFlowAwaitComplete(flow) {
      expectedAssertions(it)
    }
  }
}
