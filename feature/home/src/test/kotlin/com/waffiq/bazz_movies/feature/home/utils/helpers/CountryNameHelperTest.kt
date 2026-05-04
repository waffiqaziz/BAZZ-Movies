package com.waffiq.bazz_movies.feature.home.utils.helpers

import android.os.Build
import com.waffiq.bazz_movies.feature.home.utils.helpers.CountryNameHelper.getCountryDisplayName
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class CountryNameHelperTest {

  @Test
  @Config(sdk = [Build.VERSION_CODES.BAKLAVA])
  fun getCountryDisplayName_onAndroid36_runsWithoutProblem() {
    assertEquals("Indonesia", getCountryDisplayName("id"))
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.VANILLA_ICE_CREAM])
  fun getCountryDisplayName_belowAndroid36_runsWithoutProblem() {
    assertEquals("Japan", getCountryDisplayName("jp"))
  }
}
