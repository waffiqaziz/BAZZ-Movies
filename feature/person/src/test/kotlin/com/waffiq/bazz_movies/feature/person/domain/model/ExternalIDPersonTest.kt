package com.waffiq.bazz_movies.feature.person.domain.model

import org.junit.Assert.assertNull
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
}
