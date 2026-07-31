package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.testmodule.MockNavigatorModule.provideMockNavigator
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockNavigatorModuleTest {

  @Test
  fun provideMockNavigator_success_shouldCreateInstance() {
    assertNotNull(provideMockNavigator())
  }
}
