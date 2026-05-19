package com.waffiq.bazz_movies.feature.home.ui.viewmodel

import com.waffiq.bazz_movies.core.data.domain.usecase.asian.GetAsianMediaUseCase
import com.waffiq.bazz_movies.feature.home.testutils.BaseViewModelTest
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AsianViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: AsianViewModel

  private val mockAsianMediaUseCase: GetAsianMediaUseCase = mockk()

  @Before
  override fun setup() {
    super.setup()
    viewModel = AsianViewModel(mockAsianMediaUseCase)
  }

  @Test
  fun getAnimeAllTime_whenSuccessful_emitsCorrectValue() =
    runTest {
      viewModel.setAnimePeriod(AnimePeriod.ALL_TIME)
      coEvery { mockAsianMediaUseCase.getAnimeAllTime() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.anime })
    }

  @Test
  fun getAnimeThisSeason_whenSuccessful_emitsCorrectValue() =
    runTest {
      viewModel.setAnimePeriod(AnimePeriod.THIS_SEASON)
      coEvery { mockAsianMediaUseCase.getAnimeThisSeason() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.anime })
    }

  @Test
  fun getDonghua_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockAsianMediaUseCase.getDonghua() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getDonghua() })
    }

  @Test
  fun getAsianRomance_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockAsianMediaUseCase.getAsianRomance() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getAsianRomance() })
    }

  @Test
  fun getCostumeDrama_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockAsianMediaUseCase.getCostumeDrama() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getCostumeDrama() })
    }

  @Test
  fun getRealityShow_whenSuccessful_emitsCorrectValue() =
    runTest {
      coEvery { mockAsianMediaUseCase.getRealityShow() } returns expectedFlow
      thenEmitsCorrectItem({ viewModel.getRealityShow() })
    }

  @Test
  fun setAnimePeriod_whenCalled_shouldUpdateTrendingPeriod() {
    assertEquals(AnimePeriod.THIS_SEASON, viewModel.animePeriod.value)
    viewModel.setAnimePeriod(AnimePeriod.ALL_TIME)
    assertEquals(AnimePeriod.ALL_TIME, viewModel.animePeriod.value)
  }
}
