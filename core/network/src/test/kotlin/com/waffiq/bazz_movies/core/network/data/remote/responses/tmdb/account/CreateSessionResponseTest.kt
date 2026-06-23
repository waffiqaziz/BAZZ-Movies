package com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.account

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.createSessionResponseDump
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateSessionResponseTest {

  @Test
  fun createSessionResponse_withValidValues_setsPropertiesCorrectly() {
    val createSessionResponse = createSessionResponseDump
    assertTrue(createSessionResponse.success)
    assertEquals("session_id", createSessionResponse.sessionId)
  }
}
