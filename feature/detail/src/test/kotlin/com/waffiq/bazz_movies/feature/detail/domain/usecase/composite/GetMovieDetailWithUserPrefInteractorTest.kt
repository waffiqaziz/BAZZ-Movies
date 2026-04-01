package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.watchProviders
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetMovieDetailWithUserPrefInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMovieDataWithUserRegionInteractor

  override fun initInteractor() {
    interactor = GetMovieDataWithUserRegionInteractor(mockDetailRepository, mockUserRepository)
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserRepository.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  @Test
  fun getMovieDetailWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    every { mockDetailRepository.getMovieKeywords(MOVIE_ID.toString()) } returns
      flowOf(Outcome.Success(mediaKeywords))
    testSuccessScenario(
      mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
      mockResponse = detailMovie,
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    ) { emission ->
      val mediaDetail = assertIs<MediaDetail>(emission.data)
      assertEquals(MOVIE_ID, mediaDetail.id)
    }
  }

  @Test
  fun getMovieDetailWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    every { mockDetailRepository.getMovieKeywords(MOVIE_ID.toString()) } returns
      flowOf(Outcome.Error("error"))
    testErrorScenario(
      mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID ) },
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieDetailWithUserRegion_whenLoading_emitsLoading() = runTest {
    every { mockDetailRepository.getMovieKeywords(MOVIE_ID.toString()) } returns
      flowOf(Outcome.Success(mediaKeywords))
    testLoadingScenario(
      mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getWatchProviders("movie", MOVIE_ID) },
      mockResponse = watchProviders,
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    ) { emission ->
      val data = assertIs<WatchProvidersItem>(emission.data)
      assertEquals("https://some-provider.com", data.link)
    }
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenNoDataForCountry_emitsError() = runTest {
    val flow = flowOf(Outcome.Success(WatchProviders(results = emptyMap(), id = MOVIE_ID)))

    coEvery { mockDetailRepository.getWatchProviders("movie", MOVIE_ID) } returns flow

    interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(
        "No watch provider found for country code: US",
        (emission as Outcome.Error).message
      )
      awaitComplete()
    }

    coVerify { mockDetailRepository.getWatchProviders("movie", MOVIE_ID) }
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getWatchProviders("movie", MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getWatchProviders("movie", MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
  }
}
