package com.waffiq.bazz_movies.feature.person.utils.mapper

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CastItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CrewItemResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ProfilesItemResponse
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toCombinedCredit
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class PersonMapperTest {

  @Test
  fun toCombinedCredit_withValidValue_mapCorrectly() {
    val listOfCastItemResponse = listOf(
      CastItemResponse(id = 1, name = "John", voteCount = 12345),
      CastItemResponse(id = 2, name = "Rex", voteCount = 2345)
    )
    val listOfCrewItemResponse = listOf(
      CrewItemResponse(id = 1, job = "cameraman", title = "what"),
      CrewItemResponse(id = 2, job = "director", title = "why")
    )
    val response = CombinedCreditResponse(
      id = 345,
      cast = listOfCastItemResponse,
      crew = listOfCrewItemResponse
    )

    val combinedCredit = response.toCombinedCredit()
    assertEquals(345, combinedCredit.id)
    assertEquals("John", combinedCredit.cast?.get(0)?.name)
    assertEquals(12345, combinedCredit.cast?.get(0)?.voteCount)
    assertEquals("Rex", combinedCredit.cast?.get(1)?.name)
    assertEquals(2345, combinedCredit.cast?.get(1)?.voteCount)
    assertEquals("cameraman", combinedCredit.crew?.get(0)?.job)
    assertEquals("what", combinedCredit.crew?.get(0)?.title)
    assertEquals("director", combinedCredit.crew?.get(1)?.job)
    assertEquals("why", combinedCredit.crew?.get(1)?.title)
  }

  @Test
  fun toCombinedCredit_nullValue_mappedToDefaultValue() {
    val response = CombinedCreditResponse(
      id = 4376,
      cast = null,
      crew = null
    )

    val combinedCredit = response.toCombinedCredit()
    assertEquals(4376, combinedCredit.id)
    assertNull(combinedCredit.crew)
    assertNull(combinedCredit.cast)
  }

  @Test
  fun toCastItem_withValidValue_mapCorrectly() {
    val castItemResponse = CastItemResponse(
      firstAirDate = "firstAirDate",
      overview = "overview",
      originalLanguage = "originalLanguage",
      episodeCount = 12,
      genreIds = emptyList(),
      posterPath = "posterPath",
      originCountry = emptyList(),
      backdropPath = "backdropPath",
      character = "character",
      creditId = "creditId",
      mediaType = "tv",
      originalName = "originalName",
      popularity = 1234.0,
      voteAverage = 4123f,
      name = "name",
      id = null,
      adult = false,
      voteCount = null,
      originalTitle = "originalTitle",
      video = false,
      title = "title",
      releaseDate = "releaseDate",
      order = 3
    )
    val combinedCreditResponse = CombinedCreditResponse(
      id = 4376,
      cast = listOf(castItemResponse),
      crew = null
    )

    val combinedCredit = combinedCreditResponse.toCombinedCredit()
    assertEquals(12, combinedCredit.cast?.get(0)?.episodeCount)
    assertEquals("tv", combinedCredit.cast?.get(0)?.mediaType)
    assertEquals(1234.0, combinedCredit.cast?.get(0)?.popularity)
    assertEquals(4123f, combinedCredit.cast?.get(0)?.voteAverage)
    assertEquals(0, combinedCredit.cast?.get(0)?.id)
    assertEquals(0, combinedCredit.cast?.get(0)?.voteCount)
    assertEquals(3, combinedCredit.cast?.get(0)?.order)
  }

  @Test
  fun toDetailPerson_withValidValue_mapCorrectly() {
    val response = DetailPersonResponse(imdbId = "nm123456", name = "Silverst", gender = 2)
    val detailPerson = response.toDetailPerson()
    assertEquals("nm123456", detailPerson.imdbId)
    assertEquals("Silverst", detailPerson.name)
    assertEquals(2, detailPerson.gender)
  }

  @Test
  fun toImagePerson_withValidValue_mapCorrectly() {
    val listOfProfilesItemResponse = listOf(
      ProfilesItemResponse(
        width = 300,
        height = 450,
        filePath = "/file_path.jpg",
        voteCount = 98765
      ),
      ProfilesItemResponse(
        width = 300,
        height = 450,
        filePath = "/file_path2.jpg",
        voteCount = 9999
      )
    )
    val response = ImagePersonResponse(profiles = listOfProfilesItemResponse, id = 1)
    val imagePerson = response.toImagePerson()
    assertEquals(300, imagePerson.profiles?.get(0)?.width)
    assertEquals(450, imagePerson.profiles?.get(0)?.height)
    assertEquals("/file_path.jpg", imagePerson.profiles?.get(0)?.filePath)
    assertEquals(98765, imagePerson.profiles?.get(0)?.voteCount)
    assertEquals(300, imagePerson.profiles?.get(1)?.width)
    assertEquals(450, imagePerson.profiles?.get(1)?.height)
    assertEquals("/file_path2.jpg", imagePerson.profiles?.get(1)?.filePath)
    assertEquals(9999, imagePerson.profiles?.get(1)?.voteCount)
  }

  @Test
  fun toImagePerson_withSomeNullValue_mappedToDefaultValue() {
    val response = ImagePersonResponse(profiles = null, id = 4531)
    val imagePerson = response.toImagePerson()

    assertEquals(4531, imagePerson.id)
    assertNull(imagePerson.profiles)
  }

  @Test
  fun mtoExternalIDPerson_withValidValue_mapCorrectly() {
    val response = ExternalIDPersonResponse(
      imdbId = "nm12345",
      instagramId = "instagram_id",
      twitterId = "twitter_id"
    )
    val externalID = response.toExternalIDPerson()
    assertEquals("nm12345", externalID.imdbId)
    assertEquals("instagram_id", externalID.instagramId)
    assertEquals("twitter_id", externalID.twitterId)
  }
}
