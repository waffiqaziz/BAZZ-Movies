package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.externalTvID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.video
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTvDetailsInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetTvDetailInteractor

  override fun initInteractor() {
    interactor = GetTvDetailInteractor(mockDetailRepository)
  }

  @Test
  fun getTvExternalIds_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getTvExternalIds(TV_ID) },
      mockResponse = externalTvID,
      interactorCall = { interactor.getTvExternalIds(TV_ID) }
    ) { emission ->
      assertEquals("tt87654321", emission.data.imdbId)
    }
  }

  @Test
  fun getTvExternalIds_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getTvExternalIds(TV_ID) },
      interactorCall = { interactor.getTvExternalIds(TV_ID) }
    )
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getTvTrailerLink(TV_ID) },
      mockResponse = video,
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    ) { emission ->
      assertEquals("Link Trailer", emission.data)
    }
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getTvTrailerLink(TV_ID) },
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    )
  }

  @Test
  fun getTvTrailerLink_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getTvTrailerLink(TV_ID) },
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    )
  }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getTvCredits(TV_ID) },
      mockResponse = tvCredits,
      interactorCall = { interactor.getTvCredits(TV_ID) }
    ) { emission ->
      assertEquals(tvCredits, emission.data)
    }
  }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getTvCredits(TV_ID) },
      interactorCall = { interactor.getTvCredits(TV_ID) }
    )
  }

  @Test
  fun getTvRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData = PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))

    testPagingData(
      mockCall = { mockDetailRepository.getTvRecommendationPagingData(TV_ID) },
      pagingData = fakePagingData,
      interactorCall = { interactor.getTvRecommendationPagingData(TV_ID) },
      assertions = { pagingList ->
        assertEquals("Transformers", pagingList[0].title)
        assertEquals(MOVIE_MEDIA_TYPE, pagingList[0].mediaType)
        assertEquals(99999, pagingList[0].id)
        assertEquals(8000, pagingList[0].voteCount)
      }
    )
  }

  private fun setupExternalIdAndKeywordsMockData(){
    every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
      flowOf(Outcome.Success(mediaKeywords))
    every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
      flowOf(Outcome.Success(tvExternalIds))
  }
}
