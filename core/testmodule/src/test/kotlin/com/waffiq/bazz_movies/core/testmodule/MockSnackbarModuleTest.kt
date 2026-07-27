package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.testmodule.MockSnackbarModule.provideMockSnackbar
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockSnackbarModuleTest {

  @Test
  fun provideMockSnackbar_success_shouldCreateInstance() {
    assertNotNull(provideMockSnackbar())
  }
}
