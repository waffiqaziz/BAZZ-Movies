package com.waffiq.bazz_movies.core.user.domain.usecase.getregion

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Testing GetRegionInteractor using mockk
 */
@ExperimentalCoroutinesApi
class GetRegionInteractorTest {

  // Mock dependencies
  private val mockRepository: IUserRepository = mockk()
  private lateinit var getRegionInteractor: GetRegionInteractor

  @Before
  fun setup() {
    getRegionInteractor = GetRegionInteractor(mockRepository)
  }

  @Test
  fun `getCountryCode emits success`() = runTest {
    val expectedCountryIP = CountryIP(country = "US")
    val flow = flowOf(NetworkResult.Success(expectedCountryIP))
    coEvery { mockRepository.getCountryCode() } returns flow

    getRegionInteractor.getCountryCode().test {
      // Assert the first emission is success with correct data
      val emission = awaitItem()
      assertTrue(emission is NetworkResult.Success)
      assertEquals("US", (emission as NetworkResult.Success).data.country)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getCountryCode() }
    coVerify { mockRepository.getCountryCode() }
  }

  @Test
  fun `getCountryCode emits error`() = runTest {
    val errorMessage = "Network error"
    val flow = flowOf(NetworkResult.Error(message = errorMessage))
    coEvery { mockRepository.getCountryCode() } returns flow

    getRegionInteractor.getCountryCode().test {
      val emission = awaitItem()
      assertTrue(emission is NetworkResult.Error)
      emission as NetworkResult.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getCountryCode() }
  }
}

