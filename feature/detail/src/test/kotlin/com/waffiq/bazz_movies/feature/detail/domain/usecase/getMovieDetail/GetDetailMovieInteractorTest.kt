package com.waffiq.bazz_movies.feature.detail.domain.usecase.getMovieDetail

import androidx.paging.PagingData
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.WatchProviders
import com.waffiq.bazz_movies.feature.detail.testutils.BaseInteractorTest
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieCredits
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.detailMovie
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.differ
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.ERROR_MESSAGE
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.MOVIE_ID
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.movieMediaItem
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

class GetDetailMovieInteractorTest : BaseInteractorTest() {

  private lateinit var interactor: GetDetailMovieInteractor

  override fun initInteractor() {
    interactor = GetDetailMovieInteractor(mockRepository)
  }

  @Test
  fun getDetailMovie_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getMovieDetail(MOVIE_ID) } returns
      flowOf(Outcome.Success(detailMovie))

    interactor.getMovieDetail(MOVIE_ID, USER_REGION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success

      assertEquals(MOVIE_ID, emission.data.id)
      assertEquals("Action", emission.data.genre)
      assertEquals(listOf(1), emission.data.genreId)
      assertEquals("2h 0m", emission.data.duration)
      assertEquals("tt1234567", emission.data.imdbId)
      assertEquals("PG-13", emission.data.ageRating)
      assertEquals("7.5", emission.data.tmdbScore)
      assertEquals("Nov 20, 2023", emission.data.releaseDateRegion.releaseDate)
      assertEquals("US", emission.data.releaseDateRegion.regionRelease)

      awaitComplete()
    }

    coVerify { mockRepository.getMovieDetail(MOVIE_ID) }
  }

  @Test
  fun getDetailMovie_whenUnsuccessful_emitsError() = runTest {
    coEvery { mockRepository.getMovieDetail(MOVIE_ID) } returns
      flowOf(Outcome.Error(message = ERROR_MESSAGE))

    interactor.getMovieDetail(MOVIE_ID, USER_REGION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(ERROR_MESSAGE, emission.message)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieDetail(MOVIE_ID) }
  }

  @Test
  fun getMovieDetail_whenLoading_emitsLoading() = runTest {
    coEvery { mockRepository.getMovieDetail(MOVIE_ID) } returns
      flowOf(Outcome.Loading)

    interactor.getMovieDetail(MOVIE_ID, USER_REGION).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieDetail(MOVIE_ID) }
  }

  @Test
  fun getMovieVideoLinks_whenSuccessful_emitsSuccess() = runTest {
    coEvery { mockRepository.getMovieTrailerLink(MOVIE_ID) } returns
      flowOf(Outcome.Success(video))

    interactor.getMovieVideoLinks(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("Link Trailer", (emission as Outcome.Success).data)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieTrailerLink(MOVIE_ID) }
  }

  @Test
  fun getLinkVideoMovies_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(ERROR_MESSAGE))
    coEvery { mockRepository.getMovieTrailerLink(MOVIE_ID) } returns flow

    interactor.getMovieVideoLinks(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieTrailerLink(MOVIE_ID) }
  }

  @Test
  fun getMovieVideoLinks_whenLoading_emitsLoading() = runTest {
    val flow = flowOf(Outcome.Loading)
    coEvery { mockRepository.getMovieTrailerLink(MOVIE_ID) } returns flow

    interactor.getMovieVideoLinks(MOVIE_ID).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieTrailerLink(MOVIE_ID) }
  }

  @Test
  fun getMovieCredits_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(movieCredits))
    coEvery { mockRepository.getMovieCredits(MOVIE_ID) } returns flow

    interactor.getMovieCredits(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals(movieCredits, (emission as Outcome.Success).data)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieCredits(MOVIE_ID) }
  }

  @Test
  fun getMovieCredits_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(ERROR_MESSAGE))
    coEvery { mockRepository.getMovieCredits(MOVIE_ID) } returns flow

    interactor.getMovieCredits(MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getMovieCredits(MOVIE_ID) }
  }

  @Test
  fun getWatchProvidersMovies_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(watchProviders))
    coEvery { mockRepository.getWatchProviders("movie", MOVIE_ID) } returns flow

    interactor.getMovieWatchProviders("US", MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("https://some-provider.com", (emission as Outcome.Success).data.link)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("movie", MOVIE_ID) }
  }

  @Test
  fun getMovieWatchProviders_whenNoDataForCountry_emitsError() = runTest {
    val flow = flowOf(
      Outcome.Success(
        WatchProviders(
          results = emptyMap(),
          id = 123456
        )
      )
    )
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
    val flow = flowOf(Outcome.Error(ERROR_MESSAGE))
    coEvery { mockRepository.getWatchProviders("movie", MOVIE_ID) } returns flow

    interactor.getMovieWatchProviders("US", MOVIE_ID).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      assertEquals(ERROR_MESSAGE, (emission as Outcome.Error).message)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("movie", MOVIE_ID) }
  }

  @Test
  fun getMovieWatchProviders_whenLoading_emitsLoading() = runTest {
    val flow = flowOf(Outcome.Loading)
    coEvery { mockRepository.getWatchProviders("movie", MOVIE_ID) } returns flow

    interactor.getMovieWatchProviders("US", MOVIE_ID).test {
      assertTrue(awaitItem() is Outcome.Loading)
      awaitComplete()
    }

    coVerify { mockRepository.getWatchProviders("movie", MOVIE_ID) }
  }

  @Test
  fun getMovieRecommendationPagingData_whenValueIsValid_returnsDataCorrectly() = runTest {
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
    every { mockRepository.getMovieRecommendationPagingData(MOVIE_ID) } returns flowOf(pagingData)

    interactor.getMovieRecommendationPagingData(MOVIE_ID).test {
      val actualPagingData = awaitItem()
      val job = launch { differ.submitData(actualPagingData) }
      advanceUntilIdle()

      val pagingList = differ.snapshot().items
      assertions(pagingList)

      job.cancel()
      awaitComplete()
    }

    verify { mockRepository.getMovieRecommendationPagingData(MOVIE_ID) }
  }
}