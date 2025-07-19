package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.movieDump1
import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.tvShowDump1
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MediaResponseItemTest {

  @Test
  fun mediaResponseItem_withValidValues_setsPropertiesCorrectly() {
    val mediaResponseItem = movieDump1
    assertEquals(
      """
        Imprisoned in the 1940s for the double murder of his wife and her lover, upstanding banker 
        Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills 
        to work for an amoral warden. During his long stretch in prison, Dufresne comes to be 
        admired by the other inmates -- including an older prisoner named Red -- for his integrity 
        and unquenchable sense of hope.
      """.trimIndent(),
      mediaResponseItem.overview
    )
    assertEquals(false, mediaResponseItem.video)
    assertEquals("The Shawshank Redemption", mediaResponseItem.originalName)
  }

  @Test
  fun mediaResponseItem_withValidValues_setsPropertiesOriginCountryCorrectly() {
    val mediaResponseItem = tvShowDump1
    assertEquals("KR", mediaResponseItem.originCountry?.get(0))
  }
}
