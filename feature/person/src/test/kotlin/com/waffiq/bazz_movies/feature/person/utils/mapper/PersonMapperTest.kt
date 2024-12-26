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
import org.junit.Test

class PersonMapperTest {

  @Test
  fun `map CombinedCreditResponse to CombinedCredit`() {
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
  fun `map DetailPersonResponse to DetailPerson`() {
    val response = DetailPersonResponse(imdbId = "nm123456", name = "Silverst", gender = 2)
    val detailPerson = response.toDetailPerson()
    assertEquals("nm123456", detailPerson.imdbId)
    assertEquals("Silverst", detailPerson.name)
    assertEquals(2, detailPerson.gender)
  }

  @Test
  fun `map ImagePersonResponse to ImagePerson`() {
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
  fun `map ExternalIDPersonResponse to ExternalIDPerson`() {
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