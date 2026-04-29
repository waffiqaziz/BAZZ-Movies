package com.waffiq.bazz_movies.feature.about.ui

import android.content.Intent
import androidx.core.net.toUri
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isClickable
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.feature.about.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.about.R.id.iv_tmdb_logo
import com.waffiq.bazz_movies.feature.about.R.id.tv_about_text
import com.waffiq.bazz_movies.feature.about.R.id.tv_tmdb_attribute
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test

@LargeTest
class AboutActivityTest {

  @get:Rule
  val scenarioRule = ActivityScenarioRule(AboutActivity::class.java)

  @get:Rule
  val intentsRule = IntentsRule()

  @Test
  fun aboutActivityScreen_whenOpen_showsAllViews() {
    iv_tmdb_logo.isDisplayed()
    btn_about_us.isDisplayed()
    tv_tmdb_attribute.isDisplayed()
    tv_about_text.isDisplayed()
  }

  @Test
  fun tmdbLogo_whenClicked_opensCorrectIntent() {
    iv_tmdb_logo.performClick()

    Intents.intended(
      allOf(
        hasAction(Intent.ACTION_VIEW),
        hasData(TMDB_LINK_MAIN.toUri()),
      ),
    )
  }

  @Test
  fun aboutUsButton_whenClicked_opensCorrectIntent() {
    btn_about_us.performClick()
    Intents.intended(
      allOf(
        hasAction(Intent.ACTION_VIEW),
        hasData(BAZZ_MOVIES_LINK.toUri()),
      ),
    )
  }

  @Test
  fun allClickableElements_whenClicked_shouldNoError() {
    iv_tmdb_logo.isClickable()
    btn_about_us.isClickable()
  }

  @Test
  fun activity_whenRecreation_maintainsTheState() {
    scenarioRule.scenario.recreate()
    iv_tmdb_logo.isDisplayed()
    btn_about_us.isDisplayed()
    tv_tmdb_attribute.isDisplayed()
    tv_about_text.isDisplayed()
  }
}
