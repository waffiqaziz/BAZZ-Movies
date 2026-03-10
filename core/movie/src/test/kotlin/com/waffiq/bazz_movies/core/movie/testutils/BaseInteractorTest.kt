package com.waffiq.bazz_movies.core.movie.testutils

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.repository.IMoviesRepository
import com.waffiq.bazz_movies.core.movie.testutils.TestVariables.movieMediaItem
import com.waffiq.bazz_movies.core.test.PagingDataHelperTest.differ
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

abstract class BaseInteractorTest {

  private val differ = differ<MediaItem>()
  protected val mockMovieRepository: IMoviesRepository = mockk()
  protected val fakePagingData =
    PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))
  protected val region = "region"

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  protected fun testPagingData(
    mockCall: () -> Flow<PagingData<MediaItem>>,
    pagingData: PagingData<MediaItem>,
    interactorCall: () -> Flow<PagingData<MediaItem>>,
    assertions: (List<MediaItem>) -> Unit,
  ) = runTest {
    every { mockCall() } returns flowOf(pagingData)

    interactorCall().test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertions(pagingList)

      job.cancel()
      awaitComplete()
    }

    verify { mockCall() }
  }
}
