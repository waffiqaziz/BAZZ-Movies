package com.waffiq.bazz_movies.feature.detail.domain.usecase.getTvDetail

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailTv
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.differ
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.externalTvID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.tvCredits
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.video
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.watchProviders
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
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
    coEvery { mockRepository.getTvDetail(TV_ID) } returns
      flowOf(Outcome.Success(detailTv))

    interactor.getTvDetail(TV_ID, USER_REGION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success

      assertEquals(TV_ID, emission.data.id)
      assertEquals("Drama", emission.data.genre)
      assertEquals(listOf(5), emission.data.genreId)
      assertEquals("Returning Series", emission.data.duration)
      assertEquals("", emission.data.imdbId)
      assertEquals("TV-MA", emission.data.ageRating)
      assertEquals("8.2", emission.data.tmdbScore)
      assertEquals("2023", emission.data.releaseDateRegion.releaseDate)
      assertEquals("()", emission.data.releaseDateRegion.regionRelease)

      awaitComplete()
    }

    coVerify { mockRepository.getTvDetail(TV_ID) }
  }

  @Test
  fun getTvDetail_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockRepository.getTvDetail(TV_ID) } returns flowOf(Outcome.Error(ERROR_MESSAGE))

    interactor.getTvDetail(TV_ID, USER_REGION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getTvDetail(TV_ID) }
  }

  @Test
  fun getTvDetails_whenLoading_emitsLoading() = runTest {
    coEvery { mockRepository.getTvDetail(TV_ID) } returns flowOf(Outcome.Loading)

    interactor.getTvDetail(TV_ID, USER_REGION).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getTvDetail(TV_ID) }
  }

  @Test
  fun getTvExternalIds_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getTvExternalIds(TV_ID) } returns flowOf(Outcome.Success(externalTvID))

    interactor.getTvExternalIds(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("tt87654321", (emission as Outcome.Success).data.imdbId)
      awaitComplete()
    }

    coVerify { mockRepository.getTvExternalIds(TV_ID) }
  }

  @Test
  fun getTvExternalIds_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockRepository.getTvExternalIds(TV_ID) } returns flowOf(Outcome.Error(ERROR_MESSAGE))

    interactor.getTvExternalIds(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getTvExternalIds(TV_ID) }
  }

  @Test
  fun getTvTrailerLink_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getTvTrailerLink(TV_ID) } returns
      flowOf(Outcome.Success(video))

    interactor.getTvTrailerLink(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("Link Trailer", (emission as Outcome.Success).data)
      awaitComplete()
    }

    coVerify { mockRepository.getTvTrailerLink(TV_ID) }
  }

  @Test
  fun getTvTrailerLink_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockRepository.getTvTrailerLink(TV_ID) } returns flowOf(Outcome.Error(ERROR_MESSAGE))

    interactor.getTvTrailerLink(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getTvTrailerLink(TV_ID) }
  }

  @Test
  fun getTvTrailerLink_whenLoading_emitsLoading() = runTest {
    coEvery { mockRepository.getTvTrailerLink(TV_ID) } returns flowOf(Outcome.Loading)

    interactor.getTvTrailerLink(TV_ID).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getTvTrailerLink(TV_ID) }
  }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getTvCredits(TV_ID) } returns flowOf(Outcome.Success(tvCredits))

    interactor.getTvCredits(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals(tvCredits, (emission as Outcome.Success).data)
      awaitComplete()
    }

    coVerify { mockRepository.getTvCredits(TV_ID) }
  }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockRepository.getTvCredits(TV_ID) } returns flowOf(Outcome.Error(ERROR_MESSAGE))

    interactor.getTvCredits(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getTvCredits(TV_ID) }
  }

  @Test
  fun getTvWatchProviders_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getWatchProviders("tv", TV_ID) } returns
      flowOf(Outcome.Success(watchProviders))

    interactor.getTvWatchProviders("US", TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("https://some-provider.com", (emission as Outcome.Success).data.link)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("tv", TV_ID) }
  }

  @Test
  fun getTvWatchProviders_whenNoDataForCountry_emitsError() = runTest {
    val flow = flowOf(
      Outcome.Success(
        WatchProviders(results = emptyMap(), id = 12345)
      )
    )
    coEvery { mockRepository.getWatchProviders("tv", TV_ID) } returns flow

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
    val flow = flowOf(Outcome.Error(ERROR_MESSAGE))
    coEvery { mockRepository.getWatchProviders("tv", TV_ID) } returns flow

    interactor.getTvWatchProviders("US", TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("tv", TV_ID) }
  }

  @Test
  fun getTvWatchProviders_whenLoading_emitsLoading() = runTest {
    coEvery { mockRepository.getWatchProviders("tv", TV_ID) } returns flowOf(Outcome.Loading)

    interactor.getTvWatchProviders("US", TV_ID).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("tv", TV_ID) }
  }

  @Test
  fun getTvRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData =
      PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))

    testPagingData(fakePagingData) { pagingList ->
      assertEquals("Transformers", pagingList[0].title)
      assertEquals(MOVIE_MEDIA_TYPE, pagingList[0].mediaType)
      assertEquals(99999, pagingList[0].id)
      assertEquals(8000, pagingList[0].voteCount)
    }
  }

  private fun testPagingData(
    pagingData: PagingData<MediaItem>,
    assertions: (List<MediaItem>) -> Unit,
  ) = runTest {
    every { mockRepository.getTvRecommendationPagingData(TV_ID) } returns flowOf(pagingData)

    interactor.getTvRecommendationPagingData(TV_ID).test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertions(pagingList)

      job.cancel()
      awaitComplete()
    }

    verify { mockRepository.getTvRecommendationPagingData(TV_ID) }
  }
}
