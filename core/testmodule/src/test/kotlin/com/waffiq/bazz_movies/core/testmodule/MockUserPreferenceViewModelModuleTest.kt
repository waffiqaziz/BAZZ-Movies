package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.testmodule.MockUserPreferenceViewModelModule.provideMockUserPreferenceViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockUserPreferenceViewModelModuleTest {

  @Test
  fun provideMockUserPreferenceViewModel_success_shouldCreateInstance() {
    assertNotNull(provideMockUserPreferenceViewModel())
  }
}
