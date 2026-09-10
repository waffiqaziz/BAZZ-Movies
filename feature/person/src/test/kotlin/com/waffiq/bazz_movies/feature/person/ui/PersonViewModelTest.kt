package com.waffiq.bazz_movies.feature.person.ui

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.model.Outcome
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PersonViewModelTest : BasePersonViewModelTest() {

  @Test
  fun detailPersonState_whenNoIdSet_emitsIdleInitially() =
    runTest {
      personViewModel.detailPersonState.test {
        assertEquals(UIState.Idle, awaitItem())
        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun detailPersonState_whenSuccessful_emitsLoadingThenSuccess() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        loadingThenSuccessFlow(mockDetailPerson)

      personViewModel.detailPersonState.test {
        assertEquals(UIState.Idle, awaitItem())

        personViewModel.getDetailPerson(personId)

        assertEquals(UIState.Loading, awaitItem())
        assertEquals(UIState.Success(mockDetailPerson), awaitItem())

        cancelAndIgnoreRemainingEvents()
      }

      coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
    }

  @Test
  fun detailPersonState_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns errorFlow()

      personViewModel.detailPersonState.test {
        assertEquals(UIState.Idle, awaitItem())

        personViewModel.getDetailPerson(personId)

        assertEquals(UIState.Error(errorMessage), awaitItem())

        cancelAndIgnoreRemainingEvents()
      }

      coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
    }

  @Test
  fun detailPersonState_whenLoading_emitsLoading() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns flowOf(Outcome.Loading)

      personViewModel.detailPersonState.test {
        assertEquals(UIState.Idle, awaitItem())

        personViewModel.getDetailPerson(personId)

        assertEquals(UIState.Loading, awaitItem())

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun detailPersonState_whenIdCalledTwiceInSuccession_flatMapLatestUsesLatestId() =
    runTest {
      val secondPersonId = 2
      val secondPerson = mockDetailPerson.copy(id = secondPersonId, name = "Jane Doe")

      val firstPersonFlow = flow<Outcome<DetailPerson>> {
        delay(1_000)
        emit(Outcome.Success(mockDetailPerson))
      }

      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns firstPersonFlow
      coEvery { getDetailPersonUseCase.getDetailPerson(secondPersonId) } returns
        successFlow(secondPerson)

      personViewModel.detailPersonState.test {
        assertEquals(UIState.Idle, awaitItem())

        personViewModel.getDetailPerson(personId)
        personViewModel.getDetailPerson(secondPersonId)

        assertEquals(UIState.Success(secondPerson), awaitItem())

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun castList_whenDetailPersonSuccessful_returnsCorrectData() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson)

      personViewModel.castList.test {
        assertEquals(emptyList<CastItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)

        assertEquals(listOf(mockCastItem), awaitItem())

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun castList_whenCastNull_returnsEmptyList() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson.copy(credits = mockCreditsPerson.copy(cast = null)))

      personViewModel.castList.test {
        assertEquals(emptyList<CastItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)
        advanceUntilIdle()

        expectNoEvents()
        assertEquals(emptyList<CastItem>(), personViewModel.castList.value)

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun castList_whenCreditsNull_returnsEmptyList() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson.copy(credits = null))

      personViewModel.castList.test {
        assertEquals(emptyList<CastItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)
        advanceUntilIdle()

        expectNoEvents()
        assertEquals(emptyList<CastItem>(), personViewModel.castList.value)

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun imageList_whenDetailPersonSuccessful_returnsCorrectData() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson)

      personViewModel.imageList.test {
        assertEquals(emptyList<ProfilesItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)

        assertEquals(listOf(mockProfilesItem), awaitItem())

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun imageList_whenImagesNull_returnsEmptyList() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson.copy(images = null))

      personViewModel.imageList.test {
        assertEquals(emptyList<ProfilesItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)
        advanceUntilIdle()

        expectNoEvents()
        assertEquals(emptyList<ProfilesItem>(), personViewModel.imageList.value)

        cancelAndIgnoreRemainingEvents()
      }
    }

  @Test
  fun imageList_whenProfilesNull_returnsEmptyList() =
    runTest {
      coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns
        successFlow(mockDetailPerson.copy(images = mockImagePerson.copy(profiles = null)))

      personViewModel.imageList.test {
        assertEquals(emptyList<ProfilesItem>(), awaitItem())

        personViewModel.getDetailPerson(personId)
        advanceUntilIdle()

        expectNoEvents()
        assertEquals(emptyList<ProfilesItem>(), personViewModel.imageList.value)

        cancelAndIgnoreRemainingEvents()
      }
    }
}
