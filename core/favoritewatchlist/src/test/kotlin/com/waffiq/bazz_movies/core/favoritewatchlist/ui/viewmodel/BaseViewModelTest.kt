package com.waffiq.bazz_movies.core.favoritewatchlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseViewModelTest {
  private lateinit var viewModel: BaseViewModel

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    viewModel = BaseViewModel()
  }

  @Test
  fun markSnackbarShown_setIsSnackbarShownTrue() {
    viewModel.markSnackbarShown()
    assertTrue(viewModel.isSnackbarShown.value == true)
  }

  @Test
  fun resetSnackbarShown_setIsSnackbarShownFalse() {
    viewModel.resetSnackbarShown()
    assertTrue(viewModel.isSnackbarShown.value == false)
  }

  @Test
  fun simultaneously_changeTheValue() {
    viewModel.markSnackbarShown()
    assertTrue(viewModel.isSnackbarShown.value == true)
    viewModel.resetSnackbarShown()
    assertTrue(viewModel.isSnackbarShown.value == false)
    viewModel.markSnackbarShown()
    assertTrue(viewModel.isSnackbarShown.value == true)
  }
}
