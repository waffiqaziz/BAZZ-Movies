package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCastResponseItem
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.castcrew.MediaCrewResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaCastMapperTest {

  @Test
  fun toMediaCastItem_withValidValues_returnsDomain() {
    val response = MediaCastResponseItem(
      castId = 1,
      character = "Main Character",
      gender = 2,
      creditId = "credit123",
      knownForDepartment = "Acting",
      originalName = "John Original",
      popularity = 9.5,
      name = "John Doe",
      profilePath = "/john.jpg",
      id = 101,
      adult = false,
      order = 0
    )

    val domain = with(MediaCreditsMapper) { response.toMediaCastItem() }
    assertEquals(1, domain.castId)
    assertEquals("Main Character", domain.character)
    assertEquals(2, domain.gender)
    assertEquals("credit123", domain.creditId)
    assertEquals("Acting", domain.knownForDepartment)
    assertEquals("John Original", domain.originalName)
    assertEquals(9.5, domain.popularity)
    assertEquals("John Doe", domain.name)
    assertEquals("/john.jpg", domain.profilePath)
    assertEquals(101, domain.id)
    assertFalse(domain.adult == true)
    assertEquals(0, domain.order)
  }

  @Test
  fun toMediaCrewItem_withValidValues_returnsDomain() {
    val response = MediaCrewResponseItem(
      gender = 1,
      creditId = "crew456",
      knownForDepartment = "Directing",
      originalName = "Jane Original",
      popularity = 8.7,
      name = "Jane Smith",
      profilePath = "/jane.jpg",
      id = 202,
      adult = true,
      department = "Production",
      job = "Producer"
    )

    val domain = with(MediaCreditsMapper) { response.toMediaCrewItem() }
    assertEquals(1, domain.gender)
    assertEquals("crew456", domain.creditId)
    assertEquals("Directing", domain.knownForDepartment)
    assertEquals("Jane Original", domain.originalName)
    assertEquals(8.7, domain.popularity)
    assertEquals("Jane Smith", domain.name)
    assertEquals("/jane.jpg", domain.profilePath)
    assertEquals(202, domain.id)
    assertTrue(domain.adult == true)
    assertEquals("Production", domain.department)
    assertEquals("Producer", domain.job)
  }
}
