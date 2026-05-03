package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import com.waffiq.bazz_movies.core.data.domain.usecase.listtv.GetListTvUseCase
import com.waffiq.bazz_movies.feature.home.testutils.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TvViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: TvSeriesViewModel

  private val mockGetListTvUseCase: GetListTvUseCase = mockk()

  @Before
  override fun setup() {
    super.setup()
    viewModel = TvSeriesViewModel(mockGetListTvUseCase)
  }

  @Test
  fun getPopularTv_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListTvUseCase.getPopularTv() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getPopularTv() })
    }

  @Test
  fun getAiringThisWeekTv_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListTvUseCase.getAiringThisWeekTv() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getAiringThisWeekTv() })
    }

  @Test
  fun getAiringTodayTv_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListTvUseCase.getAiringTodayTv() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getAiringTodayTv() })
    }

  @Test
  fun getTopRatedTv_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockGetListTvUseCase.getTopRatedTv() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getTopRatedTv() })
    }
}
