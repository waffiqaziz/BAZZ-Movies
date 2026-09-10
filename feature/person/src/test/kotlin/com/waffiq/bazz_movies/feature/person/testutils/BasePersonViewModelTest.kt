package com.waffiq.bazz_movies.feature.person.testutils

import com.waffiq.bazz_movies.core.model.Outcome
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.CrewItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule

abstract class BasePersonViewModelTest {

  protected lateinit var personViewModel: PersonViewModel

  protected val getDetailPersonUseCase: GetDetailPersonUseCase = mockk()

  protected val personId = 1
  protected val errorMessage = "Network error"

  protected val mockCastItem = CastItem(
    name = "name_person",
    id = 45678,
    originalTitle = "original_title",
  )
  protected val mockCrewItem = CrewItem(
    id = 4314,
    department = "director",
    title = "movie title",
  )
  protected val mockCreditsPerson = CombinedCreditPerson(
    cast = listOf(mockCastItem),
    crew = listOf(mockCrewItem),
  )
  protected val mockProfilesItem = ProfilesItem(
    aspectRatio = 0.667,
    filePath = "/file_path_profile.jpg",
    voteAverage = 5.318f,
    width = 300,
    height = 450,
  )
  protected val mockImagePerson = ImagePerson(profiles = listOf(mockProfilesItem))
  protected val mockDetailPerson = DetailPerson(
    id = personId,
    name = "John Doe",
    biography = "Sample biography",
    credits = mockCreditsPerson,
    images = mockImagePerson,
  )

  // No more LiveData -> no InstantTaskExecutorRule needed
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    personViewModel = PersonViewModel(getDetailPersonUseCase)
  }

  protected fun successFlow(data: DetailPerson): Flow<Outcome<DetailPerson>> =
    flowOf(Outcome.Success(data))

  protected fun errorFlow(message: String = errorMessage): Flow<Outcome<DetailPerson>> =
    flowOf(Outcome.Error(message))

  protected fun loadingThenSuccessFlow(data: DetailPerson): Flow<Outcome<DetailPerson>> =
    flow {
      emit(Outcome.Loading)
      emit(Outcome.Success(data))
    }
}
