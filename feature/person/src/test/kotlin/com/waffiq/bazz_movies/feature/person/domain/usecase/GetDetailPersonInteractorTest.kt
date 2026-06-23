package com.waffiq.bazz_movies.feature.person.domain.usecase

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.waffiq.bazz_movies.core.models.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetDetailPersonInteractorTest {

  private val detailPerson = DetailPerson(
    alsoKnownAs = listOf("name1", "name2"),
    birthday = "2008-01-22",
    gender = 2,
    imdbId = "nm0001234",
    knownForDepartment = "Acting",
    profilePath = "/12345.jpg",
    biography = "lorem_ipsum",
    deathday = "2024-01-22",
    placeOfBirth = "Some place, Earth",
    popularity = 18.492F,
    name = "person_name",
    id = 12345,
    adult = false,
    homepage = null,
  )
  private val personId = 12345
  private val errorMessage = "Network error"

  private val profilesItem = ProfilesItem(
    aspectRatio = 0.667,
    filePath = "/file_path.jpg",
    voteAverage = 5.318f,
    width = 300,
    iso6391 = null,
    voteCount = 3,
    height = 450,
  )
  private val imagePerson = ImagePerson(
    profiles = listOf(profilesItem),
    id = 12345,
  )

  private val mockRepository: IPersonRepository = mockk()
  private lateinit var getDetailPersonInteractor: GetDetailPersonInteractor

  @Before
  fun setup() {
    getDetailPersonInteractor = GetDetailPersonInteractor(mockRepository)
  }

  @Test
  fun getDetailPerson_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockRepository.getDetailPerson(personId) } returns flowSuccess(detailPerson)

      getDetailPersonInteractor.getDetailPerson(personId).test {
        val result = awaitSuccessData()
        assertEquals(listOf("name1", "name2"), result.alsoKnownAs)
        assertEquals("2008-01-22", result.birthday)
        assertEquals(2, result.gender)
        assertEquals("nm0001234", result.imdbId)

        awaitComplete()
      }

      coVerify(exactly = 1) { mockRepository.getDetailPerson(personId) }
    }

  @Test
  fun getDetailPerson_whenUnsuccessful_emitsError() =
    runTest {
      coEvery { mockRepository.getDetailPerson(personId) } returns flowError

      getDetailPersonInteractor.getDetailPerson(personId).test {
        assertEquals(errorMessage, awaitOutcomeError())
        awaitComplete()
      }

      coVerify(exactly = 1) { mockRepository.getDetailPerson(personId) }
    }

  @Test
  fun getDetailPerson_whenLoading_emitsLoading() =
    runTest {
      coEvery { mockRepository.getDetailPerson(personId) } returns flowLoading

      getDetailPersonInteractor.getDetailPerson(personId).test {
        awaitOutcomeLoading()
      }

      coVerify(exactly = 1) { mockRepository.getDetailPerson(personId) }
    }

  @Test
  fun getImagePerson_whenSuccessful_emitsSuccess() =
    runTest {
      coEvery { mockRepository.getImagePerson(personId) } returns flowSuccess(imagePerson)

      getDetailPersonInteractor.getImagePerson(personId).test {
        val result = awaitSuccessData()
        assertEquals(listOf(profilesItem), result.profiles)
        assertEquals(12345, result.id)
        assertEquals(0.667, result.profiles?.get(0)?.aspectRatio)
        assertEquals("/file_path.jpg", result.profiles?.get(0)?.filePath)
        assertEquals(5.318f, result.profiles?.get(0)?.voteAverage)
        assertEquals(300, result.profiles?.get(0)?.width)
        assertEquals(null, result.profiles?.get(0)?.iso6391)
        assertEquals(3, result.profiles?.get(0)?.voteCount)
        assertEquals(450, result.profiles?.get(0)?.height)

        awaitComplete()
      }

      coVerify(exactly = 1) { mockRepository.getImagePerson(personId) }
    }

  @Test
  fun getImagePerson_whenUnsuccessful_emitsError() =
    runTest {
      val flow = flowOf(Outcome.Error(message = errorMessage))
      coEvery { mockRepository.getImagePerson(personId) } returns flow

      getDetailPersonInteractor.getImagePerson(personId).test {
        assertEquals(errorMessage, awaitOutcomeError())
        awaitComplete()
      }
      coVerify { mockRepository.getImagePerson(personId) }
    }

  private suspend fun <T> ReceiveTurbine<Outcome<T>>.awaitSuccessData(): T {
    val emission = awaitItem()
    assertTrue(emission is Outcome.Success)
    return (emission as Outcome.Success).data
  }

  private suspend fun <T> ReceiveTurbine<Outcome<T>>.awaitOutcomeError(): String {
    val emission = awaitItem()
    assertTrue(emission is Outcome.Error)
    return (emission as Outcome.Error).message
  }

  private suspend fun <T> ReceiveTurbine<Outcome<T>>.awaitOutcomeLoading() {
    val emission = awaitItem()
    assertTrue(emission is Outcome.Loading)
    awaitComplete()
  }

  private fun <T> flowSuccess(data: T) = flowOf(Outcome.Success(data))
  private val flowError = flowOf(Outcome.Error(errorMessage))
  private val flowLoading = flowOf(Outcome.Loading)
}
