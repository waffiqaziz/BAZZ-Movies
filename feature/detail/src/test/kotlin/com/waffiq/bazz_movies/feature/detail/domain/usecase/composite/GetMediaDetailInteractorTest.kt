package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywords
import com.waffiq.bazz_movies.feature.detail.domain.model.tv.TvExternalIds
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.TV_ID
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.detailTv
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.mediaKeywords
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvCredits
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.tvExternalIds
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.video
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.watchProviders
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
      mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
      interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieDetailWithUserRegion_whenDetailSuccessButKeywordsError_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getMovieKeywords(MOVIE_ID.toString()) } returns
        flowOf(Outcome.Error("keywords error"))

      testSuccessScenario(
        mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
        mockResponse = detailMovie,
        interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords) // keywords should be null
      }
    }

  @Test
  fun getMovieDetailWithUserRegion_whenDetailSuccessButKeywordsLoading_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getMovieKeywords(MOVIE_ID.toString()) } returns
        flowOf(Outcome.Loading)

      testSuccessScenario(
        mockCall = { mockDetailRepository.getMovieDetail(MOVIE_ID) },
        mockResponse = detailMovie,
        interactorCall = { interactor.getMovieDetailWithUserRegion(MOVIE_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
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
      mockCall = { mockDetailRepository.getMovieWatchProviders(MOVIE_ID) },
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

    coEvery { mockDetailRepository.getMovieWatchProviders(MOVIE_ID) } returns flow

    interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(
        "No watch provider found for country code: US",
        (emission as Outcome.Error).message
      )
      awaitComplete()
    }

    coVerify { mockDetailRepository.getMovieWatchProviders(MOVIE_ID) }
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getMovieWatchProviders(MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getMovieWatchProviders(MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProvidersWithUserRegion(MOVIE_ID) }
    )
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
  fun getTvDetailWithUserRegion_whenDetailSuccessButKeywordsError_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
        flowOf(Outcome.Error("keywords error"))
      every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
        flowOf(Outcome.Success(TvExternalIds()))

      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getTvDetailWithUserRegion_whenDetailSuccessButKeywordsLoading_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
        flowOf(Outcome.Loading)
      every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
        flowOf(Outcome.Success(TvExternalIds()))

      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getTvDetailWithUserRegion_whenExternalIdsError_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
        flowOf(Outcome.Success(MediaKeywords()))
      every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
        flowOf(Outcome.Error("Error"))

      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getTvDetailWithUserRegion_whenExternalIdsLoading_emitsSuccessWithNullKeywords() =
    runTest {
      every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
        flowOf(Outcome.Success(MediaKeywords()))
      every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
        flowOf(Outcome.Loading)

      testSuccessScenario(
        mockCall = { mockDetailRepository.getTvDetail(TV_ID) },
        mockResponse = detailTv,
        interactorCall = { interactor.getTvDetailWithUserRegion(TV_ID) }
      ) { emission ->
        val mediaDetail = assertIs<MediaDetail>(emission.data)
        assertNull(mediaDetail.keywords)
      }
    }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockDetailRepository.getTvWatchProviders(TV_ID) },
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
    coEvery { mockDetailRepository.getTvWatchProviders(TV_ID) } returns
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

    coVerify { mockDetailRepository.getTvWatchProviders(TV_ID) }
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockDetailRepository.getTvWatchProviders(TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }

  @Test
  fun getTvWatchProvidersWithUserRegion_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockDetailRepository.getTvWatchProviders(TV_ID) },
      interactorCall = { interactor.getTvWatchProvidersWithUserRegion(TV_ID) }
    )
  }

  private fun setupExternalIdAndKeywordsMockData() {
    every { mockDetailRepository.getTvKeywords(TV_ID.toString()) } returns
      flowOf(Outcome.Success(mediaKeywords))
    every { mockDetailRepository.getTvExternalIds(TV_ID) } returns
      flowOf(Outcome.Success(tvExternalIds))
  }
}
