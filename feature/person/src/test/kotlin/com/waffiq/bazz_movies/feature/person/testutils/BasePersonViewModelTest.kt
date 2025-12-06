package com.waffiq.bazz_movies.feature.person.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
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
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

abstract class BasePersonViewModelTest {

  protected lateinit var personViewModel: PersonViewModel

  protected val getDetailPersonUseCase: GetDetailPersonUseCase = mockk()

  protected val personId = 1
  protected val errorMessage = "Network error"

  protected val mockDetailPerson = DetailPerson(
    id = personId,
    name = "John Doe",
    biography = "Sample biography"
  )
  protected val mockCastItem = CastItem(
    name = "name_person",
    id = 45678,
    originalTitle = "original_title",
  )
  protected val mockProfilesItem = ProfilesItem(
    aspectRatio = 0.667,
    filePath = "/file_path_profile.jpg",
    voteAverage = 5.318f,
    width = 300,
    height = 450,
  )
  protected val mockImagePerson = ImagePerson(
    profiles = listOf(mockProfilesItem),
    id = 12345,
  )
  protected val mockExternalIDPerson = ExternalIDPerson(
    id = 3254153,
    instagramId = "instagram_id"
  )

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    personViewModel = PersonViewModel(getDetailPersonUseCase)
  }

  protected val errorFlow = flowOf(Outcome.Error(errorMessage))

  protected fun <T> successFlow(data: T): Flow<Outcome.Success<T>> =
    flow {
      emit(Outcome.Success(data))
    }

  fun <T : Any> flowSuccessWithLoading(data: T) = flow {
    emit(Outcome.Loading)
    emit(Outcome.Success(data))
  }

  protected fun <T : Any> testViewModel(
    runBlock: () -> Unit,
    liveData: LiveData<T>,
    expectedSuccess: T? = null,
    expectError: String? = null,
    checkLoading: Boolean = false,
    verifyBlock: () -> Unit = {},
  ) = runTest {
    val successData = mutableListOf<T>()
    val errorEvents = mutableListOf<Event<String>>()
    val loadingStates = mutableListOf<Boolean>()

    personViewModel.loadingState.observeForever { loadingStates.add(it) }
    personViewModel.errorState.observeForever { errorEvents.add(it) }
    liveData.observeForever { data ->
      data?.let {
        successData.add(it)
      }
    }

    runBlock()
    advanceUntilIdle()

    // ✅ Verify success result
    expectedSuccess?.let {
      assertThat(successData).containsExactly(it)
      assertThat(liveData.value).isEqualTo(it)
    }

    // ✅ Verify error result
    expectError?.let {
      val error = personViewModel.errorState.value?.getContentIfNotHandled()
      assertThat(error).isEqualTo(it)
    }

    // ✅ Optional: check loading behavior
    if (checkLoading) {
      checkEventLoading(loadingStates, expectedSuccess, expectError)
    }

    // ✅ Custom verification
    verifyBlock()
  }

  private fun <T> checkEventLoading(
    collectedLoadingStates: MutableList<Boolean>,
    expectedSuccess: T? = null,
    expectError: String? = null,
  ) {
    when {
      expectedSuccess != null -> {
        assertThat(collectedLoadingStates).contains(true)
        assertThat(collectedLoadingStates.last()).isFalse()
        assertThat(personViewModel.loadingState.value).isFalse()
      }

      expectError != null -> {
        assertThat(collectedLoadingStates).contains(true)
        assertThat(collectedLoadingStates.last()).isFalse()
        assertThat(personViewModel.loadingState.value).isFalse()
      }

      else -> {
        if (collectedLoadingStates.isNotEmpty()) {
          assertThat(collectedLoadingStates).contains(true)
        }
      }
    }
  }
}
