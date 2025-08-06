package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProvidersItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.USER_REGION
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailMovie
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieCredits
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaItem
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.video
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.watchProviders
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertIs

class GetDetailMovieInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetDetailMovieInteractor

  override fun initInteractor() {
    interactor = GetDetailMovieInteractor(mockRepository)
  }

  @Test
  fun getDetailMovie_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getMovieDetail(MOVIE_ID) },
      mockResponse = detailMovie,
      interactorCall = { interactor.getMovieDetail(MOVIE_ID, USER_REGION) }
    ) { emission ->
      val mediaDetail = assertIs<MediaDetail>(emission.data)

      assertEquals(MOVIE_ID, mediaDetail.id)
      assertEquals("Action", mediaDetail.genre)
      assertEquals(listOf(1), mediaDetail.genreId)
      assertEquals("2h 0m", mediaDetail.duration)
      assertEquals("tt1234567", mediaDetail.imdbId)
      assertEquals("PG-13", mediaDetail.ageRating)
      assertEquals("7.5", mediaDetail.tmdbScore)
      assertEquals("Nov 20, 2023", mediaDetail.releaseDateRegion.releaseDate)
      assertEquals("US", mediaDetail.releaseDateRegion.regionRelease)
    }
  }

  @Test
  fun getDetailMovie_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getMovieDetail(MOVIE_ID) },
      interactorCall = { interactor.getMovieDetail(MOVIE_ID, USER_REGION) }
    )
  }

  @Test
  fun getMovieDetail_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getMovieDetail(MOVIE_ID) },
      interactorCall = { interactor.getMovieDetail(MOVIE_ID, USER_REGION) }
    )
  }

  @Test
  fun getMovieVideoLinks_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getMovieTrailerLink(MOVIE_ID) },
      mockResponse = video,
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    ) { emission ->
      assertEquals("Link Trailer", emission.data)
    }
  }

  @Test
  fun getLinkVideoMovies_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getMovieTrailerLink(MOVIE_ID) },
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieVideoLinks_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getMovieTrailerLink(MOVIE_ID) },
      interactorCall = { interactor.getMovieVideoLinks(MOVIE_ID) }
    )
  }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getMovieCredits(MOVIE_ID) },
      mockResponse = movieCredits,
      interactorCall = { interactor.getMovieCredits(MOVIE_ID) }
    ) { emission ->
      assertEquals(movieCredits, emission.data)
    }
  }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getMovieCredits(MOVIE_ID) },
      interactorCall = { interactor.getMovieCredits(MOVIE_ID) }
    )
  }

  @Test
  fun getWatchProvidersMovies_whenSuccessful_emitsSuccess() = runTest {
    testSuccessScenario(
      mockCall = { mockRepository.getWatchProviders(MOVIE_MEDIA_TYPE, MOVIE_ID) },
      mockResponse = watchProviders,
      interactorCall = { interactor.getMovieWatchProviders("US", MOVIE_ID) }
    ) { emission ->
      val data = assertIs<WatchProvidersItem>(emission.data)
      assertEquals("https://some-provider.com", data.link)
    }
  }

  @Test
  fun getMovieWatchProviders_whenNoDataForCountry_emitsError() = runTest {
    val flow = flowOf(Outcome.Success(WatchProviders(results = emptyMap(), id = 123456)))

    coEvery { mockRepository.getWatchProviders("movie", MOVIE_ID) } returns flow

    interactor.getMovieWatchProviders("US", MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(
        "No watch provider found for country code: US",
        (emission as Outcome.Error).message
      )
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("movie", MOVIE_ID) }
  }

  @Test
  fun getWatchProvidersMovies_whenUnsuccessful_emitsError() = runTest {
    testErrorScenario(
      mockCall = { mockRepository.getWatchProviders("movie", MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProviders("US", MOVIE_ID) }
    )
  }

  @Test
  fun getMovieWatchProviders_whenLoading_emitsLoading() = runTest {
    testLoadingScenario(
      mockCall = { mockRepository.getWatchProviders("movie", MOVIE_ID) },
      interactorCall = { interactor.getMovieWatchProviders("US", MOVIE_ID) }
    )
  }

  @Test
  fun getMovieRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
    val fakePagingData = PagingData.from(listOf(movieMediaItem, movieMediaItem, movieMediaItem))

    testPagingData(
      mockCall = { mockRepository.getMovieRecommendationPagingData(MOVIE_ID) },
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
