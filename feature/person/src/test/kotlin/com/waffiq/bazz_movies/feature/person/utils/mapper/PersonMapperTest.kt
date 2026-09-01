package com.waffiq.bazz_movies.feature.person.utils.mapper

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ProfilesItemResponse
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CombinedCreditPerson
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ImagePerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.castItemResponse
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.combinedCreditResponse
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.externalIDPersonResponse
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.listOfProfilesItemResponse
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.mapCastList
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.mapImageList
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toCombinedCredit
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toDetailPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toExternalIDPerson
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.toImagePerson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PersonMapperTest {

  @Test
  fun toCombinedCredit_withValidValue_returnsCombinedCredit() {
    val combinedCredit = combinedCreditResponse.toCombinedCredit()
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
  fun toCombinedCredit_withNullValue_returnsCombinedCredit() {
    val response = CombinedCreditResponse(cast = null, crew = null)

    val combinedCredit = response.toCombinedCredit()
    assertNull(combinedCredit.crew)
    assertNull(combinedCredit.cast)
  }

  @Test
  fun toCastItem_withValidValue_returnsCastItem() {
    val combinedCreditResponse = CombinedCreditResponse(
      cast = listOf(castItemResponse),
      crew = null,
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
  fun toDetailPerson_withValidValue_returnsDetailPerson() {
    val response = DetailPersonResponse(
      imdbId = "nm123456",
      name = "Silverst",
      gender = 2,
      images = ImagePersonResponse(listOf(ProfilesItemResponse())),
    )
    val detailPerson = response.toDetailPerson()
    assertEquals("nm123456", detailPerson.imdbId)
    assertEquals("Silverst", detailPerson.name)
    assertEquals(2, detailPerson.gender)
    assertNotNull(detailPerson.images)
  }

  @Test
  fun toDetailPerson_withNullValue_returnsNull() {
    val detailPerson = DetailPersonResponse().toDetailPerson()
    assertNull(detailPerson.alsoKnownAs)
    assertNull(detailPerson.birthday)
    assertNull(detailPerson.gender)
    assertNull(detailPerson.imdbId)
    assertNull(detailPerson.knownForDepartment)
    assertNull(detailPerson.profilePath)
    assertNull(detailPerson.biography)
    assertNull(detailPerson.deathday)
    assertNull(detailPerson.placeOfBirth)
    assertNull(detailPerson.popularity)
    assertNull(detailPerson.name)
    assertNull(detailPerson.id)
    assertNull(detailPerson.adult)
    assertNull(detailPerson.homepage)
    assertNull(detailPerson.credits)
    assertNull(detailPerson.externalIds)
    assertNull(detailPerson.images)
  }

  @Test
  fun toImagePerson_withValidValue_returnsImagePerson() {
    val response = ImagePersonResponse(profiles = listOfProfilesItemResponse)
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
  fun toImagePerson_whenProfilesIsNull_returnsImagePerson() {
    val response = ImagePersonResponse(profiles = null)
    val imagePerson = response.toImagePerson()

    assertNull(imagePerson.profiles)
  }

  @Test
  fun toExternalIDPerson_withValidValue_returnsExternalIDPerson() {
    val externalID = externalIDPersonResponse.toExternalIDPerson()
    assertEquals("nm12345", externalID.imdbId)
    assertEquals("instagram_id", externalID.instagramId)
    assertEquals("twitter_id", externalID.twitterId)
  }

  @Test
  fun mapCastList_whenStateIsSuccess_returnsCast() {
    val cast = listOf(CastItem(id = 1, name = "Actor A"))
    val detailPerson = DetailPerson(credits = CombinedCreditPerson(cast = cast))
    val state = UIState.Success(detailPerson)

    assertEquals(cast, mapCastList(state))
  }

  @Test
  fun mapCastList_whenCreditsIsNull_returnsEmptyList() {
    val detailPerson = DetailPerson(credits = null)
    val state = UIState.Success(detailPerson)

    assertEquals(emptyList<CastItem>(), mapCastList(state))
  }

  @Test
  fun mapCastList_whenStateIsNotSuccess_returnsEmptyList() {
    assertEquals(emptyList<CastItem>(), mapCastList(UIState.Loading))
    assertEquals(emptyList<CastItem>(), mapCastList(UIState.Error("fail")))
    assertEquals(emptyList<CastItem>(), mapCastList(UIState.Idle))
  }

  @Test
  fun mapImageList_whenStateIsSuccess_returnsProfiles() {
    val profiles = listOf(ProfilesItem(filePath = "/a.jpg"))
    val detailPerson = DetailPerson(images = ImagePerson(profiles = profiles))
    val state = UIState.Success(detailPerson)

    assertEquals(profiles, mapImageList(state))
  }
}
