package com.waffiq.bazz_movies.core.network.domain

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DebugConfigImplTest {

  private lateinit var debugConfig: DebugConfigImpl

  @Test
  fun isDebug_whenDebugIsTrue_returnsTrue() {
    debugConfig = DebugConfigImpl(isDebug = true)
    assertTrue(debugConfig.isDebug())
  }

  @Test
  fun isDebug_whenDebugIsFalse_returnsFalse() {
    debugConfig = DebugConfigImpl(isDebug = false)
    assertFalse(debugConfig.isDebug())
  }
}
