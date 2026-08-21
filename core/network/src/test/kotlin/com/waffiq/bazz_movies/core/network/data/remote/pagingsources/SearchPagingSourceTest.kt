package com.waffiq.bazz_movies.core.network.data.remote.pagingsources

import androidx.paging.PagingSource
import com.waffiq.bazz_movies.core.common.MediaType
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.search.MultiSearchResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.retrofit.services.SearchApiService
import com.waffiq.bazz_movies.core.network.testutils.DummyData.movieMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.DummyData.multiMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.DummyData.personMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.DummyData.tvMediaTypeSet
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsErrorOnException
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsErrorOnHttpException
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPage
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testLoadReturnsPageWithNullNextKeyOnEmptyResponse
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyAllKeysNull
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyEmptyList
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyUsesCorrectKey
import com.waffiq.bazz_movies.core.network.testutils.PagingSourceTestHelper.testRefreshKeyWithAnchorInMiddlePage
import com.waffiq.bazz_movies.core.network.utils.common.Constants.INITIAL_PAGE_INDEX
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SearchPagingSourceTest {

  private val mockSearchApiService: SearchApiService = mockk()
  private val query = "test query"

  @Test
  fun fetchPage_withSuccessfulApiCall_returnsCorrectPageData() {
    val mediaItems = listOf(
      MultiSearchResponseItem(mediaType = "person"),
      MultiSearchResponseItem(mediaType = "person"),
    )

    testLoadReturnsPage(
      pagingSourceFactory = {
        SearchPagingSource(mockSearchApiService, query, personMediaTypeSet)
      },
      setupMock = {
        coEvery { mockSearchApiService.searchPerson(query, INITIAL_PAGE_INDEX) } returns
          MultiSearchResponse(results = mediaItems, totalPages = 2)
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedData = mediaItems,
      expectedPrevKey = null,
      expectedNextKey = INITIAL_PAGE_INDEX + 1,
    )
  }

  @Test
  fun fetchPage_withIOException_returnsError() {
    testLoadReturnsErrorOnException(
      pagingSourceFactory = {
        SearchPagingSource(mockSearchApiService, query, personMediaTypeSet)
      },
      setupMock = {
        coEvery { mockSearchApiService.searchPerson(query, INITIAL_PAGE_INDEX) } throws
          IOException()
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedException = IOException::class.java,
    )
  }

  @Test
  fun fetchPage_withHttpException_returnsError() {
    val response: Response<Any> =
      Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
    val exception = HttpException(response)

    testLoadReturnsErrorOnHttpException(
      pagingSourceFactory = {
        SearchPagingSource(mockSearchApiService, query, personMediaTypeSet)
      },
      setupMock = {
        coEvery { mockSearchApiService.searchPerson(query, INITIAL_PAGE_INDEX) } throws exception
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedMessage = "HTTP 500 Response.error()",
    )
  }

  @Test
  fun fetchPage_withNullResponseData_returnsError() =
    runTest {
      coEvery { mockSearchApiService.searchPerson(query, INITIAL_PAGE_INDEX) } returns
        MultiSearchResponse(results = null)

      val pagingSource = SearchPagingSource(mockSearchApiService, query, personMediaTypeSet)
      val result = pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))

      if (result is PagingSource.LoadResult.Error) {
        assertTrue(result.throwable is Exception)
        assertEquals(result.throwable.message, "Response data is null")
      } else {
        fail("Expected LoadResult.Error but got $result")
      }
    }

  @Test
  fun fetchPage_whenTypeReachesTotalPages_excludesTypeFromSubsequentLoad() =
    runTest {
      val movieItemsPage1 = listOf(MultiSearchResponseItem(mediaType = "movie"))
      val tvItemsPage1 = listOf(MultiSearchResponseItem(mediaType = "tv"))
      val tvItemsPage2 = listOf(MultiSearchResponseItem(mediaType = "tv"))

      // setup with movie only has 1 page total and tv has 2
      coEvery { mockSearchApiService.searchMovies(query, INITIAL_PAGE_INDEX) } returns
        MultiSearchResponse(results = movieItemsPage1, totalPages = 1)
      coEvery { mockSearchApiService.searchTv(query, INITIAL_PAGE_INDEX) } returns
        MultiSearchResponse(results = tvItemsPage1, totalPages = 2)
      coEvery { mockSearchApiService.searchTv(query, 2) } returns
        MultiSearchResponse(results = tvItemsPage2, totalPages = 2)

      val pagingSource = SearchPagingSource(
        mockSearchApiService,
        query,
        setOf(MediaType.MOVIE, MediaType.TV),
      )

      // totalPagesByType is empty, known == null
      pagingSource.load(PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false))

      // known != null for both now,  comparison decides who stays active
      // movie: position(2) <= known(1) -> false -> excluded
      // tv:    position(2) <= known(2) -> true  -> included
      val result = pagingSource.load(PagingSource.LoadParams.Refresh(2, 2, false))

      val pageResult = result as PagingSource.LoadResult.Page
      assertEquals(tvItemsPage2, pageResult.data)

      coVerify(exactly = 0) { mockSearchApiService.searchMovies(query, 2) }
      coVerify(exactly = 1) { mockSearchApiService.searchTv(query, 2) }
    }

  @Test
  fun search_withMultiMediaType_returnsCorrectPageData() {
    val mediaItems = listOf(
      MultiSearchResponseItem(mediaType = "multi"),
      MultiSearchResponseItem(mediaType = "multi"),
    )

    testLoadReturnsPage(
      pagingSourceFactory = {
        SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet)
      },
      setupMock = {
        coEvery { mockSearchApiService.searchMulti(query, INITIAL_PAGE_INDEX) } returns
          MultiSearchResponse(results = mediaItems, totalPages = 2)
      },
      params = PagingSource.LoadParams.Refresh(INITIAL_PAGE_INDEX, 2, false),
      expectedData = mediaItems,
      expectedPrevKey = null,
      expectedNextKey = INITIAL_PAGE_INDEX + 1,
    )
  }

  @Test
  fun loadPage_onSubsequentPage_returnPageWithNonNullPrevKey() {
    val mediaItems = listOf(
      MultiSearchResponseItem(mediaType = "movie"),
      MultiSearchResponseItem(mediaType = "movie"),
    )

    testLoadReturnsPageWithNonNullPrevKeyOnSubsequentPage(
      pagingSourceFactory = {
        SearchPagingSource(mockSearchApiService, query, movieMediaTypeSet)
      },
      setupMock = { page ->
        coEvery { mockSearchApiService.searchMovies(query, page) } returns
          MultiSearchResponse(results = mediaItems, totalPages = 2)
      },
      page = 2,
      expectedData = mediaItems,
      expectedPrevKey = 1,
      expectedNextKey = null,
    )
  }

  @Test
  fun loadPage_withEmptyResponse_returnPageWithNullNextKey() {
    testLoadReturnsPageWithNullNextKeyOnEmptyResponse(
      pagingSourceFactory = { SearchPagingSource(mockSearchApiService, query, tvMediaTypeSet) },
      setupMock = { page ->
        coEvery { mockSearchApiService.searchTv(query, page) } returns
          MultiSearchResponse(results = emptyList())
      },
      expectedData = emptyList(),
    )
  }

  @Test
  fun getRefreshKey_whenAnchorInMiddlePage_returnsCorrectKey() {
    testRefreshKeyWithAnchorInMiddlePage(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      data = listOf(
        listOf(MultiSearchResponseItem("item1"), MultiSearchResponseItem("item2")),
        listOf(MultiSearchResponseItem("item3"), MultiSearchResponseItem("item4")),
      ),
      prevKeys = listOf(null, 1),
      nextKeys = listOf(2, 3),
      anchorPosition = 2, // Anchor in the middle of the second page
      expectedRefreshKey = 2, // Expected nextKey of the first page
    )
  }

  @Test
  fun getRefreshKey_whenBothKeysPresent_shouldUsePrevKeyPlusOne() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      data = listOf(MultiSearchResponseItem("item1")),
      anchorPosition = 0,
      prevKey = 1,
      nextKey = 3,
      expectedRefreshKey = 2, // prevKey + 1
    )
  }

  @Test
  fun getRefreshKey_whenPrevKeyIsNull_shouldUseNextKeyMinusOne() {
    testRefreshKeyUsesCorrectKey(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      data = listOf(MultiSearchResponseItem("item1")),
      anchorPosition = 0,
      prevKey = null,
      nextKey = 3,
      expectedRefreshKey = 2, // nextKey - 1
    )
  }

  @Test
  fun getRefreshKey_whenBothKeysAreNull_returnsNull() {
    testRefreshKeyAllKeysNull(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      data = listOf(MultiSearchResponseItem("item1")),
    )
  }

  @Test
  fun getRefreshKey_whenNoAnchorPositionAndEmptyPages_returnsNull() {
    testRefreshKeyEmptyList(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      anchorPosition = null,
    )
  }

  @Test
  fun getRefreshKey_whenAnchorIsZeroAndEmptyPages_returnsNull() {
    testRefreshKeyEmptyList(
      pagingSource = SearchPagingSource(mockSearchApiService, query, multiMediaTypeSet),
      anchorPosition = 0,
    )
  }
}
