package com.waffiq.bazz_movies.feature.detail.testutils

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.network.data.remote.datasource.MovieDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.test.UnconfinedDispatcherRule
import com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
import io.mockk.mockk
import org.junit.Rule
import kotlin.test.BeforeTest

abstract class BaseDetailRepositoryImplTest {

  protected lateinit var repository: DetailRepositoryImpl
  protected val movieDataSource: MovieDataSource = mockk()
  protected val id = 1
  protected val idString = "tt12345"

  @get:Rule
  val mainDispatcherRule = UnconfinedDispatcherRule()

  @BeforeTest
  fun setUp() {
    repository = DetailRepositoryImpl(movieDataSource)
  }

  /**
   * Creates sample test data for paging tests
   */
  protected fun createSampleMediaItemResponse(
    id: Int = 1,
    name: String = "Test Name",
  ): MediaResponseItem = MediaResponseItem(id = id, name = name)

  /**
   * Creates paging data with sample items
   */
  protected fun createSamplePagingData(
    vararg items: MediaResponseItem,
  ): PagingData<MediaResponseItem> = PagingData.from(items.toList())
}
