package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.tvMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.watchProviders
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetTvDataWithUserRegionInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetTvDataWithUserRegionInteractor

  override fun initInteractor() {
    interactor = GetTvDataWithUserRegionInteractor(
      mockGetTvDetailUseCase,
      mockUserPrefUseCase
    )
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserPrefUseCase.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  @Test
  fun getTvDetailWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetTvDetailUseCase.getTvDetail(TV_ID, USER_REGION) },
      mockResponse = tvMediaDetail,
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    ) { emission ->
      val mediaDetail = assertIs<MediaDetail>(emission.data)
      assertEquals(TV_ID, mediaDetail.id)
    }
  }

  @Test
  fun getTvDetailWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockGetTvDetailUseCase.getTvDetail(TV_ID, USER_REGION) },
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvDetailWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetTvDetailUseCase.getTvDetail(TV_ID, USER_REGION) },
      interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetTvDetailUseCase.getTvWatchProviders(USER_REGION, TV_ID) },
      mockResponse = watchProviders.results?.get("US"),
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    ) { emission ->
      val data = assertIs<WatchProvidersItem>(emission.data)
      assertEquals("https://some-provider.com", data.link)
    }
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockGetTvDetailUseCase.getTvWatchProviders(USER_REGION, TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetTvDetailUseCase.getTvWatchProviders(USER_REGION, TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }
}
