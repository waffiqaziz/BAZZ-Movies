package com.waffiq.bazz_movies.feature.about.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.button.MaterialButton
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.feature.about.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.about.R.id.iv_tmdb_logo
import com.waffiq.bazz_movies.feature.about.R.id.tv_about_text
import com.waffiq.bazz_movies.feature.about.R.id.tv_tmdb_attribute
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class AboutActivityTest {

  private lateinit var activity: AboutActivity
  private lateinit var context: Context
  private lateinit var scenario: ActivityScenario<AboutActivity>

  @Before
  fun setUp() {
    // Initialize the context and set the theme for the activity
    // This is necessary to ensure the activity uses the correct theme during tests
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    val activityInfo = ActivityInfo().apply {
      name = AboutActivity::class.java.name
      packageName = context.packageName
    }
    shadowOf(context.packageManager).addOrUpdateActivity(activityInfo)

    scenario = ActivityScenario.launch(AboutActivity::class.java)
    scenario.onActivity { activity = it }
  }

  @After
  fun tearDown() {
    scenario.close()
  }

  @Test
  fun activity_whenInitialized_showsAllView() {
    assertNotNull(activity)
    assertFalse(activity.isFinishing)

    assertNotNull(activity.findViewById<TextView>(tv_tmdb_attribute))
    assertNotNull(activity.findViewById<TextView>(tv_about_text))
    assertTrue(activity.findViewById<ImageView>(iv_tmdb_logo).isClickable)
    assertTrue(activity.findViewById<MaterialButton>(btn_about_us).isClickable)
  }

  @Test
  fun clickTmdbLogo_whenClicked_shouldLaunchTmdbWebsite() {
    val shadowActivity = shadowOf(activity)
    val tmdbLogo = activity.findViewById<ImageView>(iv_tmdb_logo)

    tmdbLogo.performClick()

    val startedIntent = shadowActivity.nextStartedActivity
    assertNotNull(startedIntent)
    assertEquals(Intent.ACTION_VIEW, startedIntent.action)
    assertEquals(TMDB_LINK_MAIN, startedIntent.data.toString())
  }

  @Test
  fun clickAboutUsButton_whenClicked_shouldLaunchBazzMoviesWebsite() {
    val shadowActivity = shadowOf(activity)
    val aboutUsButton = activity.findViewById<MaterialButton>(btn_about_us)

    aboutUsButton.performClick()

    val startedIntent = shadowActivity.nextStartedActivity
    assertNotNull(startedIntent)
    assertEquals(Intent.ACTION_VIEW, startedIntent.action)
    assertEquals(BAZZ_MOVIES_LINK, startedIntent.data.toString())
  }
}
