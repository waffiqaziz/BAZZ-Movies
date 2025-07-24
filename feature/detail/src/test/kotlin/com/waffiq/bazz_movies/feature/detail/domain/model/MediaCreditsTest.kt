package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.domain.MediaCastItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaCreditsTest {

  @Test
  fun createMediaCredits_withRequiredParameters_createsInstanceSuccessfully() {
    val castList = listOf(MediaCastItem(name = "Actor 1"))
    val crewList = listOf(MediaCrewItem(name = "Director 1"))
    val credits = MediaCredits(cast = castList, crew = crewList)

    assertEquals(castList, credits.cast)
    assertNull(credits.id)
    assertEquals(crewList, credits.crew)
  }

  @Test
  fun createMediaCredits_withAllParameters_createsInstanceSuccessfully() {
    val castList = listOf(MediaCastItem(name = "Actor 1"))
    val crewList = listOf(MediaCrewItem(name = "Director 1"))
    val credits = MediaCredits(cast = castList, id = 123, crew = crewList)

    assertEquals(castList, credits.cast)
    assertEquals(123, credits.id)
    assertEquals(crewList, credits.crew)
  }

  @Test
  fun createMediaCredits_withEmptyLists_createsInstanceSuccessfully() {
    val credits = MediaCredits(cast = emptyList(), crew = emptyList())

    assertTrue(credits.cast.isEmpty())
    assertNull(credits.id)
    assertTrue(credits.crew.isEmpty())
  }

  @Test
  fun createMediaCredits_withExplicitNullId_createsInstanceSuccessfully() {
    val castList = listOf(MediaCastItem(name = "Actor 1"))
    val crewList = listOf(MediaCrewItem(name = "Director 1"))
    val credits = MediaCredits(cast = castList, id = null, crew = crewList)

    assertEquals(castList, credits.cast)
    assertNull(credits.id)
    assertEquals(crewList, credits.crew)
  }
}
