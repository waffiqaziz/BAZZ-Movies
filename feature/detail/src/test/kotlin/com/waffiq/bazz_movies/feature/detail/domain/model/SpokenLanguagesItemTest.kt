package com.waffiq.bazz_movies.feature.detail.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SpokenLanguagesItemTest {

  @Test
  fun createSpokenLanguagesItem_withNoParameters_createsInstanceSuccessfully() {
    val spokenLanguage = SpokenLanguagesItem()

    assertNull(spokenLanguage.name)
    assertNull(spokenLanguage.iso6391)
    assertNull(spokenLanguage.englishName)
  }

  @Test
  fun createSpokenLanguagesItem_withAllParameters_createsInstanceSuccessfully() {
    val spokenLanguage = SpokenLanguagesItem(
      name = "Español",
      iso6391 = "es",
      englishName = "Spanish"
    )

    assertEquals("Español", spokenLanguage.name)
    assertEquals("es", spokenLanguage.iso6391)
    assertEquals("Spanish", spokenLanguage.englishName)
  }

  @Test
  fun createSpokenLanguagesItem_withOnlyName_createsInstanceSuccessfully() {
    val spokenLanguage = SpokenLanguagesItem(name = "Français")

    assertEquals("Français", spokenLanguage.name)
    assertNull(spokenLanguage.iso6391)
    assertNull(spokenLanguage.englishName)
  }

  @Test
  fun createSpokenLanguagesItem_withExplicitNulls_createsInstanceSuccessfully() {
    val spokenLanguage = SpokenLanguagesItem(
      name = null,
      iso6391 = null,
      englishName = null
    )

    assertNull(spokenLanguage.name)
    assertNull(spokenLanguage.iso6391)
    assertNull(spokenLanguage.englishName)
  }
}
