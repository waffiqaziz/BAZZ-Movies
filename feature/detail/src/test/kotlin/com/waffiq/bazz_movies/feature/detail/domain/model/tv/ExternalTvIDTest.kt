package com.waffiq.bazz_movies.feature.detail.domain.model.tv

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExternalTvIDTest {

  @Test
  fun createExternalTvID_withValidValues_setsPropertiesCorrectly() {
    val externalTvID = TvExternalIds(
      imdbId = "tt0903747",
      freebaseMid = "/m/03d34x8",
      tvdbId = 81189,
      freebaseId = "/en/breaking_bad",
      id = 1396,
      twitterId = "BreakingBad_AMC",
      tvrageId = 18164,
      facebookId = "BreakingBad",
      instagramId = "breakingbad"
    )

    assertEquals("tt0903747", externalTvID.imdbId)
    assertEquals("/m/03d34x8", externalTvID.freebaseMid)
    assertEquals(81189, externalTvID.tvdbId)
    assertEquals("/en/breaking_bad", externalTvID.freebaseId)
    assertEquals(1396, externalTvID.id)
    assertEquals("BreakingBad_AMC", externalTvID.twitterId)
    assertEquals(18164, externalTvID.tvrageId)
    assertEquals("BreakingBad", externalTvID.facebookId)
    assertEquals("breakingbad", externalTvID.instagramId)
  }

  @Test
  fun createExternalTvID_withDefaultValues_setsAllPropertiesToNull() {
    val externalTvID = TvExternalIds()

    assertNull(externalTvID.imdbId)
    assertNull(externalTvID.freebaseMid)
    assertNull(externalTvID.tvdbId)
    assertNull(externalTvID.freebaseId)
    assertNull(externalTvID.id)
    assertNull(externalTvID.twitterId)
    assertNull(externalTvID.tvrageId)
    assertNull(externalTvID.facebookId)
    assertNull(externalTvID.instagramId)
  }

  @Test
  fun createExternalTvID_withPartialValues_setsSpecifiedPropertiesOnly() {
    val externalTvID = TvExternalIds(
      imdbId = "tt1234567",
      id = 5678,
      twitterId = "SomeShow"
    )

    assertEquals("tt1234567", externalTvID.imdbId)
    assertNull(externalTvID.freebaseMid)
    assertNull(externalTvID.tvdbId)
    assertNull(externalTvID.freebaseId)
    assertEquals(5678, externalTvID.id)
    assertEquals("SomeShow", externalTvID.twitterId)
    assertNull(externalTvID.tvrageId)
    assertNull(externalTvID.facebookId)
    assertNull(externalTvID.instagramId)
  }

  @Test
  fun createExternalTvID_withZeroValues_setsZeroValues() {
    val externalTvID = TvExternalIds(
      tvdbId = 0,
      id = 0,
      tvrageId = 0
    )

    assertEquals(0, externalTvID.tvdbId)
    assertEquals(0, externalTvID.id)
    assertEquals(0, externalTvID.tvrageId)
  }

  @Test
  fun createExternalTvID_withEmptyStrings_setsEmptyStrings() {
    val externalTvID = TvExternalIds(
      imdbId = "",
      freebaseMid = "",
      freebaseId = "",
      twitterId = "",
      facebookId = "",
      instagramId = ""
    )

    assertEquals("", externalTvID.imdbId)
    assertEquals("", externalTvID.freebaseMid)
    assertEquals("", externalTvID.freebaseId)
    assertEquals("", externalTvID.twitterId)
    assertEquals("", externalTvID.facebookId)
    assertEquals("", externalTvID.instagramId)
  }

  @Test
  fun createExternalTvID_withNegativeValues_setsNegativeValues() {
    val externalTvID = TvExternalIds(
      tvdbId = -1,
      id = -100,
      tvrageId = -50
    )

    assertEquals(-1, externalTvID.tvdbId)
    assertEquals(-100, externalTvID.id)
    assertEquals(-50, externalTvID.tvrageId)
  }
}
