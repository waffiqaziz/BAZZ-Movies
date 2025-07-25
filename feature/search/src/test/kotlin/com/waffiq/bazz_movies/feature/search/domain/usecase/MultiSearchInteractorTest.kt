package com.waffiq.bazz_movies.feature.search.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.MultiSearchItem
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.QUERY
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponseItem
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.multiSearchResponseItem2
import com.waffiq.bazz_movies.feature.search.utils.SearchMapper.toMultiSearchItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiSearchInteractorTest {
  private val testCases = listOf(
    // all null - should be filtered out
    multiSearchResponseItem.copy(backdropPath = null, posterPath = null, profilePath = null),

    // one path present, others null
    multiSearchResponseItem.copy(
      backdropPath = "1/path.jpg",
      posterPath = null,
      profilePath = null
    ),
    multiSearchResponseItem.copy(
      backdropPath = null,
      posterPath = "2/path.jpg",
      profilePath = null
    ),
    multiSearchResponseItem.copy(
      backdropPath = null,
      posterPath = null,
      profilePath = "3/path.jpg"
    ),

    // two paths present, one null
    multiSearchResponseItem.copy(
      backdropPath = "4/path.jpg",
      posterPath = "4/path.jpg",
      profilePath = null
    ),
    multiSearchResponseItem.copy(
      backdropPath = "5/path.jpg",
      posterPath = null,
      profilePath = "5/path.jpg"
    ),
    multiSearchResponseItem.copy(
      backdropPath = null,
      posterPath = "6/path.jpg",
      profilePath = "6/path.jpg"
    ),

    // all paths not null
    multiSearchResponseItem.copy(
      backdropPath = "7/path.jpg",
      posterPath = "7/path.jpg",
      profilePath = "7/path.jpg"
    ),

    // all empty - should be filtered out
    multiSearchResponseItem.copy(backdropPath = "", posterPath = "", profilePath = "")
  ).map { it.toMultiSearchItem() }

  private val mockRepository: ISearchRepository = mockk()
  private lateinit var multiSearchInteractor: MultiSearchInteractor

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    multiSearchInteractor = MultiSearchInteractor(mockRepository)
  }

  private fun mockSearchPagingData(pagingData: PagingData<MultiSearchItem>) {
    every { mockRepository.search(QUERY) } returns flowOf(pagingData)
  }

  private fun verifyPagingSearch() {
    verify { mockRepository.search(QUERY) }
  }

  /**
   * Helper function to run a test with a specific paging data and execute assertions on the results
   */
  private fun testSearchWithPagingData(
    pagingData: PagingData<MultiSearchItem>,
    assertions: (List<MultiSearchItem>) -> Unit,
  ) = runTest {
    mockSearchPagingData(pagingData)

    multiSearchInteractor.search(QUERY).test {
      val actualPagingData = awaitItem() // collect first item
      val job = launch { differ.submitData(actualPagingData) }
      advanceUntilIdle()

      // get the filtered items
      val pagingList = differ.snapshot().items

      // run the provided assertions
      assertions(pagingList)

      job.cancel()
      awaitComplete()
    }

    verifyPagingSearch()
  }

  @Test
  fun search_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          multiSearchResponseItem.copy(
            backdropPath = "/backdrop_path0.jpg",
            posterPath = "/poster_path0.jpg",
            profilePath = "/profile_path0.jpg"
          ).toMultiSearchItem(),
          multiSearchResponseItem2.copy(
            backdropPath = "/backdrop_path1.jpg",
            posterPath = "/poster_path1.jpg",
            profilePath = "/profile_path1.jpg"
          ).toMultiSearchItem()
        )
      )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertEquals("Transformers TV-series", pagingList[0].title)
      assertEquals(TV_MEDIA_TYPE, pagingList[0].mediaType)
      assertEquals(12345, pagingList[0].id)
      assertEquals(2222.0, pagingList[0].voteCount)
      assertEquals("/backdrop_path0.jpg", pagingList[0].backdropPath)
      assertEquals("/poster_path0.jpg", pagingList[0].posterPath)
      assertEquals("/profile_path0.jpg", pagingList[0].profilePath)
      assertFalse(pagingList[0].adult)
      assertEquals("Transformers 2", pagingList[1].title)
      assertEquals(MOVIE_MEDIA_TYPE, pagingList[1].mediaType)
      assertEquals(333111, pagingList[1].id)
      assertEquals(3333.0, pagingList[1].voteCount)
      assertEquals("/backdrop_path1.jpg", pagingList[1].backdropPath)
      assertEquals("/poster_path1.jpg", pagingList[1].posterPath)
      assertEquals("/profile_path1.jpg", pagingList[1].profilePath)
      assertFalse(pagingList[1].adult)
    }
  }

  @Test
  fun search_whenPicturePathIsNull_returnsEmptyPage() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          multiSearchResponseItem.toMultiSearchItem(),
          multiSearchResponseItem2.toMultiSearchItem()
        )
      )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertTrue(pagingList.isEmpty())
    }
  }

  @Test
  fun search_whenBackdropPathIsNull_returnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          multiSearchResponseItem.copy(
            posterPath = "/poster_path3.jpg",
            profilePath = "/profile_path3.jpg"
          ).toMultiSearchItem(),
          multiSearchResponseItem2.copy(
            posterPath = "/poster_path4.jpg",
            profilePath = "/profile_path4.jpg"
          ).toMultiSearchItem()
        )
      )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path3.jpg", pagingList[0].posterPath)
      assertEquals("/profile_path3.jpg", pagingList[0].profilePath)
      assertTrue(pagingList[1].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path4.jpg", pagingList[1].posterPath)
      assertEquals("/profile_path4.jpg", pagingList[1].profilePath)
    }
  }

  @Test
  fun search_whenPosterPathIsNull_returnDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          multiSearchResponseItem.copy(
            backdropPath = "/backdrop_path5.jpg",
            profilePath = "/profile_path5.jpg"
          ).toMultiSearchItem(),
          multiSearchResponseItem2.copy(
            backdropPath = "/backdrop_path6.jpg",
            profilePath = "/profile_path6.jpg"
          ).toMultiSearchItem()
        )
      )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertEquals("/backdrop_path5.jpg", pagingList[0].backdropPath)
      assertEquals("/profile_path5.jpg", pagingList[0].profilePath)
      assertTrue(pagingList[1].posterPath.isNullOrEmpty())
      assertEquals("/backdrop_path6.jpg", pagingList[1].backdropPath)
      assertEquals("/profile_path6.jpg", pagingList[1].profilePath)
    }
  }

  @Test
  fun search_whenProfilePathIsNull_returnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          multiSearchResponseItem.copy(
            backdropPath = "/backdrop_path7.jpg",
            posterPath = "/poster_path7.jpg",
          ).toMultiSearchItem(),
          multiSearchResponseItem2.copy(
            backdropPath = "/backdrop_path8.jpg",
            posterPath = "/poster_path8.jpg",
          ).toMultiSearchItem()
        )
      )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
      assertEquals("/backdrop_path7.jpg", pagingList[0].backdropPath)
      assertEquals("/poster_path7.jpg", pagingList[0].posterPath)
      assertTrue(pagingList[1].profilePath.isNullOrEmpty())
      assertEquals("/backdrop_path8.jpg", pagingList[1].backdropPath)
      assertEquals("/poster_path8.jpg", pagingList[1].posterPath)
    }
  }

  @Test
  fun search_whenBackdropPathIsValid_showsBackdrop() = runTest {
    // case 1: only backdropPath
    val fakePagingData1 = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = "/backdrop_path9.jpg",
          posterPath = null,
          profilePath = null
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingData1) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertEquals("/backdrop_path9.jpg", pagingList[0].backdropPath)
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
    }
  }

  @Test
  fun search_whenPosterPathIsValid_showsPoster() = runTest {
    // case 2: only posterPath
    val fakePagingData2 = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = null,
          posterPath = "/poster_path10.jpg",
          profilePath = null
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingData2) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path10.jpg", pagingList[0].posterPath)
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
    }
  }

  @Test
  fun search_whenProfilePathIsValid_showsProfile() = runTest {
    // case 3: only profilePath
    val fakePagingData3 = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = null,
          posterPath = null,
          profilePath = "/profile_path11.jpg"
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingData3) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertEquals("/profile_path11.jpg", pagingList[0].profilePath)
    }
  }

  @Test
  fun search_whenAllDataIsEmpty_returnsEmptyList() = runTest {
    // case 1: empty strings (not null)
    val fakePagingDataEmpty = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = "",
          posterPath = "",
          profilePath = ""
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingDataEmpty) { pagingList ->
      assertTrue(pagingList.isEmpty()) // Should be filtered out
    }
  }

  @Test
  fun search_whenMixedValidValue_returnsEmptyList() = runTest {
    // case 2: one path is empty string, one is null, one has content
    val fakePagingDataMixed = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = "",
          posterPath = null,
          profilePath = "/profile_path12.jpg"
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingDataMixed) { pagingList ->
      assertTrue(pagingList.isNotEmpty())
      assertEquals("", pagingList[0].backdropPath)
      assertNull(pagingList[0].posterPath)
      assertEquals("/profile_path12.jpg", pagingList[0].profilePath)
    }
  }

  @Test
  fun search_whenMixedEmpty_returnsEmptyList() = runTest {
    // case 3: all are empty or null but in different ways
    val fakePagingDataVariedEmpty = PagingData.from(
      listOf(
        multiSearchResponseItem.copy(
          backdropPath = "",
          posterPath = null,
          profilePath = ""
        ).toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingDataVariedEmpty) { pagingList ->
      assertTrue(pagingList.isEmpty())
    }
  }

  @Test
  fun search_whenAllDataIsNull_returnsEmptyList() = runTest {
    val fakePagingData = PagingData.from(
      listOf(
        MultiSearchResponseItem().toMultiSearchItem(),
        MultiSearchResponseItem().toMultiSearchItem()
      )
    )

    testSearchWithPagingData(fakePagingData) { pagingList ->
      assertEquals(emptyList<MultiSearchItem>(), pagingList)
    }
  }

  @Test
  fun filter_withPathCombinations_ensureProperFilteringLogic() = runTest {
    // total 9 elements - should only left 7 after filtering
    val fakePagingData = PagingData.from(testCases)

    testSearchWithPagingData(fakePagingData) { pagingList ->

      // should have filtered out 2 items (all null and all empty)
      assertEquals(7, pagingList.size)

      // verify that all items in the result have at least one path that is non-empty
      pagingList.forEach { item ->
        assertTrue(
          !item.backdropPath.isNullOrEmpty() ||
            !item.posterPath.isNullOrEmpty() ||
            !item.profilePath.isNullOrEmpty()
        )
      }
    }
  }
}
