package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainCoroutineRule
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.usecase.getregion.GetRegionUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RegionViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainCoroutineRule()

  private lateinit var viewModel: RegionViewModel
  private lateinit var getRegionUseCase: GetRegionUseCase

  @Before
  fun setup() {
    getRegionUseCase = mock()
    viewModel = RegionViewModel(getRegionUseCase)
  }

  @Test
  fun getCountryCode_whenSuccessful_emitsCountryCode() = runTest {
    val mockCountryIP = CountryIP(country = "US", ip = "192.168.0.1")
    val mockResult = Outcome.Success(mockCountryIP)
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()
    advanceUntilIdle()

    verify(observer).onChanged("US")
    viewModel.countryCode.removeObserver(observer)
  }

  @Test
  fun getCountryCode_whenUnsuccessful_emitsErrorState() = runTest {
    val mockResult = Outcome.Error(message = "Network error")
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<Event<String>>>()
    viewModel.errorState.observeForever(observer)
    viewModel.getCountryCode()
    advanceUntilIdle()

    verify(observer).onChanged(argThat { event -> event.peekContent() == "Network error" })
    viewModel.errorState.removeObserver(observer)
  }

  @Test
  fun getCountryCode_whenLoading_processesLoadingState() = runTest {
    val mockResult = Outcome.Loading
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()
    advanceUntilIdle()

    // verify that the observer is not called since no value is emitted for loading state
    verify(observer, never()).onChanged(any())
    viewModel.countryCode.removeObserver(observer)
  }

  @Test
  fun getCountryCode_whenUnsuccessful_setsCountryCodeToEmpty() = runTest {
    val mockResult = Outcome.Error(message = "Error")
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()
    advanceUntilIdle()

    verify(observer).onChanged("")
    viewModel.countryCode.removeObserver(observer)
  }

  @Test
  fun getCountryCode_whenCountryIsNull_setsCountryCodeToEmpty() = runTest {
    val mockCountryIP = CountryIP(country = null, ip = "192.168.0.1")
    val mockResult = Outcome.Success(mockCountryIP)
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()
    advanceUntilIdle()

    verify(observer).onChanged("")
    viewModel.countryCode.removeObserver(observer)
  }
}
