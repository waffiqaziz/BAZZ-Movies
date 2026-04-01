package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailTv
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvExternalIds
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

class GetTvDataWithUserRegionInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetTvDataWithUserRegionInteractor

  override fun initInteractor() {
    interactor = GetTvDataWithUserRegionInteractor(mockDetailRepository, mockUserRepository)
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserRepository.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  @Test
  fun getTvDetailWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    setupExternalIdAndKeywordsMockData()
    testSuccessScenario(
      mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
      mockResponse = detailTv,
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    ) { emission ->
      val mediaDetail = assertIs<MediaDetail>(emission.data)
      assertEquals(TV_ID, mediaDetail.id)
    }
  }

  @Test
  fun getTvDetailWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
      flowOf(Outcome.Error("error"))
    every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
      flowOf(Outcome.Error("error"))
    testErrorScenario(
      mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvDetailWithUserRegion_whenLoading_emitsLoading() = runTest {
    setupExternalIdAndKeywordsMockData()
    testLoadingScenario(
      mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getWatchProviders("tv", TV_ID) },
      mockResponse = watchProviders,
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    ) { emission ->
      val data = assertIs<WatchProvidersItem>(emission.data)
      assertEquals("https://some-provider.com", data.link)
    }
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenNoDataForCountry_emitsError() = runTest {
    val emptyWatchProviders = WatchProviders(results = emptyMap(), id = TV_ID)
    coEvery { mockDetailRepository.getWatchProviders("tv", TV_ID) } returns
      flowOf(Outcome.Success(emptyWatchProviders))

    interactor.getTvWatchProvidersWithUserRegion(TV_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(
        "No watch provider found for country code: US",
        (emission as Outcome.Error).message
      )
      awaitComplete()
    }

    coVerify { mockDetailRepository.getWatchProviders("tv", TV_ID) }
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getWatchProviders("tv", TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getWatchProviders("tv", TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }

  private fun setupExternalIdAndKeywordsMockData(){
    every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
      flowOf(Outcome.Success(mediaKeywords))
    every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
      flowOf(Outcome.Success(tvExternalIds))
  }
}
