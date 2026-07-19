package com.waffiq.bazz_movies.feature.about.ui

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isClickable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.about.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.about.R.id.iv_tmdb_logo
import com.waffiq.bazz_movies.feature.about.R.id.tv_about_text
import com.waffiq.bazz_movies.feature.about.R.id.tv_tmdb_attribute
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AboutActivityTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var mockUriLauncher: UriLauncher

  lateinit var context: Context

  @Before
  fun setup() {
    hiltRule.inject()
    Intents.init()
    every { mockUriLauncher.launch(any()) } just Runs

    ActivityScenario.launch(AboutActivity::class.java).onActivity {
      context = it.applicationContext
    }
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun aboutActivityScreen_whenOpen_showsAllViews() {
    shortDelay(5000)
    iv_tmdb_logo.isDisplayed()
    btn_about_us.isDisplayed()
    tv_tmdb_attribute.isDisplayed()
    tv_about_text.isDisplayed()
  }

  @Test
  fun tmdbLogo_whenClicked_opensCorrectIntent() {
    iv_tmdb_logo.performClick()

    verify { mockUriLauncher.launch(any()) }
  }

  @Test
  fun aboutUsButton_whenClicked_opensCorrectIntent() {
    btn_about_us.performClick()
    verify { mockUriLauncher.launch(any()) }
  }

  @Test
  fun allClickableElements_whenClicked_shouldNoError() {
    iv_tmdb_logo.isClickable()
    btn_about_us.isClickable()
  }

//  @Test
//  fun activity_whenRecreation_maintainsTheState() {
//    scenarioRule.scenario.recreate()
//    iv_tmdb_logo.isDisplayed()
//    btn_about_us.isDisplayed()
//    tv_tmdb_attribute.isDisplayed()
//    tv_about_text.isDisplayed()
//  }
}
