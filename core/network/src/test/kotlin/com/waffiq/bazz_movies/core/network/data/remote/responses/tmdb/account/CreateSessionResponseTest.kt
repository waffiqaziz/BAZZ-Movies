package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.createSessionResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class CreateSessionResponseTest {

  @Test
  fun createSessionResponse_withValidValues_setsPropertiesCorrectly() {
    val createSessionResponse = createSessionResponseDump
    assertTrue(createSessionResponse.success)
    assertEquals("session_id", createSessionResponse.sessionId)
  }
}
