package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DummyData.spokenLanguagesResponseItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SpokenLanguagesResponseItemTest {

  @Test
  fun spokenLanguagesResponseItem_withValidValues_setsPropertiesCorrectly() {
    assertEquals("Korean", spokenLanguagesResponseItem.englishName)
    assertEquals("ko", spokenLanguagesResponseItem.iso6391)
    assertEquals("한국어/조선말", spokenLanguagesResponseItem.name)
  }

  @Test
  fun spokenLanguagesResponseItem_withDefaultValues_setsPropertiesCorrectly() {
    val spokenLanguagesResponseItem = SpokenLanguagesResponseItem()
    assertNull(spokenLanguagesResponseItem.englishName)
    assertNull(spokenLanguagesResponseItem.iso6391)
    assertNull(spokenLanguagesResponseItem.name)
  }

  @Test
  fun spokenLanguagesResponseItem_withSomeNullValues_setsPropertiesCorrectly() {
    val spokenLanguagesResponseItem = SpokenLanguagesResponseItem(
      name = "Bahasa Indonesia",
    )
    assertEquals("Bahasa Indonesia", spokenLanguagesResponseItem.name)
    assertNull(spokenLanguagesResponseItem.englishName)
    assertNull(spokenLanguagesResponseItem.iso6391)
  }
}
