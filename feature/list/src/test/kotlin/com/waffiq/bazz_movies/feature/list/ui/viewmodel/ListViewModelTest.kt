package com.waffiq.bazz_movies.feature.list.ui.viewmodel

import androidx.paging.PagingData
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.movie.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.core.movie.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.core.test.KotestInstantExecutorExtension
import com.waffiq.bazz_movies.core.test.PagingFlowHelperTest.testPagingFlowCancelRemaining
import com.waffiq.bazz_movies.feature.list.domain.usecase.GetListUseCase
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeMovieMediaItemPagingData
import com.waffiq.bazz_movies.feature.list.testutils.DummyData.fakeTvMediaItemPagingData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.scopes.BehaviorSpecWhenContainerScope
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class ListViewModelTest : BehaviorSpec({

  extensions(KotestInstantExecutorExtension)
  val testDispatcher = UnconfinedTestDispatcher()

  val mockGetListUseCase: GetListUseCase = mockk()
  val mockGetListMoviesUseCase: GetListMoviesUseCase = mockk()
  val mockGetListTvUseCase: GetListTvUseCase = mockk()

  lateinit var viewModel: ListViewModel

  beforeTest {
    Dispatchers.setMain(testDispatcher)
    viewModel = ListViewModel(mockGetListUseCase, mockGetListMoviesUseCase, mockGetListTvUseCase)
  }

  afterTest {
    Dispatchers.resetMain()
  }

  data class MediaItemExpectation(
    val id: Int,
    val title: String,
    val overview: String,
  )

  val expectedMovie = MediaItemExpectation(1, "Inception", "A mind-bending thriller")
  val expectedTv = MediaItemExpectation(
    id = 1,
    title = "Breaking Bad",
    overview = "A high school chemistry teacher turned methamphetamine producer",
  )

  suspend fun BehaviorSpecWhenContainerScope.thenEmitsCorrectItem(
    flowProvider: () -> Flow<PagingData<MediaItem>>,
    expected: MediaItemExpectation = expectedTv, // default use tv as expected
  ) {
    Then("emit paging data with id=${expected.id}, title='${expected.title}'") {
      testPagingFlowCancelRemaining(flowProvider()) {
        it[0].id shouldBe expected.id
        it[0].title shouldBe expected.title
        it[0].overview shouldBe expected.overview
      }
    }
  }

  Given("a genre id is provided") {

    When("fetching movies by genre") {
      coEvery { mockGetListUseCase.getMovieByGenres(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getMovieByGenres("1") },
        expected = expectedMovie,
      )
    }

    When("fetching tv shows by genre") {
      coEvery { mockGetListUseCase.getTvByGenres(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getTvByGenres("1") },
      )
    }
  }

  Given("a keyword id is provided") {

    When("fetching movies by keyword") {
      coEvery { mockGetListUseCase.getMovieByKeywords(any()) } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getMovieByKeywords("1") },
        expected = expectedMovie,
      )
    }

    When("fetching tv shows by keyword") {
      coEvery { mockGetListUseCase.getTvByKeywords(any()) } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getTvByKeywords("1") },
      )
    }
  }

  Given("movies list is requested") {

    When("fetching upcoming movies") {
      coEvery { mockGetListMoviesUseCase.getUpcomingMovies() } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getUpcomingMovies() },
        expected = expectedMovie,
      )
    }

    When("fetching now playing movies") {
      coEvery { mockGetListMoviesUseCase.getPlayingNowMovies() } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getPlayingNowMovies() },
        expected = expectedMovie,
      )
    }

    When("fetching popular movies") {
      coEvery { mockGetListMoviesUseCase.getPopularMovies() } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getPopularMovies() },
        expected = expectedMovie,
      )
    }

    When("fetching top rated movies") {
      coEvery { mockGetListMoviesUseCase.getTopRatedMovies() } returns
        flowOf(fakeMovieMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getTopRatedMovies() },
        expected = expectedMovie,
      )
    }
  }

  Given("tv shows list is requested") {

    When("fetching popular tv shows") {
      coEvery { mockGetListTvUseCase.getPopularTv() } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getPopularTv() },
      )
    }

    When("fetching top rated tv shows") {
      coEvery { mockGetListTvUseCase.getTopRatedTv() } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getTopRatedTv() },
      )
    }

    When("fetching tv shows airing this week") {
      coEvery { mockGetListTvUseCase.getAiringThisWeekTv() } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getAiringThisWeekTv() },
      )
    }

    When("fetching tv shows airing today") {
      coEvery { mockGetListTvUseCase.getAiringTodayTv() } returns
        flowOf(fakeTvMediaItemPagingData)

      thenEmitsCorrectItem(
        flowProvider = { viewModel.getAiringTodayTv() },
      )
    }
  }
})