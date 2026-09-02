package com.waffiq.bazz_movies.core.models

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import org.junit.Test

class MediaCastItemTest {

  private val mediaCastItemValid = MediaCastItem(
    castId = 10,
    character = "John Doe",
    gender = 2,
    creditId = "abc123",
    knownForDepartment = "Acting",
    originalName = "Jonathan Doe",
    popularity = 12.5,
    name = "John D.",
    profilePath = "/profile.jpg",
    id = 200,
    adult = false,
    order = 1,
  )
  private val mediaCastItemNull = MediaCastItem()

  @Test
  fun mediaCastItem_withValidValue_returnsCorrectData() {
    assertEquals(10, mediaCastItemValid.castId)
    assertEquals("John Doe", mediaCastItemValid.character)
    assertEquals(2, mediaCastItemValid.gender)
    assertEquals("abc123", mediaCastItemValid.creditId)
    assertEquals("Acting", mediaCastItemValid.knownForDepartment)
    assertEquals("Jonathan Doe", mediaCastItemValid.originalName)
    assertEquals(12.5, mediaCastItemValid.popularity)
    assertEquals("John D.", mediaCastItemValid.name)
    assertEquals("/profile.jpg", mediaCastItemValid.profilePath)
    assertEquals(200, mediaCastItemValid.id)
    assertFalse(mediaCastItemValid.adult == true)
    assertEquals(1, mediaCastItemValid.order)
  }

  @Test
  fun mediaCastItem_withNullValue_returnsNull() {
    assertNull(mediaCastItemNull.castId)
    assertNull(mediaCastItemNull.character)
    assertNull(mediaCastItemNull.gender)
    assertNull(mediaCastItemNull.creditId)
    assertNull(mediaCastItemNull.knownForDepartment)
    assertNull(mediaCastItemNull.originalName)
    assertNull(mediaCastItemNull.popularity)
    assertNull(mediaCastItemNull.name)
    assertNull(mediaCastItemNull.profilePath)
    assertNull(mediaCastItemNull.id)
    assertNull(mediaCastItemNull.adult)
    assertNull(mediaCastItemNull.order)
  }
}
