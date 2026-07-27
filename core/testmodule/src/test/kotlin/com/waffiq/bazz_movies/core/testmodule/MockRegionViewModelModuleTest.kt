package com.waffiq.bazz_movies.core.testmodule

import com.waffiq.bazz_movies.core.testmodule.MockRegionViewModelModule.provideMockRegionViewModel
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockRegionViewModelModuleTest {

  @Test
  fun provideMockRegionViewModel_success_shouldCreateInstance() {
    assertNotNull(provideMockRegionViewModel())
  }
}
