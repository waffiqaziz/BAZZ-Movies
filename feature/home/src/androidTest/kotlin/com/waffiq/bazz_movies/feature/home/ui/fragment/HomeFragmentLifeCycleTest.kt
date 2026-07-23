package com.waffiq.bazz_movies.feature.home.ui.fragment

import androidx.lifecycle.Lifecycle
import com.waffiq.bazz_movies.feature.home.testutils.BaseHomeFragmentTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class HomeFragmentLifeCycleTest : BaseHomeFragmentTest() {

  @Test
  fun onDestroyView_whenCalled_resetsState() {
    launchFragment()
    scenario.moveToState(Lifecycle.State.DESTROYED)
  }
}
