package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailTv
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvCredits
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertIs

class GetMediaDetailInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetMediaDetailInteractor

  override fun initInteractor() {
    interactor = GetMediaDetailInteractor(mockDetailRepository, mockUserRepository)
  }

  @Before
  override fun baseSetUp() {
    super.baseSetUp()
    every { mockUserRepository.getUserRegionPref() } returns flowOf(USER_REGION)
  }

  @Test
  fun getMovieDetailWithUserRegion_whenSuccessful_emitsSuccess() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
        mockResponse = detailMovie,
        interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) },
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertEquals(MOVIE_ID, mediaDetail.id)
        assertNotNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getMovieDetailWithUserRegion_whenUnsuccessful_emitsError() =
    runTest {
      testErrorScenario(
        mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
        interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) },
      )
    }

  @Test
  fun getMovieDetailWithUserRegion_whenLoading_emitsLoading() =
    runTest {
      testLoadingScenario(
        mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
        interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) },
      )
    }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getMovieCredits(MOVIE_ID) },
        mockResponse = movieCredits,
        interactorCall = { interactor.getMovieCredits(MOVIE_ID) },
      ) { emission ->
        assertEquals(movieCredits, emission.data)
      }
    }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() =
    runTest {
      testErrorScenario(
        mockCall = { mockDetailRepository.getMovieCredits(MOVIE_ID) },
        interactorCall = { interactor.getMovieCredits(MOVIE_ID) },
      )
    }

  @Test
  fun getTvCredits_whenSuccessful_emitsSuccess() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvCredits(TV_ID) },
        mockResponse = tvCredits,
        interactorCall = { interactor.getTvCredits(TV_ID) },
      ) { emission ->
        assertEquals(tvCredits, emission.data)
      }
    }

  @Test
  fun getTvCredits_whenUnsuccessful_emitsError() =
    runTest {
      testErrorScenario(
        mockCall = { mockDetailRepository.getTvCredits(TV_ID) },
        interactorCall = { interactor.getTvCredits(TV_ID) },
      )
    }

  @Test
  fun getTvDetailWithUserRegion_whenSuccessful_emitsSuccess() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) },
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertEquals(TV_ID, mediaDetail.id)
      }
    }

  @Test
  fun getTvDetailWithUserRegion_whenUnsuccessful_emitsError() =
    runTest {
      testErrorScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) },
      )
    }

  @Test
  fun getTvDetailWithUserRegion_whenLoading_emitsLoading() =
    runTest {
      testLoadingScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) },
      )
    }

  @Test
  fun getTvDetailWithUserRegion_whenExternalIdsError_emitsSuccessWithNullKeywords() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) },
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getTvDetailWithUserRegion_whenExternalIdsLoading_emitsSuccessWithNullKeywords() =
    runTest {
      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) },
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }
}
