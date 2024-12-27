package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.test.MainCoroutineRule
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPreferenceViewModelTest {

  private val userPrefUseCase: UserPrefUseCase = mockk(relaxed = true)
  private lateinit var viewModel: UserPreferenceViewModel
  private val userModel = UserModel(
    userId = 2,
    name = "Jane Doe",
    username = "janedoe",
    password = "",
    region = "US",
    token = "anotherToken",
    isLogin = false,
    gravatarHast = null,
    tmdbAvatar = null
  )

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainCoroutineRule()

  @Before
  fun setUp() {
    viewModel = UserPreferenceViewModel(userPrefUseCase)
  }

  @Test
  fun `getUserPref emits UserModel`() = runTest {
    every { userPrefUseCase.getUser() } returns flowOf(userModel) // Mocking getUser()

    val observer = mockk<Observer<UserModel>>(relaxed = true) // Relaxed mock for observer
    viewModel.getUserPref().observeForever(observer)
    advanceUntilIdle() // Ensure all coroutines have completed

    coVerify { observer.onChanged(userModel) } // Verify observer was called with the expected value
  }

  @Test
  fun `getUserRegionPref emits region string`() = runTest {
    val region = "US"
    every { userPrefUseCase.getUserRegionPref() } returns flowOf(region)

    val observer = mockk<Observer<String>>(relaxed = true)
    viewModel.getUserRegionPref().observeForever(observer)
    advanceUntilIdle()
    coVerify { observer.onChanged(region) }
  }

  @Test
  fun `saveUserPref calls use case with correct data`() = runTest {
    coEvery { userPrefUseCase.saveUserPref(userModel) } just Runs
    viewModel.saveUserPref(userModel)
    advanceUntilIdle()
    coVerify { userPrefUseCase.saveUserPref(userModel) }
  }

  @Test
  fun `saveRegionPref calls saveRegionPref on UserPrefUseCase`() = runTest {
    val region = "US"
    viewModel.saveRegionPref(region)
    advanceUntilIdle()
    coVerify { userPrefUseCase.saveRegionPref(region) }
  }

  @Test
  fun `removeUserDataPref calls use case`() = runTest {
    coEvery { userPrefUseCase.removeUserDataPref() } just Runs
    viewModel.removeUserDataPref()
    advanceUntilIdle()
    coVerify { userPrefUseCase.removeUserDataPref() }
  }
}
