package com.waffiq.bazz_movies.core.coroutines

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class DispatcherModuleTest {

  @Test
  fun provideDefaultDispatcher_returnsDispatchersDefault() {
    val dispatcher = DispatcherModule.provideDefaultDispatcher()
    assertEquals(Dispatchers.Default, dispatcher)
  }

  @Test
  fun provideIoDispatcher_returnsDispatchersIO() {
    val dispatcher = DispatcherModule.provideIoDispatcher()
    assertEquals(Dispatchers.IO, dispatcher)
  }

  @Test
  fun provideMainDispatcher_returnsDispatchersMain() {
    val dispatcher = DispatcherModule.provideMainDispatcher()
    assertEquals(Dispatchers.Main, dispatcher)
  }
}
