package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MediaCrewItemTest {

  @Test
  fun createMediaCrewItem_withNoParameters_createsInstanceSuccessfully() {
    val crewItem = MediaCrewItem()

    assertNull(crewItem.gender)
    assertNull(crewItem.creditId)
    assertNull(crewItem.knownForDepartment)
    assertNull(crewItem.originalName)
    assertNull(crewItem.popularity)
    assertNull(crewItem.name)
    assertNull(crewItem.profilePath)
    assertNull(crewItem.id)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
    assertNull(crewItem.job)
  }

  @Test
  fun createMediaCrewItem_withAllParameters_createsInstanceSuccessfully() {
    val crewItem = MediaCrewItem(
      gender = 1,
      creditId = "12345",
      knownForDepartment = "Directing",
      originalName = "John Doe",
      popularity = 8.5,
      name = "John Doe",
      profilePath = "/profile.jpg",
      id = 123,
      adult = false,
      department = "Directing",
      job = "Director"
    )

    assertEquals(1, crewItem.gender)
    assertEquals("12345", crewItem.creditId)
    assertEquals("Directing", crewItem.knownForDepartment)
    assertEquals("John Doe", crewItem.originalName)
    assertEquals(8.5, crewItem.popularity)
    assertEquals("John Doe", crewItem.name)
    assertEquals("/profile.jpg", crewItem.profilePath)
    assertEquals(123, crewItem.id)
    assertEquals(false, crewItem.adult)
    assertEquals("Directing", crewItem.department)
    assertEquals("Director", crewItem.job)
  }

  @Test
  fun createMediaCrewItem_withOnlyName_createsInstanceSuccessfully() {
    val crewItem = MediaCrewItem(name = "Jane Smith")

    assertNull(crewItem.gender)
    assertNull(crewItem.creditId)
    assertNull(crewItem.knownForDepartment)
    assertNull(crewItem.originalName)
    assertNull(crewItem.popularity)
    assertEquals("Jane Smith", crewItem.name)
    assertNull(crewItem.profilePath)
    assertNull(crewItem.id)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
    assertNull(crewItem.job)
  }

  @Test
  fun createMediaCrewItem_withExplicitNulls_createsInstanceSuccessfully() {
    val crewItem = MediaCrewItem(
      gender = null,
      creditId = null,
      knownForDepartment = null,
      originalName = null,
      popularity = null,
      name = null,
      profilePath = null,
      id = null,
      adult = null,
      department = null,
      job = null
    )

    assertNull(crewItem.gender)
    assertNull(crewItem.creditId)
    assertNull(crewItem.knownForDepartment)
    assertNull(crewItem.originalName)
    assertNull(crewItem.popularity)
    assertNull(crewItem.name)
    assertNull(crewItem.profilePath)
    assertNull(crewItem.id)
    assertNull(crewItem.adult)
    assertNull(crewItem.department)
    assertNull(crewItem.job)
  }
}
