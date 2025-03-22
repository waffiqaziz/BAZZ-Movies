package com.waffiq.bazz_movies.feature.search.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.ResultsItemSearchResponse
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.QUERY
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.differ
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.resultsItemSearchResponse
import com.waffiq.bazz_movies.feature.search.testutils.SearchTestVariables.resultsItemSearchResponse2
import com.waffiq.bazz_movies.feature.search.utils.SearchMapper.toResultItemSearch
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
    resultsItemSearchResponse.copy(backdropPath = null, posterPath = null, profilePath = null),

    // one path present, others null
    resultsItemSearchResponse.copy(
      backdropPath = "/path.jpg",
      posterPath = null,
      profilePath = null
    ),
    resultsItemSearchResponse.copy(
      backdropPath = null,
      posterPath = "/path.jpg",
      profilePath = null
    ),
    resultsItemSearchResponse.copy(
      backdropPath = null,
      posterPath = null,
      profilePath = "/path.jpg"
    ),

    // two paths present, one null
    resultsItemSearchResponse.copy(
      backdropPath = "/path.jpg",
      posterPath = "/path.jpg",
      profilePath = null
    ),
    resultsItemSearchResponse.copy(
      backdropPath = "/path.jpg",
      posterPath = null,
      profilePath = "/path.jpg"
    ),
    resultsItemSearchResponse.copy(
      backdropPath = null,
      posterPath = "/path.jpg",
      profilePath = "/path.jpg"
    ),

    // all paths not null
    resultsItemSearchResponse.copy(
      backdropPath = "/path.jpg",
      posterPath = "/path.jpg",
      profilePath = "/path.jpg"
    ),

    // all empty - should be filtered out
    resultsItemSearchResponse.copy(backdropPath = "", posterPath = "", profilePath = "")
  ).map { it.toResultItemSearch() }

  private val mockRepository: ISearchRepository = mockk()
  private lateinit var multiSearchInteractor: MultiSearchInteractor

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    multiSearchInteractor = MultiSearchInteractor(mockRepository)
  }

  @Test
  fun search_validValue_returnDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          resultsItemSearchResponse.copy(
            backdropPath = "/backdrop_path0.jpg",
            posterPath = "/poster_path0.jpg",
            profilePath = "/profile_path0.jpg"
          ).toResultItemSearch(),
          resultsItemSearchResponse2.copy(
            backdropPath = "/backdrop_path1.jpg",
            posterPath = "/poster_path1.jpg",
            profilePath = "/profile_path1.jpg"
          ).toResultItemSearch()
        )
      )
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // Assert on the actual items
      val pagingList = differ.snapshot().items // this is the data you wanted
      assertTrue(pagingList.isNotEmpty())
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
      job.cancel()

      awaitComplete()
    }
    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_picturePathNull_returnNull() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          resultsItemSearchResponse.toResultItemSearch(),
          resultsItemSearchResponse2.toResultItemSearch()
        )
      )
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // assert on the actual items
      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isEmpty())
      job.cancel()

      awaitComplete()
    }
    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_backdropNull_returnDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          resultsItemSearchResponse.copy(
            posterPath = "/poster_path0.jpg",
            profilePath = "/profile_path0.jpg"
          ).toResultItemSearch(),
          resultsItemSearchResponse2.copy(
            posterPath = "/poster_path1.jpg",
            profilePath = "/profile_path1.jpg"
          ).toResultItemSearch()
        )
      )
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // Assert on the actual items
      val pagingList = differ.snapshot().items // this is the data you wanted
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path0.jpg", pagingList[0].posterPath)
      assertEquals("/profile_path0.jpg", pagingList[0].profilePath)
      assertTrue(pagingList[1].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path1.jpg", pagingList[1].posterPath)
      assertEquals("/profile_path1.jpg", pagingList[1].profilePath)
      job.cancel()

      awaitComplete()
    }
    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_posterNull_returnDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          resultsItemSearchResponse.copy(
            backdropPath = "/backdrop_path0.jpg",
            profilePath = "/profile_path0.jpg"
          ).toResultItemSearch(),
          resultsItemSearchResponse2.copy(
            backdropPath = "/backdrop_path1.jpg",
            profilePath = "/profile_path1.jpg"
          ).toResultItemSearch()
        )
      )
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // Assert on the actual items
      val pagingList = differ.snapshot().items // this is the data you wanted
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertEquals("/backdrop_path0.jpg", pagingList[0].backdropPath)
      assertEquals("/profile_path0.jpg", pagingList[0].profilePath)
      assertTrue(pagingList[1].posterPath.isNullOrEmpty())
      assertEquals("/backdrop_path1.jpg", pagingList[1].backdropPath)
      assertEquals("/profile_path1.jpg", pagingList[1].profilePath)
      job.cancel()

      awaitComplete()
    }
    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_profileNull_shouldReturnDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(
        listOf(
          resultsItemSearchResponse.copy(
            backdropPath = "/backdrop_path0.jpg",
            posterPath = "/poster_path0.jpg",
          ).toResultItemSearch(),
          resultsItemSearchResponse2.copy(
            backdropPath = "/backdrop_path1.jpg",
            posterPath = "/poster_path1.jpg",
          ).toResultItemSearch()
        )
      )
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem() // Collect first item
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // Assert on the actual items
      val pagingList = differ.snapshot().items // this is the data you wanted
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
      assertEquals("/backdrop_path0.jpg", pagingList[0].backdropPath)
      assertEquals("/poster_path0.jpg", pagingList[0].posterPath)
      assertTrue(pagingList[1].profilePath.isNullOrEmpty())
      assertEquals("/backdrop_path1.jpg", pagingList[1].backdropPath)
      assertEquals("/poster_path1.jpg", pagingList[1].posterPath)
      job.cancel()

      awaitComplete()
    }
    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_backdropPathValid_showBackdrop() = runTest {
    // case 1: only backdropPath
    val fakePagingData1 = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = "/backdrop_path0.jpg",
          posterPath = null,
          profilePath = null
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData1)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isNotEmpty())
      assertEquals("/backdrop_path0.jpg", pagingList[0].backdropPath)
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_posterPathValid_showPoster() = runTest {
    // case 2: only posterPath
    val fakePagingData2 = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = null,
          posterPath = "/poster_path0.jpg",
          profilePath = null
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData2)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertEquals("/poster_path0.jpg", pagingList[0].posterPath)
      assertTrue(pagingList[0].profilePath.isNullOrEmpty())
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_profilePathValid_showProfile() = runTest {
    // case 3: only profilePath
    val fakePagingData3 = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = null,
          posterPath = null,
          profilePath = "/profile_path0.jpg"
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData3)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isNotEmpty())
      assertTrue(pagingList[0].backdropPath.isNullOrEmpty())
      assertTrue(pagingList[0].posterPath.isNullOrEmpty())
      assertEquals("/profile_path0.jpg", pagingList[0].profilePath)
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_allEmpty_returnEmptyList() = runTest {
    // case 1: empty strings (not null)
    val fakePagingDataEmpty = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = "",
          posterPath = "",
          profilePath = ""
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingDataEmpty)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isEmpty()) // Should be filtered out
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_mixedValid_returnEmptyList() = runTest {
    // case 2: one path is empty string, one is null, one has content
    val fakePagingDataMixed = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = "",
          posterPath = null,
          profilePath = "/profile_path0.jpg"
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingDataMixed)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isNotEmpty())
      assertEquals("", pagingList[0].backdropPath)
      assertNull(pagingList[0].posterPath)
      assertEquals("/profile_path0.jpg", pagingList[0].profilePath)
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_mixedEmpty_returnEmptyList() = runTest {
    // case 3: all are empty or null but in different ways
    val fakePagingDataVariedEmpty = PagingData.from(
      listOf(
        resultsItemSearchResponse.copy(
          backdropPath = "",
          posterPath = null,
          profilePath = ""
        ).toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingDataVariedEmpty)
    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertTrue(pagingList.isEmpty())
      job.cancel()

      awaitComplete()
    }

    verify(exactly = 1) { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun search_nullValue_returnEmptyList() = runTest {
    val fakePagingData = PagingData.from(
      listOf(
        ResultsItemSearchResponse().toResultItemSearch(),
        ResultsItemSearchResponse().toResultItemSearch()
      )
    )

    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertEquals(emptyList<ResultsItemSearch>(), pagingList)

      job.cancel()
      awaitComplete()
    }

    verify { mockRepository.getPagingSearch(QUERY) }
  }

  @Test
  fun filter_allPathCombinations_ensureProperFilteringLogic() = runTest {
    // total 9 elements - should only left 7 after filtering
    val fakePagingData = PagingData.from(testCases)
    every { mockRepository.getPagingSearch(QUERY) } returns flowOf(fakePagingData)

    multiSearchInteractor.search(QUERY).test {
      val pagingData = awaitItem()
      val job = launch { differ.submitData(pagingData) }
      advanceUntilIdle()

      // get the filtered items
      val pagingList = differ.snapshot().items

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

      job.cancel()
      awaitComplete()
    }

    verify { mockRepository.getPagingSearch(QUERY) }
  }
}
