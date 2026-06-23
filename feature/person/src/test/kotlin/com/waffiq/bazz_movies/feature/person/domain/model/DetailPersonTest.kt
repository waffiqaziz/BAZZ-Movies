package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailPersonTest {

  @Test
  fun detailPerson_whenInstantiatedWithNoData_returnsAllFieldsNull() {
    val detailPerson = DetailPerson()

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
  }

  @Test
  fun detailPerson_withValidData_returnsAllFieldsCorrectly() {
    val detailPerson = DetailPerson(
      alsoKnownAs = listOf("Name1", "Name2"),
      birthday = "1990-01-01",
      gender = 1,
      imdbId = "tt1234567",
      knownForDepartment = "Acting",
      profilePath = "/path/to/profile.jpg",
      biography = "biography",
      deathday = "2023-10-01",
      placeOfBirth = "Los Angeles",
      popularity = 85.6f,
      name = "Your Name",
      id = 12345,
      adult = false,
      homepage = "https://example.com",
    )

    assertEquals(listOf("Name1", "Name2"), detailPerson.alsoKnownAs)
    assertEquals("1990-01-01", detailPerson.birthday)
    assertEquals(1, detailPerson.gender)
    assertEquals("tt1234567", detailPerson.imdbId)
    assertEquals("Acting", detailPerson.knownForDepartment)
    assertEquals("/path/to/profile.jpg", detailPerson.profilePath)
    assertEquals("biography", detailPerson.biography)
    assertEquals("2023-10-01", detailPerson.deathday)
    assertEquals("Los Angeles", detailPerson.placeOfBirth)
    assertEquals(85.6f, detailPerson.popularity)
    assertEquals("Your Name", detailPerson.name)
    assertEquals(12345, detailPerson.id)
    assertEquals(false, detailPerson.adult)
    assertEquals("https://example.com", detailPerson.homepage)
  }
}
