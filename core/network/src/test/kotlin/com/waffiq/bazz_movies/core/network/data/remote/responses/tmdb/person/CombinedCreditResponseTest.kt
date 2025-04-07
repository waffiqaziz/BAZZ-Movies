package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.combinedCreditResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class CombinedCreditResponseTest {

  @Test
  fun combinedCreditResponse_withValidValues_setsPropertiesCorrectly() {
    val combinedCreditResponse = combinedCreditResponseDump
    assertEquals("War of the Worlds", combinedCreditResponse.cast?.get(0)?.originalTitle)
    assertEquals(500, combinedCreditResponse.id)
    assertEquals("The Last Samurai", combinedCreditResponse.crew?.get(0)?.originalTitle)
  }

  @Test
  fun combinedCreditResponse_withDefaultValues_setsPropertiesCorrectly() {
    val combinedCreditResponse = CombinedCreditResponse()
    assertNull(combinedCreditResponse.crew)
    assertNull(combinedCreditResponse.id)
    assertNull(combinedCreditResponse.cast)
  }

  @Test
  fun combinedCreditResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val combinedCreditResponse = CombinedCreditResponse(
      id = 83754
    )
    assertEquals(83754, combinedCreditResponse.id)
    assertNull(combinedCreditResponse.cast)
    assertNull(combinedCreditResponse.crew)
  }
}
