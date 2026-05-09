package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import com.waffiq.bazz_movies.core.data.domain.usecase.listmovie.GetListMoviesUseCase
import com.waffiq.bazz_movies.feature.home.testutils.BaseViewModelTest
import com.waffiq.bazz_movies.feature.home.ui.domain.TrendingPeriod
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: MovieViewModel

  private val mockGetListMoviesUseCase: GetListMoviesUseCase = mockk()

  @Before
  override fun setup() {
    super.setup()
    viewModel = MovieViewModel(mockGetListMoviesUseCase)
  }

  @Test
  fun getTopRatedMovies_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListMoviesUseCase.getTopRatedMovies() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getTopRatedMovies() })
    }

  @Test
  fun getPopularMovies_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListMoviesUseCase.getPopularMovies() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getPopularMovies() })
    }

  @Test
  fun getTrendingThisWeek_whenSuccessful_emitsCorrectValue() =
    runTest {
      viewModel.setTrendingPeriod(TrendingPeriod.WEEK)
      coEvery { mockGetListMoviesUseCase.getTrendingThisWeek() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.trending })
    }

  @Test
  fun getTrendingToday_whenSuccessful_emitsCorrectValue() =
    runTest {
      viewModel.setTrendingPeriod(TrendingPeriod.TODAY)
      coEvery { mockGetListMoviesUseCase.getTrendingToday() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.trending })
    }

  @Test
  fun getUpcomingMovies_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListMoviesUseCase.getUpcomingMovies() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getUpcomingMovies() })
    }

  @Test
  fun getPlayingNowMovies_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListMoviesUseCase.getPlayingNowMovies() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getPlayingNowMovies() })
    }
}
