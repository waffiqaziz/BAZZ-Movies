package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.video
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMovieDetailInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMovieDetailInteractor

  override fun initInteractor() {
    interactor = GetMovieDetailInteractor(mockDetailRepository)
  }

  @Test
  fun getMovieVideoLinks_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getMovieTrailerLink(MOVIE_ID) },
      mockResponse = video,
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    ) { emission ->
      assertEquals("Link Trailer", emission.data)
    }
  }

  @Test
  fun getMovieVideoLinks_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getMovieTrailerLink(MOVIE_ID) },
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieVideoLinks_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getMovieTrailerLink(MOVIE_ID) },
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getMovieCredits(MOVIE_ID) },
      mockResponse = movieCredits,
      interactorCall = { interactor.getMovieCredits(MOVIE_ID) }
    ) { emission ->
      assertEquals(movieCredits, emission.data)
    }
  }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getMovieCredits(MOVIE_ID) },
      interactorCall = { interactor.getMovieCredits(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData = PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))

    testPagingData(
      mockCall = { mockDetailRepository.getMovieRecommendationPagingData(MOVIE_ID) },
      pagingData = fakePagingData,
      interactorCall = { interactor.getMovieRecommendationPagingData(MOVIE_ID) },
    ) { pagingList ->
      assertEquals("Transformers", pagingList[0].title)
      assertEquals(MOVIE_MEDIA_TYPE, pagingList[0].mediaType)
      assertEquals(99999, pagingList[0].id)
      assertEquals(8000, pagingList[0].voteCount)
    }
  }
}
