package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.combinedCreditResponseDump
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CombinedCreditResponseTest {

  @Test
  fun combinedCreditResponse_withValidValues_setsPropertiesCorrectly() {
    val combinedCreditResponse = combinedCreditResponseDump
    assertEquals("War of the Worlds", combinedCreditResponse.cast?.get(0)?.originalTitle)
    assertEquals("The Last Samurai", combinedCreditResponse.crew?.get(0)?.originalTitle)
  }

  @Test
  fun combinedCreditResponse_withDefaultValues_setsPropertiesCorrectly() {
    val combinedCreditResponse = CombinedCreditResponse()
    assertNull(combinedCreditResponse.crew)
    assertNull(combinedCreditResponse.cast)
  }
}
