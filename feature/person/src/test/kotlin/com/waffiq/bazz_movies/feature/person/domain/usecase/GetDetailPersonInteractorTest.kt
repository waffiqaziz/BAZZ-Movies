package com.waffiq.bazz_movies.feature.person.domain.usecase

import app.cash.turbine.test
import com.waffiq.bazz_movies.core.domain.Outcome
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ExternalIDPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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

  private val castItem = CastItem(
    firstAirDate = "2014-02-17",
    overview = "lorem_ipsum",
    originalLanguage = "en",
    episodeCount = 1,
    listGenreIds = listOf(80, 28, 53),
    posterPath = "/poser_path.jpg",
    listOriginCountry = listOf("origin_country"),
    backdropPath = "/backdrop_path.jpg",
    character = "character_name",
    creditId = "credit_id",
    mediaType = "movie",
    originalName = "original_name",
    popularity = 63992.0,
    voteAverage = 9f,
    name = "name",
    id = 12345,
    adult = false,
    voteCount = 500,
    originalTitle = "original_title",
    video = true,
    title = "title",
    releaseDate = "2003-01-26",
    order = 3,
  )
  private val combinedCreditPerson = CombinedCreditPerson(
    cast = listOf(castItem),
    id = 12345678,
    crew = null
  )

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

  private val externalIdPerson = ExternalIDPerson(
    imdbId = "imdb_id",
    freebaseMid = "freebase_m_id",
    tiktokId = "tiktok_id",
    wikidataId = "wikidata_id",
    id = 1234567890,
    freebaseId = "freebase_id",
    twitterId = "twitter_id",
    youtubeId = "youtube_id",
    tvrageId = "tv_rage_id",
    facebookId = "facebook_id",
    instagramId = "instagram_id",
  )

  // Mock dependencies
  private val mockRepository: IPersonRepository = mockk()
  private lateinit var getDetailPersonInteractor: GetDetailPersonInteractor

  @Before
  fun setup() {
    getDetailPersonInteractor = GetDetailPersonInteractor(mockRepository)
  }

  @Test
  fun getDetailPerson_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(detailPerson))
    coEvery { mockRepository.getDetailPerson(personId) } returns flow

    getDetailPersonInteractor.getDetailPerson(personId).test {
      // Assert the first emission is success with correct data
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertEquals(listOf("name1", "name2"), emission.data.alsoKnownAs)
      assertEquals("2008-01-22", emission.data.birthday)
      assertEquals(2, emission.data.gender)
      assertEquals("nm0001234", emission.data.imdbId)
      assertEquals("Acting", emission.data.knownForDepartment)
      assertEquals("/12345.jpg", emission.data.profilePath)
      assertEquals("lorem_ipsum", emission.data.biography)
      assertEquals("2024-01-22", emission.data.deathday)
      assertEquals("Some place, Earth", emission.data.placeOfBirth)
      assertEquals(18.492F, emission.data.popularity)
      assertEquals("person_name", emission.data.name)
      assertEquals(12345, emission.data.id)
      assertEquals(false, emission.data.adult)
      assertEquals(null, emission.data.homepage)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getDetailPerson(personId) }
    coVerify { mockRepository.getDetailPerson(personId) }
  }

  @Test
  fun getDetailPerson_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    coEvery { mockRepository.getDetailPerson(personId) } returns flow

    getDetailPersonInteractor.getDetailPerson(personId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getDetailPerson(personId) }
  }

  @Test
  fun getKnownForPerson_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(combinedCreditPerson))
    coEvery { mockRepository.getKnownForPerson(personId) } returns flow

    getDetailPersonInteractor.getKnownForPerson(personId).test {
      // Assert the first emission is success with correct data
      val emission = awaitItem() // NetworkResult<List<CastItem>>

      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertEquals("2014-02-17", emission.data[0].firstAirDate)
      assertEquals("lorem_ipsum", emission.data[0].overview)
      assertEquals("en", emission.data[0].originalLanguage)
      assertEquals(1, emission.data[0].episodeCount)
      assertEquals(listOf(80, 28, 53), emission.data[0].listGenreIds)
      assertEquals("/poser_path.jpg", emission.data[0].posterPath)
      assertEquals(listOf("origin_country"), emission.data[0].listOriginCountry)
      assertEquals("/backdrop_path.jpg", emission.data[0].backdropPath)
      assertEquals("character_name", emission.data[0].character)
      assertEquals("credit_id", emission.data[0].creditId)
      assertEquals("movie", emission.data[0].mediaType)
      assertEquals("original_name", emission.data[0].originalName)
      assertEquals(63992.0, emission.data[0].popularity)
      assertEquals(9f, emission.data[0].voteAverage)
      assertEquals("name", emission.data[0].name)
      assertEquals(12345, emission.data[0].id)
      assertFalse(emission.data[0].adult)
      assertEquals(500, emission.data[0].voteCount)
      assertEquals("original_title", emission.data[0].originalTitle)
      assertTrue(emission.data[0].video)
      assertEquals("title", emission.data[0].title)
      assertEquals("2003-01-26", emission.data[0].releaseDate)
      assertEquals(3, emission.data[0].order)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getKnownForPerson(personId) }
    coVerify { mockRepository.getKnownForPerson(personId) }
  }

  @Test
  fun getKnownForPerson_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    coEvery { mockRepository.getKnownForPerson(personId) } returns flow

    getDetailPersonInteractor.getKnownForPerson(personId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getKnownForPerson(personId) }
  }

  @Test
  fun getKnownForPerson_whenSuccessful_sortsCastItemsByVoteCountDescending() = runTest {
    // Create a list of CastItem with varying voteCount values
    val castItems = listOf(
      castItem.copy(voteCount = 100, id = 1),
      castItem.copy(voteCount = 500, id = 2),
      castItem.copy(voteCount = 300, id = 3)
    )

    // Expected sorted order
    val sortedCastItems = castItems.sortedByDescending { it.voteCount }

    // Create a CombinedCreditPerson with the cast items
    val combinedCreditPerson = CombinedCreditPerson(cast = castItems, id = 12345678, crew = null)

    // Mock the repository to return the unsorted list wrapped in NetworkResult.Success
    val flow = flowOf(Outcome.Success(combinedCreditPerson))
    coEvery { mockRepository.getKnownForPerson(personId) } returns flow

    // Test the interactor's getKnownForPerson method
    getDetailPersonInteractor.getKnownForPerson(personId).test {
      // Assert the first emission is success with sorted data
      val emission = awaitItem() // NetworkResult<List<CastItem>>

      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success

      // Assert the sorted order matches the expected order
      assertEquals(sortedCastItems, emission.data)

      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getKnownForPerson(personId) }
  }

  @Test
  fun getImagePerson_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(imagePerson))
    coEvery { mockRepository.getImagePerson(personId) } returns flow

    getDetailPersonInteractor.getImagePerson(personId).test {
      // Assert the first emission is success with correct data
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertEquals(listOf(profilesItem), emission.data.profiles)
      assertEquals(12345, emission.data.id)
      assertEquals(0.667, emission.data.profiles?.get(0)?.aspectRatio)
      assertEquals("/file_path.jpg", emission.data.profiles?.get(0)?.filePath)
      assertEquals(5.318f, emission.data.profiles?.get(0)?.voteAverage)
      assertEquals(300, emission.data.profiles?.get(0)?.width)
      assertEquals(null, emission.data.profiles?.get(0)?.iso6391)
      assertEquals(3, emission.data.profiles?.get(0)?.voteCount)
      assertEquals(450, emission.data.profiles?.get(0)?.height)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getImagePerson(personId) }
    coVerify { mockRepository.getImagePerson(personId) }
  }

  @Test
  fun getImagePerson_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    coEvery { mockRepository.getImagePerson(personId) } returns flow

    getDetailPersonInteractor.getImagePerson(personId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getImagePerson(personId) }
  }

  @Test
  fun getExternalIDPerson_whenSuccessful_emitsSuccess() = runTest {
    val flow = flowOf(Outcome.Success(externalIdPerson))
    coEvery { mockRepository.getExternalIDPerson(personId) } returns flow

    getDetailPersonInteractor.getExternalIDPerson(personId).test {
      // Assert the first emission is success with correct data
      val emission = awaitItem()
      assertTrue(emission is Outcome.Success)
      emission as Outcome.Success
      assertEquals("imdb_id", emission.data.imdbId)
      assertEquals("freebase_m_id", emission.data.freebaseMid)
      assertEquals("tiktok_id", emission.data.tiktokId)
      assertEquals("wikidata_id", emission.data.wikidataId)
      assertEquals(1234567890, emission.data.id)
      assertEquals("freebase_id", emission.data.freebaseId)
      assertEquals("twitter_id", emission.data.twitterId)
      assertEquals("youtube_id", emission.data.youtubeId)
      assertEquals("tv_rage_id", emission.data.tvrageId)
      assertEquals("facebook_id", emission.data.facebookId)
      assertEquals("instagram_id", emission.data.instagramId)

      // Assert no further emissions
      awaitComplete()
    }

    // Verify repository interaction
    coVerify(exactly = 1) { mockRepository.getExternalIDPerson(personId) }
    coVerify { mockRepository.getExternalIDPerson(personId) }
  }

  @Test
  fun getExternalIDPerson_whenUnsuccessful_emitsError() = runTest {
    val flow = flowOf(Outcome.Error(message = errorMessage))
    coEvery { mockRepository.getExternalIDPerson(personId) } returns flow

    getDetailPersonInteractor.getExternalIDPerson(personId).test {
      val emission = awaitItem()
      assertTrue(emission is Outcome.Error)
      emission as Outcome.Error
      assertEquals(errorMessage, emission.message)
      awaitComplete()
    }
    coVerify { mockRepository.getExternalIDPerson(personId) }
  }
}
