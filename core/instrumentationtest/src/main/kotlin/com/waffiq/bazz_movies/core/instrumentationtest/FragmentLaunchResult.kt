package com.waffiq.bazz_movies.core.instrumentationtest

import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario

data class FragmentLaunchResult<T : Fragment>(
  val scenario: ActivityScenario<HiltTestActivity>,
  val fragment: T,
)
