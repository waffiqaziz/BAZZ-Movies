package com.waffiq.bazz_movies.feature.about.ui

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.feature.about.R.id.btn_about_us
import com.waffiq.bazz_movies.feature.about.R.id.iv_tmdb_logo
import com.waffiq.bazz_movies.feature.about.R.id.toolbar_layout
import com.waffiq.bazz_movies.feature.about.R.id.tv_about_text
import com.waffiq.bazz_movies.feature.about.R.id.tv_tmdb_attribute
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals

@LargeTest
class AboutActivityTest {

  @get:Rule
  val scenarioRule = ActivityScenarioRule(AboutActivity::class.java)

  @get:Rule
  val intentsRule = IntentsRule()

  @Test
  fun aboutActivityScreen_whenOpen_showsAllViews() {
    onView(withId(iv_tmdb_logo)).check(matches(isDisplayed()))
    onView(withId(btn_about_us)).check(matches(isDisplayed()))
    onView(withId(tv_tmdb_attribute)).check(matches(isDisplayed()))
    onView(withId(tv_about_text)).check(matches(isDisplayed()))
  }

  @Test
  fun actionBar_initial_isSetupCorrectly() {
    onView(withId(toolbar_layout)).check(matches(isDisplayed()))
    onView(withContentDescription("Navigate up")).check(matches(isDisplayed()))
  }

  @Test
  fun tmdbLogo_whenClicked_opensCorrectIntent() {
    onView(withId(iv_tmdb_logo)).perform(click())

    Intents.intended(
      allOf(
        hasAction(Intent.ACTION_VIEW),
        hasData(Uri.parse(TMDB_LINK_MAIN))
      )
    )
  }

  @Test
  fun aboutUsButton_whenClicked_opensCorrectIntent() {
    onView(withId(btn_about_us)).perform(click())
    Intents.intended(
      allOf(
        hasAction(Intent.ACTION_VIEW),
        hasData(Uri.parse(BAZZ_MOVIES_LINK))
      )
    )
  }

  @Test
  fun navigationUp_whenClicked_finishesActivity() {
    onView(withContentDescription("Navigate up")).perform(click())
    scenarioRule.scenario.moveToState(Lifecycle.State.DESTROYED)
    assertEquals(Lifecycle.State.DESTROYED, scenarioRule.scenario.state)
  }

  @Test
  fun allClickableElements_whenClicked_shouldNoError() {
    onView(withId(iv_tmdb_logo)).check(matches(allOf(isDisplayed(), isClickable())))
    onView(withId(btn_about_us)).check(matches(allOf(isDisplayed(), isClickable())))
  }

  @Test
  fun activity_whenRecreation_maintainsTheState() {
    scenarioRule.scenario.recreate()
    onView(withId(iv_tmdb_logo)).check(matches(isDisplayed()))
    onView(withId(btn_about_us)).check(matches(isDisplayed()))
    onView(withId(tv_tmdb_attribute)).check(matches(isDisplayed()))
    onView(withId(tv_about_text)).check(matches(isDisplayed()))
  }
}
