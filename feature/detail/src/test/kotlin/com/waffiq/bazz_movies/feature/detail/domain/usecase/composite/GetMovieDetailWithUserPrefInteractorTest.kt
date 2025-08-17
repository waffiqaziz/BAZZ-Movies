package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.watchProviders
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetMovieDetailWithUserPrefInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMovieDataWithUserRegionInteractor

  override fun initInteractor() {
    interactor = GetMovieDataWithUserRegionInteractor(
      mockGetMovieDetailUseCase,
      mockUserPrefUseCase
    )
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserPrefUseCase.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  @Test
  fun getMovieDetailWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieDetail(MOVIE_ID, USER_REGION) },
      mockResponse = movieMediaDetail,
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    ) { emission ->
      val mediaDetail = assertIs<MediaDetail>(emission.data)
      assertEquals(MOVIE_ID, mediaDetail.id)
    }
  }

  @Test
  fun getMovieDetailWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieDetail(MOVIE_ID, USER_REGION) },
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieDetailWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieDetail(MOVIE_ID, USER_REGION) },
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieWatchProviders(USER_REGION, MOVIE_ID) },
      mockResponse = watchProviders.results?.get("US"),
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    ) { emission ->
      val data = assertIs<WatchProvidersItem>(emission.data)
      assertEquals("https://some-provider.com", data.link)
    }
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieWatchProviders(USER_REGION, MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockGetMovieDetailUseCase.getMovieWatchProviders(USER_REGION, MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
  }
}
