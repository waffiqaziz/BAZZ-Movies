package com.waffiq.bazz_movies.feature.search.data.repository

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.common.value
import com.waffiq.bazz_movies.core.network.data.remote.datasource.search.SearchRemoteDataSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.testutils.DumyData.multiSearchResponseItem
import com.waffiq.bazz_movies.feature.search.testutils.DumyData.multiSearchResponseItem2
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val mockSearchRemoteDataSource: SearchRemoteDataSource = mockk()
  private lateinit var repository: SearchRepositoryImpl

  @Before
  fun setUp() {
    repository = SearchRepositoryImpl(mockSearchRemoteDataSource)
  }

  @Test
  fun search_whenSuccessful_returnsDataCorrectly() =
    runTest {
      val filters = setOf(MediaType.MULTI)
      val fakePagingData =
        PagingData.from(listOf(multiSearchResponseItem, multiSearchResponseItem2))

      every { mockSearchRemoteDataSource.search(QUERY, filters) } returns flowOf(fakePagingData)

      val items: List<MultiSearchItem> = repository.search(QUERY, filters).asSnapshot()

      assertTrue(items.isNotEmpty())
      assertEquals(2, items.size)
      verify { mockSearchRemoteDataSource.search(QUERY, filters) }
    }

  @Test
  fun search_whenEmpty_returnsEmptyPagedData() =
    runTest {
      val filters = setOf(MediaType.MOVIE)
      val emptyPagingData = PagingData.from(emptyList<MultiSearchResponseItem>())

      every { mockSearchRemoteDataSource.search(QUERY, filters) } returns flowOf(emptyPagingData)

      val items: List<MultiSearchItem> = repository.search(QUERY, filters).asSnapshot()

      assertTrue(items.isEmpty())
      verify { mockSearchRemoteDataSource.search(QUERY, filters) }
    }

  @Test
  fun search_withSingleSpecificFilter_passesKnownTypeToMapping() =
    runTest {
      val filters = setOf(MediaType.MOVIE)
      val fakePagingData = PagingData.from(listOf(multiSearchResponseItem2))

      every { mockSearchRemoteDataSource.search(QUERY, filters) } returns flowOf(fakePagingData)

      val items: List<MultiSearchItem> = repository.search(QUERY, filters).asSnapshot()

      assertEquals(1, items.size)
      // verify 'knownType' (MediaType.MOVIE) was applied
      assertEquals(MediaType.MOVIE.value, items.first().mediaType)
      verify { mockSearchRemoteDataSource.search(QUERY, filters) }
    }

  @Test
  fun search_withSingleMultiFilter_passesNullKnownTypeToMapping() =
    runTest {
      val filters = setOf(MediaType.MULTI)
      val fakePagingData = PagingData.from(listOf(multiSearchResponseItem))

      every { mockSearchRemoteDataSource.search(QUERY, filters) } returns flowOf(fakePagingData)

      val items: List<MultiSearchItem> = repository.search(QUERY, filters).asSnapshot()

      assertEquals(1, items.size)
      verify { mockSearchRemoteDataSource.search(QUERY, filters) }
    }

  @Test
  fun search_withMultipleFilters_passesNullKnownTypeToMapping() =
    runTest {
      val filters = setOf(MediaType.MOVIE, MediaType.TV)
      val fakePagingData = PagingData.from(listOf(multiSearchResponseItem))

      every { mockSearchRemoteDataSource.search(QUERY, filters) } returns flowOf(fakePagingData)

      val items: List<MultiSearchItem> = repository.search(QUERY, filters).asSnapshot()

      assertEquals(1, items.size)
      verify { mockSearchRemoteDataSource.search(QUERY, filters) }
    }

  companion object {
    private const val QUERY = "Transformers"
  }
}
