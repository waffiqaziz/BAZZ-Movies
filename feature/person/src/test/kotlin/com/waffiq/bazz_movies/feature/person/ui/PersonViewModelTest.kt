package com.waffiq.bazz_movies.feature.person.ui

import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PersonViewModelTest : BasePersonViewModelTest() {

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
      }
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
      }
    )
  }

  @Test
  fun getKnownFor_whenSuccessful_emitsSuccess() {
    coEvery { getDetailPersonUseCase.getKnownForPerson(personId) } returns
      successFlow(listOf(mockCastItem))

    testViewModel(
      runBlock = { personViewModel.getKnownFor(personId) },
      liveData = personViewModel.knownFor,
      expectedSuccess = listOf(mockCastItem),
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getKnownForPerson(personId) }
      }
    )
  }

  @Test
  fun getKnownFor_whenUnsuccessful_emitsError() = runTest {
    coEvery { getDetailPersonUseCase.getKnownForPerson(personId) } returns errorFlow

    testViewModel(
      runBlock = { personViewModel.getKnownFor(personId) },
      liveData = personViewModel.knownFor,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getKnownForPerson(personId) }
      }
    )
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
      }
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
      }
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
      }
    )
  }

  @Test
  fun getImagePerson_whenUnsuccessful_emitsError() = runTest {
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns errorFlow

    testViewModel(
      runBlock = { personViewModel.getImagePerson(personId) },
      liveData = personViewModel.imagePerson,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getImagePerson(personId) }
      }
    )
  }

  @Test
  fun getExternalIDPerson_whenSuccessful_emitsSuccess() = runTest {
    coEvery { getDetailPersonUseCase.getExternalIDPerson(personId) } returns
      successFlow(mockExternalIDPerson)

    testViewModel(
      runBlock = { personViewModel.getExternalIDPerson(personId) },
      liveData = personViewModel.externalIdPerson,
      expectedSuccess = mockExternalIDPerson,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getExternalIDPerson(personId) }
      }
    )
  }

  @Test
  fun getExternalIDPerson_whenUnsuccessful_emitsError() = runTest {
    coEvery { getDetailPersonUseCase.getExternalIDPerson(personId) } returns errorFlow

    testViewModel(
      runBlock = { personViewModel.getExternalIDPerson(personId) },
      liveData = personViewModel.externalIdPerson,
      expectError = errorMessage,
      verifyBlock = {
        coVerify { getDetailPersonUseCase.getExternalIDPerson(personId) }
      }
    )
  }
}
