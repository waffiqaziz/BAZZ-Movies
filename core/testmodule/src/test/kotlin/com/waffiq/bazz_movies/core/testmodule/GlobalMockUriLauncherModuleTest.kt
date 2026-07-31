package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.testmodule.DefaultMockUriLauncherModule.provideMockUriLauncher
import org.junit.Assert.assertNotNull
import org.junit.Test

class GlobalMockUriLauncherModuleTest {

  @Test
  fun provideMockUriLauncher_success_shouldCreateInstance() {
    assertNotNull(provideMockUriLauncher())
  }
}
