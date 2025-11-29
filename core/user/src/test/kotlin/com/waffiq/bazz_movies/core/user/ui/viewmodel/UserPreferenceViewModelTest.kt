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
    gravatarHash = null,
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
  fun getUserPref_whenSuccessful_emitsUserModel() = runTest {
    every { userPrefUseCase.getUser() } returns flowOf(userModel) // Mocking getUser()

    val observer = mockk<Observer<UserModel>>(relaxed = true) // Relaxed mock for observer
    viewModel.getUserPref().observeForever(observer)
    advanceUntilIdle() // Ensure all coroutines have completed

    coVerify { observer.onChanged(userModel) } // Verify observer was called with the expected value
  }

  @Test
  fun getUserRegionPref_whenSuccessful_emitsRegionString() = runTest {
    val region = "US"
    every { userPrefUseCase.getUserRegionPref() } returns flowOf(region)

    val observer = mockk<Observer<String>>(relaxed = true)
    viewModel.getUserRegionPref().observeForever(observer)
    advanceUntilIdle()
    coVerify { observer.onChanged(region) }
  }

  @Test
  fun getPermissionAsked_whenSuccessful_emitsBoolean() = runTest {
    every { userPrefUseCase.getPermissionAsked() } returns flowOf(false)

    val observer = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.getPermissionAsked().observeForever(observer)
    advanceUntilIdle()
    coVerify { observer.onChanged(false) }
  }

  @Test
  fun saveUserPref_whenSuccessful_callsSaveUserPrefFromUseCase() = runTest {
    coEvery { userPrefUseCase.saveUserPref(userModel) } just Runs
    viewModel.saveUserPref(userModel)
    advanceUntilIdle()
    coVerify { userPrefUseCase.saveUserPref(userModel) }
  }

  @Test
  fun saveRegionPref_whenSuccessful_callsSaveRegionPrefFromUseCase() = runTest {
    val region = "US"
    viewModel.saveRegionPref(region)
    advanceUntilIdle()
    coVerify { userPrefUseCase.saveRegionPref(region) }
  }

  @Test
  fun removeUserDataPref_whenSuccessful_callsRemoveUserDataPrefFromUseCase() = runTest {
    coEvery { userPrefUseCase.removeUserDataPref() } just Runs
    viewModel.removeUserDataPref()
    advanceUntilIdle()
    coVerify { userPrefUseCase.removeUserDataPref() }
  }

  @Test
  fun savePermissionAsked_whenSuccessful_callsSavePermissionAskedFromUseCase() = runTest {
    coEvery { userPrefUseCase.savePermissionAsked() } just Runs
    viewModel.savePermissionAsked()
    advanceUntilIdle()
    coVerify { userPrefUseCase.savePermissionAsked() }
  }
}
