package com.waffiq.bazz_movies.feature.person.ui

import androidx.lifecycle.Observer
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class PersonViewModelTest : BasePersonViewModelTest() {

  val observer = mockk<Observer<List<CastItem>>>(relaxed = true)

  @Test
  fun getDetailPerson_whenSuccessful_emitsSuccess() {
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
      flowSuccessWithLoading(mockDetailPerson)

    testViewModel(
      runBlock = { personViewModel.getDetailPerson(personId) },
      liveData = personViewModel.detailPerson,
      expectedSuccess = mockDetailPerson,
      checkLoading = true,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
      },
    )
  }

  @Test
  fun getDetailPerson_whenUnsuccessful_emitsError() {
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns errorFlow

    testViewModel(
      runBlock = { personViewModel.getDetailPerson(personId) },
      liveData = personViewModel.detailPerson,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
      },
    )
  }

  @Test
  fun getDetailPerson_whenLoading_emitsLoading() {
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns flowOf(Outcome.Loading)

    testViewModel(
      runBlock = { personViewModel.getDetailPerson(personId) },
      liveData = personViewModel.detailPerson,
      checkLoading = true,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
      },
    )
  }

  @Test
  fun castList_whenNotNull_returnsCorrectData() {
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
      flowSuccessWithLoading(mockDetailPerson)

    testViewModel(
      runBlock = { personViewModel.getDetailPerson(personId) },
      liveData = personViewModel.castList,
      expectedSuccess = listOf(mockCastItem),
      checkLoading = true,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
      },
    )
  }

  @Test
  fun castList_whenCastNull_returnsEmptyList() =
    runTest {
      personViewModel.castList.observeForever(observer)
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(
          mockDetailPerson.copy(
            credits = mockCreditsPerson.copy(
              cast = null,
            ),
          ),
        )

      personViewModel.getDetailPerson(personId)
      advanceUntilIdle()
      assertEquals(emptyList<CastItem>(), personViewModel.castList.value)

      personViewModel.castList.removeObserver(observer)
    }

  @Test
  fun castList_whenCreditsNull_returnsEmptyList() =
    runTest {
      personViewModel.castList.observeForever(observer)
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson.copy(credits = null))

      personViewModel.getDetailPerson(personId)
      advanceUntilIdle()
      assertEquals(emptyList<CastItem>(), personViewModel.castList.value)

      personViewModel.castList.removeObserver(observer)
    }

  @Test
  fun getImagePerson_whenSuccessful_emitsSuccess() {
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns
      successFlow(mockImagePerson)

    testViewModel(
      runBlock = { personViewModel.getImagePerson(personId) },
      liveData = personViewModel.imagePerson,
      expectedSuccess = listOf(mockProfilesItem),
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getImagePerson(personId) }
      },
    )
  }

  @Test
  fun getImagePerson_whenSuccessfulWithEmptyList_emitsSuccess() {
    val mockImagePerson = ImagePerson(
      profiles = listOf(),
      id = 12345,
    )
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns
      successFlow(mockImagePerson)

    testViewModel(
      runBlock = { personViewModel.getImagePerson(personId) },
      liveData = personViewModel.imagePerson,
      expectedSuccess = listOf(),
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getImagePerson(personId) }
      },
    )
  }

  @Test
  fun getImagePerson_whenSuccessfulWithNullProfiles_emitsSuccess() {
    val mockImagePerson = ImagePerson(
      profiles = null,
      id = 12345,
    )
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns
      successFlow(mockImagePerson)

    testViewModel(
      runBlock = { personViewModel.getImagePerson(personId) },
      liveData = personViewModel.imagePerson,
      expectedSuccess = emptyList(),
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getImagePerson(personId) }
      },
    )
  }

  @Test
  fun getImagePerson_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns errorFlow

      testViewModel(
        runBlock = { personViewModel.getImagePerson(personId) },
        liveData = personViewModel.imagePerson,
        expectError = errorMessage,
        verifyBlock = {
          coVerify { getDetailPersonUseCase.getImagePerson(personId) }
        },
      )
    }

  @Test
  fun executeUseCase_whenError_shouldUpdateErrorStateAndLoadingState() =
    runTest {
      val errorMessage = "Something went wrong"
      val flow = flowOf(Outcome.Error(errorMessage))

      personViewModel.executeUseCase(
        flowProvider = { flow },
        onSuccess = {},
      )
      advanceUntilIdle()

      assertFalse(personViewModel.loadingState.value == true)
      assertEquals(errorMessage, personViewModel.errorState.value?.getContentIfNotHandled())
    }

  @Test
  fun executeUseCase_whenLoading_withDefaultOnLoading_shouldDoNothing() =
    runTest {
      val flow = flowOf(Outcome.Loading)

      // Don't pass onLoading — uses the default
      personViewModel.executeUseCase(
        flowProvider = { flow },
        onSuccess = {},
        // onLoading not passed intentionally
      )

      // Just verify nothing crashes and no state changes occurred
      assertFalse(personViewModel.loadingState.value == true)
      assertNull(personViewModel.errorState.value)
    }
}
