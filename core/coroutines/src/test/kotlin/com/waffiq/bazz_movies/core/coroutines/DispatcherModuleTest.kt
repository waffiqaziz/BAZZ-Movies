package com.waffiq.bazz_movies.core.coroutines

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class DispatcherModuleTest {

  @Test
  fun provideDefaultDispatcher_whenCalled_returnsDefaultDispatcher() {
    val dispatcher = DispatcherModule.provideDefaultDispatcher()
    assertEquals(Dispatchers.Default, dispatcher)
  }

  @Test
  fun provideIoDispatcher_whenCalled_returnsIoDispatcher() {
    val dispatcher = DispatcherModule.provideIoDispatcher()
    assertEquals(Dispatchers.IO, dispatcher)
  }

  @Test
  fun provideMainDispatcher_whenCalled_returnsMainDispatcher() {
    val dispatcher = DispatcherModule.provideMainDispatcher()
    assertEquals(Dispatchers.Main, dispatcher)
  }
}
