package com.waffiq.bazz_movies.feature.more.ui

import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.feature.more.testutils.BaseMoreFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.verify
import org.junit.Test

@HiltAndroidTest
class MoreFragmentLifecycleTest : BaseMoreFragmentTest() {

  @Test
  fun onStop_whenCalled_shouldCleanupSnackbarAndDialog() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onStop()
    }
  }

  @Test
  fun onPause_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onPause()
    }
  }

  @Test
  fun onResume_whenCalled_shouldPassed() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onResume()
    }
  }

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      moreFragment.onDestroyView()
    }
    verify(exactly = 1) { mockUserViewModel.removeState() }
  }
}
