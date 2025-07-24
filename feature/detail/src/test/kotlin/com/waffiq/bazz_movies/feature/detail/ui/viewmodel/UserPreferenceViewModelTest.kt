package com.waffiq.bazz_movies.feature.detail.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.domain.UserModel
import com.waffiq.bazz_movies.core.test.MainCoroutineRule
import com.waffiq.bazz_movies.core.user.domain.usecase.userpreference.UserPrefUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPreferenceViewModelTest {

  private val userPrefUseCase: UserPrefUseCase = mockk(relaxed = true)
  private lateinit var viewModel: DetailUserPrefViewModel
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
    viewModel = DetailUserPrefViewModel(userPrefUseCase)
  }

  @Test
  fun getUserToken_whenSuccessful_emitsUserModel() = runTest {
    every { userPrefUseCase.getUserToken() } returns flowOf("US")

    val observer = mockk<Observer<String>>(relaxed = true)
    viewModel.getUserToken().observeForever(observer)
    advanceUntilIdle()

    coVerify { observer.onChanged("US") }
  }

  @Test
  fun getUserPref_whenSuccessful_emitsUserModel() = runTest {
    every { userPrefUseCase.getUser() } returns flowOf(userModel)

    val observer = mockk<Observer<UserModel>>(relaxed = true)
    viewModel.getUserPref().observeForever(observer)
    advanceUntilIdle()

    coVerify { observer.onChanged(userModel) }
  }

  @Test
  fun getUserRegion_whenSuccessful_emitsRegionString() = runTest {
    val region = "US"
    every { userPrefUseCase.getUserRegionPref() } returns flowOf(region)

    val observer = mockk<Observer<String>>(relaxed = true)
    viewModel.getUserRegion().observeForever(observer)
    advanceUntilIdle()
    coVerify { observer.onChanged(region) }
  }
}
