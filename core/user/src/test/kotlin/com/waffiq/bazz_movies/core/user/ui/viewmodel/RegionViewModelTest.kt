package com.waffiq.bazz_movies.core.user.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.usecase.get_region.GetRegionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class RegionViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val testDispatcher = UnconfinedTestDispatcher()

  private lateinit var viewModel: RegionViewModel
  private lateinit var getRegionUseCase: GetRegionUseCase

  @Before
  fun setup() {
    Dispatchers.setMain(testDispatcher)

    // Mock the UseCase
    getRegionUseCase = mock()
    viewModel = RegionViewModel(getRegionUseCase)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `getCountryCode emits country code when use case returns success`() = runTest {
    // Arrange
    val mockCountryIP = CountryIP(country = "US", ip = "192.168.0.1")
    val mockResult = NetworkResult.Success(mockCountryIP)

    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    // Act
    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()

    // Assert
    verify(observer).onChanged("US")
    viewModel.countryCode.removeObserver(observer)
  }

  @Test
  fun `getCountryCode emits error state when use case returns error`() = runTest {
    // Arrange
    val mockResult = NetworkResult.Error(message = "Network error")

    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    // Act
    val observer = mock<Observer<Event<String>>>()
    viewModel.errorState.observeForever(observer)
    viewModel.getCountryCode()

    // Assert
    verify(observer).onChanged(argThat { event -> event.peekContent() == "Network error" })
    viewModel.errorState.removeObserver(observer)
  }

  @Test
  fun `getCountryCode sets countryCode to empty when use case returns error`() = runTest {
    val mockResult = NetworkResult.Error(message = "Error")
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()

    verify(observer).onChanged("")
    viewModel.countryCode.removeObserver(observer)
  }

  @Test
  fun `getCountryCode sets countryCode to empty when country is null`() = runTest {
    val mockCountryIP = CountryIP(country = null, ip = "192.168.0.1")
    val mockResult = NetworkResult.Success(mockCountryIP)
    `when`(getRegionUseCase.getCountryCode()).thenReturn(flow { emit(mockResult) })

    val observer = mock<Observer<String>>()
    viewModel.countryCode.observeForever(observer)
    viewModel.getCountryCode()

    verify(observer).onChanged("")
    viewModel.countryCode.removeObserver(observer)
  }
}
