package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExternalIDPersonTest {

  @Test
  fun externalIdPerson_withNullValue_returnsDefaultValue() {
    val externalIDPerson = ExternalIDPerson()

    assertNull(externalIDPerson.imdbId)
    assertNull(externalIDPerson.freebaseMid)
    assertNull(externalIDPerson.tiktokId)
    assertNull(externalIDPerson.wikidataId)
    assertNull(externalIDPerson.id)
    assertNull(externalIDPerson.freebaseId)
    assertNull(externalIDPerson.twitterId)
    assertNull(externalIDPerson.youtubeId)
    assertNull(externalIDPerson.tvrageId)
    assertNull(externalIDPerson.facebookId)
    assertNull(externalIDPerson.instagramId)
  }

  @Test
  fun hasAnySocialMediaIds_whenCheckingVariousIds_returnsExpectedResult() {
    // should return true if there's id
    assertTrue(ExternalIDPerson(instagramId = "instagramId").hasAnySocialMediaIds())
    assertTrue(ExternalIDPerson(twitterId = "twitterId").hasAnySocialMediaIds())
    assertTrue(ExternalIDPerson(facebookId = "facebookId").hasAnySocialMediaIds())
    assertTrue(ExternalIDPerson(tiktokId = "tiktokId").hasAnySocialMediaIds())
    assertTrue(ExternalIDPerson(youtubeId = "youtubeId").hasAnySocialMediaIds())

    // should return false if there's no id at all
    assertFalse(ExternalIDPerson().hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson(instagramId = "").hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson(twitterId = "").hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson(facebookId = "").hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson(tiktokId = "").hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson(youtubeId = "").hasAnySocialMediaIds())
    assertFalse(ExternalIDPerson().hasAnySocialMediaIds())

    // all id is available
    assertTrue(
      ExternalIDPerson(
        youtubeId = "youtubeId",
        instagramId = "instagramId",
        facebookId = "facebookId",
        tiktokId = "tiktokId",
        twitterId = "twitterId",
      ).hasAnySocialMediaIds(),
    )
  }
}
