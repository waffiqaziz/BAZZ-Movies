package com.waffiq.bazz_movies.feature.about.ui

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class AboutActivityNullTest {

  private lateinit var context: Context
  private lateinit var scenario: ActivityScenario<*>

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }

    val activityInfo = ActivityInfo().apply {
      name = AboutActivity::class.java.name
      packageName = context.packageName
    }
    shadowOf(context.packageManager).addOrUpdateActivity(activityInfo)
  }

  @After
  fun tearDown() {
    if (::scenario.isInitialized) {
      scenario.close()
    }
  }

  @Test
  fun supportActionBar_whenNull_shouldHandleGracefully() {
    scenario = ActivityScenario.launch(TestableAboutActivity::class.java)

    scenario.onActivity { activity ->
      val testableActivity = activity as TestableAboutActivity
      testableActivity.mockSupportActionBar = null
      testableActivity.setupActionBar()

      // verify that supportActionBar is null
      assertNull(testableActivity.supportActionBar)

      // verify activity doesn't crash and is still functional
      assertFalse(activity.isFinishing)
    }
  }

  @Test
  fun supportActionBar_whenNull_shouldBeHandled() {
    scenario = ActivityScenario.launch(AboutActivityWithNoToolbar::class.java)

    scenario.onActivity { activity ->
      val actionBar = (activity as AppCompatActivity).supportActionBar
      assertNull(actionBar)
    }
  }
}
