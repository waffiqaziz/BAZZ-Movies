package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailTv
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.externalTvID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.tvCredits
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.video
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.watchProviders
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetTvDetailsInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetTvDetailInteractor

  override fun initInteractor() {
    interactor = GetTvDetailInteractor(mockRepository)
  }

  @Test
  fun getTvDetail_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getTvDetail(TV_ID) },
      mockResponse = detailTv,
      interactorCall = { interactor.getTvDetail(TV_ID, USER_REGION) }
    ) { emission ->
      val mediaDetail = emission.data as MediaDetail

      assertEquals(TV_ID, mediaDetail.id)
      assertEquals("Drama", mediaDetail.genre)
      assertEquals(listOf(5), mediaDetail.genreId)
      assertEquals("Returning Series", mediaDetail.duration)
      assertEquals("", mediaDetail.imdbId)
      assertEquals("TV-MA", mediaDetail.ageRating)
      assertEquals("8.2", mediaDetail.tmdbScore)
      assertEquals("2023", mediaDetail.releaseDateRegion.releaseDate)
      assertEquals("()", mediaDetail.releaseDateRegion.regionRelease)
    }
  }

  @Test
  fun getTvDetail_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getTvDetail(TV_ID) },
      interactorCall = { interactor.getTvDetail(TV_ID, USER_REGION) }
    )
  }

  @Test
  fun getTvDetails_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getTvDetail(TV_ID) },
      interactorCall = { interactor.getTvDetail(TV_ID, USER_REGION) }
    )
  }

  @Test
  fun getTvExternalIds_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getTvExternalIds(TV_ID) },
      mockResponse = externalTvID,
      interactorCall = { interactor.getTvExternalIds(TV_ID) }
    ) { emission ->
      assertEquals("tt87654321", emission.data.imdbId)
    }
  }

  @Test
  fun getTvExternalIds_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getTvExternalIds(TV_ID) },
      interactorCall = { interactor.getTvExternalIds(TV_ID) }
    )
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getTvTrailerLink(TV_ID) },
      mockResponse = video,
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    ) { emission ->
      assertEquals("Link Trailer", emission.data)
    }
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getTvTrailerLink(TV_ID) },
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    )
  }

  @Test
  fun getTvTrailerLink_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getTvTrailerLink(TV_ID) },
      interactorCall = { interactor.getTvTrailerLink(TV_ID) }
    )
  }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getTvCredits(TV_ID) },
      mockResponse = tvCredits,
      interactorCall = { interactor.getTvCredits(TV_ID) }
    ) { emission ->
      assertEquals(tvCredits, emission.data)
    }
  }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getTvCredits(TV_ID) },
      interactorCall = { interactor.getTvCredits(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProviders_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getWatchProviders("tv", TV_ID) },
      mockResponse = watchProviders,
      interactorCall = { interactor.getTvWatchProviders("US", TV_ID) }
    ) { emission ->
      assertEquals("https://some-provider.com", (emission.data as WatchProvidersItem).link)
    }
  }

  @Test
  fun getTvWatchProviders_whenNoDataForCountry_emitsError() = runTest {
    val emptyWatchProviders = WatchProviders(results = emptyMap(), id = 12345)
     coEvery { mockRepository.getWatchProviders("tv", TV_ID) } returns
      flowOf(Outcome.Success(emptyWatchProviders))

    interactor.getTvWatchProviders("US", TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(
        "No watch provider found for country code: US",
        (emission as Outcome.Error).message
      )
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("tv", TV_ID) }
  }

  @Test
  fun getTvWatchProviders_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getWatchProviders("tv", TV_ID) },
      interactorCall = { interactor.getTvWatchProviders("US", TV_ID) }
    )
  }

  @Test
  fun getTvWatchProviders_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getWatchProviders("tv", TV_ID) },
      interactorCall = { interactor.getTvWatchProviders("US", TV_ID) }
    )
  }

  @Test
  fun getTvRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData = PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))

    testPagingData(
      mockCall = { mockRepository.getTvRecommendationPagingData(TV_ID) },
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
}
