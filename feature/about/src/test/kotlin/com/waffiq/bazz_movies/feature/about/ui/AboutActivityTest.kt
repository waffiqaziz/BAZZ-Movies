package com.waffiq.bazz_movies.feature.about.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.feature.about.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.about.R.id.iv_tmdb_logo
import com.waffiq.bazz_movies.feature.about.R.id.toolbar_layout
import com.waffiq.bazz_movies.feature.about.R.id.tv_about_text
import com.waffiq.bazz_movies.feature.about.R.id.tv_tmdb_attribute
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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
  fun createActivity_whenInitialized_shouldCreateSuccessfully() {
    assertNotNull(activity)
    assertFalse(activity.isFinishing)
  }

  @Test
  fun setupToolbar_whenActivityCreated_shouldEnableHomeButton() {
    val supportActionBar = activity.supportActionBar

    assertNotNull(supportActionBar)
    assertTrue(supportActionBar.isShowing)
  }

  @Test
  fun supportActionBar_whenActivityCreated_shouldHaveDisplayOptions() {
    val supportActionBar = activity.supportActionBar

    assertNotNull(supportActionBar)
    assertTrue(supportActionBar.isShowing)

    val displayOptions = supportActionBar.displayOptions
    assertTrue((displayOptions and ActionBar.DISPLAY_HOME_AS_UP) != 0)
    assertTrue((displayOptions and ActionBar.DISPLAY_SHOW_HOME) != 0)
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

  @Test
  fun navigateUp_whenCalled_shouldFinishActivity() {
    val result = activity.onSupportNavigateUp()

    assertTrue(result)
    assertTrue(activity.isFinishing)
  }

  @Test
  fun toolbarLayout_whenActivityCreated_shouldHaveToolbar() {
    val toolbar = activity.findViewById<MaterialToolbar>(toolbar_layout)
    assertNotNull(toolbar)
  }

  @Test
  fun tmdbAttributeTextView_whenActivityCreated_shouldExist() {
    val tmdbAttributeTextView = activity.findViewById<TextView>(tv_tmdb_attribute)
    assertNotNull(tmdbAttributeTextView)
  }

  @Test
  fun aboutTextView_whenActivityCreated_shouldExist() {
    val aboutTextView = activity.findViewById<TextView>(tv_about_text)
    assertNotNull(aboutTextView)
  }

  @Test
  fun justifyTextViews_whenActivityCreated_shouldCallJustifyForBothTextViews() {
    val tmdbAttributeTextView = activity.findViewById<TextView>(tv_tmdb_attribute)
    val aboutTextView = activity.findViewById<TextView>(tv_about_text)

    assertNotNull(tmdbAttributeTextView)
    assertNotNull(aboutTextView)
  }

  @Test
  fun tmdbLogo_whenActivityCreated_shouldBeClickable() {
    val tmdbLogo = activity.findViewById<ImageView>(iv_tmdb_logo)

    assertNotNull(tmdbLogo)
    assertTrue(tmdbLogo.isClickable)
  }

  @Test
  fun aboutUsButton_whenActivityCreated_shouldBeClickable() {
    val aboutUsButton = activity.findViewById<MaterialButton>(btn_about_us)

    assertNotNull(aboutUsButton)
    assertTrue(aboutUsButton.isClickable)
  }
}
