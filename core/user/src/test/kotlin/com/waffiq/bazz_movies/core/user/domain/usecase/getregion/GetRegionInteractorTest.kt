package com.waffiq.bazz_movies.core.user.domain.usecase.getregion

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.user.domain.model.account.CountryIP
import com.waffiq.bazz_movies.core.user.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Testing GetRegionInteractor using mockk
 */
class GetRegionInteractorTest {

  private val mockRepository: IUserRepository = mockk()
  private lateinit var getRegionInteractor: GetRegionInteractor

  @Before
  fun setup() {
    getRegionInteractor = GetRegionInteractor(mockRepository)
  }

  @Test
  fun getCountryCode_whenSuccessful_emitsSuccess() = runTest {
    val expectedCountryIP = CountryIP(country = "US")
    val flow = flowOf(Outcome.Success(expectedCountryIP))
    coEvery { mockRepository.getCountryCode() } returns flow

    getRegionInteractor.getCountryCode().test {
      // assert the first emission is success with correct data
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      assertEquals("US", (emission as Outcome.Success).data.country)

      // assert no further emissions
      awaitComplete()
    }

    // verify repository interaction
    coVerify(exactly = 1) { mockRepository.getCountryCode() }
    coVerify { mockRepository.getCountryCode() }
  }

  @Test
  fun getCountryCode_whenUnsuccessful_emitsError() = runTest {
    val errorMessage = "Network error"
    val flow = flowOf(Outcome.Error(message = errorMessage))
    coEvery { mockRepository.getCountryCode() } returns flow

    getRegionInteractor.getCountryCode().test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getCountryCode() }
  }
}
