package com.waffiq.bazz_movies.core.network.di

import com.waffiq.bazz_movies.core.network.domain.DebugConfigImpl
import com.waffiq.bazz_movies.core.network.domain.IDebugConfig
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class NetworkConfigModuleTest {

  private lateinit var debugConfig: IDebugConfig

  @Before
  fun setup() {
    debugConfig = mockk()
  }

  @Test
  fun provideDebugConfig_isProvided_returnsDebugConfigImpl() {
    val providedConfig: IDebugConfig = NetworkConfigModule.provideDebugConfig()

    assertNotNull(providedConfig)
    assertTrue(providedConfig is DebugConfigImpl)
  }

  @Test
  fun debugConfig_isDebug_returnsDebugIsTrue() {
    every { debugConfig.isDebug() } returns true
    assertTrue(debugConfig.isDebug())

    every { debugConfig.isDebug() } returns false
    assertFalse(debugConfig.isDebug())
  }
}
