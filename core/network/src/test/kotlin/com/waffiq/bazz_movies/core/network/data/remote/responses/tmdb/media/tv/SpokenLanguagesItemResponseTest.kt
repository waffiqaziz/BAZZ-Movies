package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.media.tv

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.spokenLanguagesItemResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class SpokenLanguagesItemResponseTest {

  @Test
  fun spokenLanguagesItemResponse_withValidValues_setsPropertiesCorrectly() {
    val spokenLanguagesItemResponse = spokenLanguagesItemResponseDump
    assertEquals("Korean", spokenLanguagesItemResponse.englishName)
    assertEquals("ko", spokenLanguagesItemResponse.iso6391)
    assertEquals("한국어/조선말", spokenLanguagesItemResponse.name)
  }

  @Test
  fun spokenLanguagesItemResponse_withDefaultValues_setsPropertiesCorrectly() {
    val spokenLanguagesItemResponse = SpokenLanguagesResponseItem()
    assertNull(spokenLanguagesItemResponse.englishName)
    assertNull(spokenLanguagesItemResponse.iso6391)
    assertNull(spokenLanguagesItemResponse.name)
  }

  @Test
  fun spokenLanguagesItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val spokenLanguagesItemResponse = SpokenLanguagesResponseItem(
      name = "Bahasa Indonesia",
    )
    assertEquals("Bahasa Indonesia", spokenLanguagesItemResponse.name)
    assertNull(spokenLanguagesItemResponse.englishName)
    assertNull(spokenLanguagesItemResponse.iso6391)
  }
}
