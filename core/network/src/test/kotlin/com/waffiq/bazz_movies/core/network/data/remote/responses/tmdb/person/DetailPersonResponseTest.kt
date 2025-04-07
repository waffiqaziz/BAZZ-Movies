package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.personResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DetailPersonResponseTest {

  @Test
  fun detailPersonResponse_withValidValues_setsPropertiesCorrectly() {
    val detailPersonResponse = personResponseDump
    assertEquals("希斯·萊傑", detailPersonResponse.alsoKnownAs?.get(3))
    assertEquals("1979-04-04", detailPersonResponse.birthday)
    assertEquals(2, detailPersonResponse.gender)
    assertEquals("nm0005132", detailPersonResponse.imdbId)
    assertEquals("Acting", detailPersonResponse.knownForDepartment)
    assertEquals("/AdWKVqyWpkYSfKE5Gb2qn8JzHni.jpg", detailPersonResponse.profilePath)
    assertThat(
      detailPersonResponse.biography,
      containsString("Heath Andrew Ledger (April 4, 1979 – January 22, 2008)")
    )
    assertEquals("2008-01-22", detailPersonResponse.deathday)
    assertEquals("Perth, Western Australia, Australia", detailPersonResponse.placeOfBirth)
    assertEquals(29.537f, detailPersonResponse.popularity)
    assertEquals("Heath Ledger", detailPersonResponse.name)
    assertEquals(1810, detailPersonResponse.id)
    assertTrue(detailPersonResponse.adult == false)
    assertNull(detailPersonResponse.homepage)
  }

  @Test
  fun detailPersonResponse_withDefaultValues_setsPropertiesCorrectly() {
    val detailPersonResponse = DetailPersonResponse()
    assertNull(detailPersonResponse.alsoKnownAs)
    assertNull(detailPersonResponse.birthday)
    assertNull(detailPersonResponse.gender)
    assertNull(detailPersonResponse.imdbId)
    assertNull(detailPersonResponse.knownForDepartment)
    assertNull(detailPersonResponse.profilePath)
    assertNull(detailPersonResponse.biography)
    assertNull(detailPersonResponse.deathday)
    assertNull(detailPersonResponse.placeOfBirth)
    assertNull(detailPersonResponse.popularity)
    assertNull(detailPersonResponse.name)
    assertNull(detailPersonResponse.id)
    assertNull(detailPersonResponse.adult)
    assertNull(detailPersonResponse.homepage)
  }

  @Test
  fun detailPersonResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val detailPersonResponse = DetailPersonResponse(
      name = null,
      id = 2
    )
    assertNull(detailPersonResponse.name)
    assertEquals(2, detailPersonResponse.id)
  }
}
