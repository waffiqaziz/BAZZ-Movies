package com.waffiq.bazz_movies.feature.person.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonViewModelTest {

  private lateinit var personViewModel: PersonViewModel
  private val getDetailPersonUseCase: GetDetailPersonUseCase = mockk()

  private val personId = 1
  private val errorMessage = "Network error"

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    personViewModel = PersonViewModel(getDetailPersonUseCase)
  }

  @Test
  fun `getDetailPerson emits success`() = runTest {
    val expectedDetailPerson = DetailPerson(
      id = personId,
      name = "John Doe",
      biography = "Sample biography"
    )
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns flowOf(
      Outcome.Success(expectedDetailPerson)
    )

    // observe loading state changes
    val loadingState = mutableListOf<Boolean>()
    personViewModel.loadingState.observeForever { loadingState.add(it) }

    // observe error state changes
    val errorState = mutableListOf<Event<String>>()
    personViewModel.errorState.observeForever { errorState.add(it) }

    // observe the detail person LiveData
    val detailPersonData = mutableListOf<DetailPerson>()
    personViewModel.detailPerson.observeForever { detailPersonData.add(it) }

    personViewModel.getDetailPerson(personId)
    advanceUntilIdle()

    // assert detail person value
    val observer = mockk<Observer<DetailPerson>>(relaxed = true)
    personViewModel.detailPerson.observeForever(observer)
    assertThat(detailPersonData).containsExactly(expectedDetailPerson)
    assertThat(personViewModel.detailPerson.value).isEqualTo(expectedDetailPerson)
    assertEquals(1, detailPersonData[0].id)
    assertEquals("John Doe", detailPersonData[0].name)
    assertEquals("Sample biography", detailPersonData[0].biography)

    // assert loading states
    assertThat(loadingState).containsExactly(false)
    assertThat(personViewModel.loadingState.value).isFalse()

    // assert error state is null
    assertThat(errorState).containsExactly()
    assertThat(personViewModel.errorState.value).isNull()

    verify { observer.onChanged(expectedDetailPerson) }
    coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
  }

  @Test
  fun `getDetailPerson emits error`() = runTest {
    coEvery { getDetailPersonUseCase.getDetailPerson(personId) } returns flowOf(
      Outcome.Error(errorMessage)
    )

    val errorObserver = mockk<Observer<Event<String>>>(relaxed = true)
    personViewModel.errorState.observeForever(errorObserver)

    personViewModel.getDetailPerson(personId)
    advanceUntilIdle()

    assertThat(personViewModel.loadingState.value).isFalse()
    verify { errorObserver.onChanged(match { it.getContentIfNotHandled() == errorMessage }) }
    coVerify { getDetailPersonUseCase.getDetailPerson(personId) }
  }

  @Test
  fun `getKnownFor emits success`() = runTest {
    val castItem = CastItem(
      name = "name_person",
      id = 45678,
      originalTitle = "original_title",
    )
    val expectedCastItem =
      listOf(castItem.copy(id = 1), castItem.copy(id = 2), castItem.copy(id = 3))
    coEvery { getDetailPersonUseCase.getKnownForPerson(personId) } returns flowOf(
      Outcome.Success(expectedCastItem)
    )

    val loadingState = mutableListOf<Boolean>()
    personViewModel.loadingState.observeForever { loadingState.add(it) }

    val errorState = mutableListOf<Event<String>>()
    personViewModel.errorState.observeForever { errorState.add(it) }

    val listOfKnownFor = mutableListOf<List<CastItem>>()
    personViewModel.knownFor.observeForever { listOfKnownFor.add(it) }

    personViewModel.getKnownFor(personId)
    advanceUntilIdle()

    val observer = mockk<Observer<List<CastItem>>>(relaxed = true)
    personViewModel.knownFor.observeForever(observer)
    assertThat(listOfKnownFor).containsExactly(expectedCastItem)
    assertThat(personViewModel.knownFor.value).isEqualTo(expectedCastItem)
    assertEquals(1, listOfKnownFor[0][0].id)
    assertEquals(2, listOfKnownFor[0][1].id)
    assertEquals(3, listOfKnownFor[0][2].id)
    assertEquals("name_person", listOfKnownFor[0][0].name)
    assertEquals("original_title", listOfKnownFor[0][0].originalTitle)

    assertThat(loadingState).containsExactly()
    assertThat(personViewModel.loadingState.value).isNull()

    assertThat(errorState).containsExactly()
    assertThat(personViewModel.errorState.value).isNull()

    verify { observer.onChanged(expectedCastItem) }
    coVerify { getDetailPersonUseCase.getKnownForPerson(personId) }
  }

  @Test
  fun `getKnownFor emits error`() = runTest {
    coEvery { getDetailPersonUseCase.getKnownForPerson(personId) } returns flowOf(
      Outcome.Error(errorMessage)
    )

    val errorObserver = mockk<Observer<Event<String>>>(relaxed = true)
    personViewModel.errorState.observeForever(errorObserver)

    personViewModel.getKnownFor(personId)
    advanceUntilIdle()

    assertThat(personViewModel.loadingState.value).isFalse()
    verify { errorObserver.onChanged(match { it.getContentIfNotHandled() == errorMessage }) }
    coVerify { getDetailPersonUseCase.getKnownForPerson(personId) }
  }

  @Test
  fun `getImagePerson emits success`() = runTest {
    val profilesItem = ProfilesItem(
      aspectRatio = 0.667,
      filePath = "/file_path_profile.jpg",
      voteAverage = 5.318f,
      width = 300,
      height = 450,
    )
    val expectedImagePerson = ImagePerson(
      profiles = listOf(profilesItem),
      id = 12345,
    )
    val listProfilesItem = listOf(profilesItem)
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns flowOf(
      Outcome.Success(expectedImagePerson)
    )

    val loadingState = mutableListOf<Boolean>()
    personViewModel.loadingState.observeForever { loadingState.add(it) }

    val errorState = mutableListOf<Event<String>>()
    personViewModel.errorState.observeForever { errorState.add(it) }

    val listImagePerson = mutableListOf<List<ProfilesItem>>()
    personViewModel.imagePerson.observeForever { listImagePerson.add(it) }

    personViewModel.getImagePerson(personId)
    advanceUntilIdle()

    val observer = mockk<Observer<List<ProfilesItem>>>(relaxed = true)
    personViewModel.imagePerson.observeForever(observer)
    assertThat(listImagePerson).containsExactly(expectedImagePerson.profiles)
    assertThat(personViewModel.imagePerson.value).isEqualTo(expectedImagePerson.profiles)
    assertEquals(0.667, listImagePerson[0][0].aspectRatio)
    assertEquals("/file_path_profile.jpg", listImagePerson[0][0].filePath)
    assertEquals(5.318f, listImagePerson[0][0].voteAverage)
    assertEquals(300, listImagePerson[0][0].width)
    assertEquals(450, listImagePerson[0][0].height)

    assertThat(loadingState).containsExactly()
    assertThat(personViewModel.loadingState.value).isNull()

    assertThat(errorState).containsExactly()
    assertThat(personViewModel.errorState.value).isNull()

    verify { observer.onChanged(listProfilesItem) }
    coVerify { getDetailPersonUseCase.getImagePerson(personId) }
  }

  @Test
  fun `getImagePerson emits error`() = runTest {
    coEvery { getDetailPersonUseCase.getImagePerson(personId) } returns flowOf(
      Outcome.Error(errorMessage)
    )

    val errorObserver = mockk<Observer<Event<String>>>(relaxed = true)
    personViewModel.errorState.observeForever(errorObserver)

    personViewModel.getImagePerson(personId)
    advanceUntilIdle()

    assertThat(personViewModel.loadingState.value).isFalse()

    verify { errorObserver.onChanged(match { it.getContentIfNotHandled() == errorMessage }) }
    coVerify { getDetailPersonUseCase.getImagePerson(personId) }
  }

  @Test
  fun `getExternalIDPerson emits success`() = runTest {
    val expectedExternalIDPerson = ExternalIDPerson(
      id = 3254153,
      instagramId = "instagram_id"
    )
    coEvery { getDetailPersonUseCase.getExternalIDPerson(personId) } returns flowOf(
      Outcome.Success(expectedExternalIDPerson)
    )

    val loadingState = mutableListOf<Boolean>()
    personViewModel.loadingState.observeForever { loadingState.add(it) }

    val errorState = mutableListOf<Event<String>>()
    personViewModel.errorState.observeForever { errorState.add(it) }

    val externalIDPerson = mutableListOf<ExternalIDPerson>()
    personViewModel.externalIdPerson.observeForever { externalIDPerson.add(it) }

    personViewModel.getExternalIDPerson(personId)
    advanceUntilIdle()

    val observer = mockk<Observer<ExternalIDPerson>>(relaxed = true)
    personViewModel.externalIdPerson.observeForever(observer)
    assertThat(externalIDPerson).containsExactly(expectedExternalIDPerson)
    assertThat(personViewModel.externalIdPerson.value).isEqualTo(expectedExternalIDPerson)
    assertEquals(3254153, externalIDPerson[0].id)
    assertEquals("instagram_id", externalIDPerson[0].instagramId)

    assertThat(loadingState).containsExactly()
    assertThat(personViewModel.loadingState.value).isNull()

    assertThat(errorState).containsExactly()
    assertThat(personViewModel.errorState.value).isNull()

    verify { observer.onChanged(expectedExternalIDPerson) }
    coVerify { getDetailPersonUseCase.getExternalIDPerson(personId) }
  }

  @Test
  fun `getExternalIDPerson emits error`() = runTest {
    coEvery { getDetailPersonUseCase.getExternalIDPerson(personId) } returns flowOf(
      Outcome.Error(errorMessage)
    )

    val errorObserver = mockk<Observer<Event<String>>>(relaxed = true)
    personViewModel.errorState.observeForever(errorObserver)

    personViewModel.getExternalIDPerson(personId)
    advanceUntilIdle()

    assertThat(personViewModel.loadingState.value).isFalse()

    // Verify calls to the use case
    verify { errorObserver.onChanged(match { it.getContentIfNotHandled() == errorMessage }) }
    coVerify { getDetailPersonUseCase.getExternalIDPerson(personId) }
  }
}
