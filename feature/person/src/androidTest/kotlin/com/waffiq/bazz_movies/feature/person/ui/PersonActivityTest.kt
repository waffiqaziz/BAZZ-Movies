package com.waffiq.bazz_movies.feature.person.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.waffiq.bazz_movies.core.common.utils.Constants.INSTAGRAM_LINK
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.hasContentDescription
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isGone
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitForActivityToBeDestroyed
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.feature.person.R.id.background_dim_person
import com.waffiq.bazz_movies.feature.person.R.id.btn_back
import com.waffiq.bazz_movies.feature.person.R.id.btn_facebook
import com.waffiq.bazz_movies.feature.person.R.id.btn_instagram
import com.waffiq.bazz_movies.feature.person.R.id.btn_link
import com.waffiq.bazz_movies.feature.person.R.id.btn_x
import com.waffiq.bazz_movies.feature.person.R.id.collapse
import com.waffiq.bazz_movies.feature.person.R.id.iv_picture
import com.waffiq.bazz_movies.feature.person.R.id.progress_bar
import com.waffiq.bazz_movies.feature.person.R.id.rv_known_for
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.person.R.id.tv_biography
import com.waffiq.bazz_movies.feature.person.R.id.tv_born
import com.waffiq.bazz_movies.feature.person.R.id.tv_death
import com.waffiq.bazz_movies.feature.person.R.id.view_group_social_media
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonActivityTest
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testExternalIDPerson
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.testutils.TestHelper.isRefreshing
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue

@HiltAndroidTest
class PersonActivityTest : BasePersonActivityTest() {

  @Test
  fun personScreen_whenAllDataProvided_showsAllViews() {
    context.launchPersonActivity {
      iv_picture.hasContentDescription("with_profile")
      collapse.isDisplayed()
      rv_photos.performScrollTo()
      tv_biography.doesHaveText(testDetailPerson.biography.orEmpty())
      rv_known_for.isDisplayed()

      rv_photos.performScrollTo()
      tv_born.isDisplayed()
      tv_death.isNotDisplayed()
      rv_photos.isDisplayed()

      verify { mockPersonViewModel.getDetailPerson(any<Int>()) }
    }
  }

  @Test
  fun launchPersonActivity_whenPersonIdIsNull_closesTheActivity() {
    context.launchNullPersonActivity { scenario ->
      val resumedActivities = mutableListOf<Activity>()
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        val activities = ActivityLifecycleMonitorRegistry.getInstance()
          .getActivitiesInStage(Stage.RESUMED)
        resumedActivities.addAll(activities)
      }

      // assert that PersonActivity is NOT resumed (means it was finished)
      assertTrue(resumedActivities.none { it is PersonActivity })
      assertEquals(scenario.state, Lifecycle.State.DESTROYED)
    }
  }

  @Test
  fun personScreen_whenNavigateUpPressed_finishesActivity() {
    val monitor = InstrumentationRegistry.getInstrumentation()
      .addMonitor(PersonActivity::class.java.name, null, false)

    context.launchPersonActivity { scenario ->
      shortDelay()

      btn_back.performClick()
      scenario.waitForActivityToBeDestroyed()
    }

    InstrumentationRegistry.getInstrumentation().removeMonitor(monitor)
  }

  @Test
  fun dataPerson_whenNoId_shouldNoProblem() {
    context.launchPersonActivity(testMediaCastItem.copy(id = null)) {
      verify(exactly = 0) { mockPersonViewModel.getDetailPerson(any()) }
    }
  }

  @Test
  fun personScreen_whenLoading_showsProgressBar() {
    context.launchPersonActivity {
      // loading
      detailPersonState.value = UIState.Loading
      progress_bar.isDisplayed()
      background_dim_person.isDisplayed()

      // content is ready
      detailPersonState.value = UIState.Success(testDetailPerson)
      progress_bar.isNotDisplayed()
      background_dim_person.isNotDisplayed()
    }
  }

  @Test
  fun errorState_whenErrorOccurs_displaysSnackbar() {
    val errorMessage = "Network error occurred"

    context.launchPersonActivity {
      detailPersonState.value = UIState.Error(errorMessage)
      errorMessage.isDisplayed()
    }
  }

  @Test
  fun idleState_whenIdleOccurs_doNothing() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Idle
    }
  }

  @Test
  fun buttonBack_whenPressed_closesPersonActivity() {
    context.launchPersonActivity { scenario ->
      btn_back.performClick()

      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertEquals(Lifecycle.State.DESTROYED, scenario.state)
    }
  }

  @Test
  fun photoProfile_whenNull_showsNoProfile() {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = null)) {
      iv_picture.hasContentDescription("no_profile")
    }
  }

  @Test
  fun photoProfile_whenEmpty_showsNoProfile() {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = "")) {
      iv_picture.hasContentDescription("no_profile")
    }
  }

  @Test
  fun swipeRefresh_whenScroll_runsCorrectly() {
    context.launchPersonActivity { _ ->
      performSwipeRefresh()

      onView(withId(swipe_refresh)).check(matches(not(isRefreshing())))
      verify { mockPersonViewModel.getDetailPerson(any()) }
    }
  }

  @Test
  fun swipeRefresh_noId_doesNotTriggerFetchDetailPerson() {
    context.launchPersonActivity(testMediaCastItem.copy(id = null)) { _ ->
      performSwipeRefresh()

      onView(withId(swipe_refresh)).check(matches(not(isRefreshing())))
      verify(exactly = 0) { mockPersonViewModel.getDetailPerson(any()) }
    }
  }

  @Test
  fun homePageLink_withUrlWhenClicked_opensBrowser() {
    val testDetailPersonWithHomepage = testDetailPerson.copy(
      homepage = "https://example.com",
      imdbId = "nm1234567",
    )

    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPersonWithHomepage)

      rv_photos.performScrollTo()
      checkHomePageLink(isDisplayed())
      btn_link.performClick()

      every { mockUriLauncher.launch(testDetailPersonWithHomepage.homepage.orEmpty()) }
    }
  }

  @Test
  fun homePageLink_withNullUrl_hidesLink() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(homepage = null))
      checkHomePageLink(not(isDisplayed()))
    }
  }

  @Test
  fun homePageLink_withEmptyUrl_hidesLink() {
    detailPersonState.value = UIState.Success(testDetailPerson.copy(homepage = ""))
    context.launchPersonActivity {
      checkHomePageLink(not(isDisplayed()))
    }
  }

  @Test
  fun socialMediaLinks_withValidIds_shouldVisible() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson)
      view_group_social_media.isDisplayed()
      btn_instagram.isDisplayed()
      btn_x.isDisplayed()
      btn_facebook.isDisplayed()
    }
  }

  @Test
  fun socialMediaLinks_withoutIds_shouldHidden() {
    val testExternalIds = testExternalIDPerson.copy(
      instagramId = null,
      twitterId = null,
      facebookId = null,
      tiktokId = null,
      youtubeId = null,
    )

    context.launchPersonActivity {
      detailPersonState.value =
        UIState.Success(testDetailPerson.copy(externalIds = testExternalIds))
      view_group_social_media.isGone()
    }
  }

  @Test
  fun socialMediaLinks_whenExternalIdIsNull_shouldHidden() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(externalIds = null))
      view_group_social_media.isGone()
    }
  }

  @Test
  fun socialMediaLinks_withNullId_shouldHidden() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(externalIds = null))
      view_group_social_media.isGone()
    }
  }

  @Test
  fun birthInfo_whenEmpty_showsNoData() {
    mockkObject(PersonPageHelper)
    every { any<Context>().formatBirthInfo(any(), any(), any()) } returns ""

    context.launchPersonActivity {
      verify { any<Context>().formatBirthInfo(any(), any(), any()) }
      tv_born.performScrollTo()
      tv_born.doesHaveText(context.getString(no_data))
    }

    unmockkObject(PersonPageHelper)
  }

  @Test
  fun deathInfo_withDeathday_shouldVisible() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(deathday = "2023-01-01"))
      tv_death.performScrollTo()
      checkDeathInfo(isDisplayed())
    }
  }

  @Test
  fun deathInfo_whenNull_shouldHidden() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(deathday = null))
      checkDeathInfo(not(isDisplayed()))
    }
  }

  @Test
  fun deathInfo_whenEmpty_shouldHidden() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(deathday = ""))
      checkDeathInfo(not(isDisplayed()))
    }
  }

  @Test
  fun biography_whenEmpty_displaysNoBiography() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(biography = ""))
      noBiography()
    }
  }

  @Test
  fun biography_whenNull_displaysNoBiography() {
    context.launchPersonActivity {
      detailPersonState.value = UIState.Success(testDetailPerson.copy(biography = null))
      noBiography()
    }
  }

  @Test
  fun personScreen_whenInitialized_showsAllViews() {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, testMediaCastItem)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { _ ->
      iv_picture.isDisplayed()
    }
  }

  @Test
  fun instagramSocialMedia_performClick_shouldTriggerUriLauncher() {
    context.launchPersonActivity {
      btn_instagram.performClick()
      verify { mockUriLauncher.launch(INSTAGRAM_LINK + testExternalIDPerson.instagramId) }
    }
  }
}
