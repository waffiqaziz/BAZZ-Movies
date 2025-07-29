package com.waffiq.bazz_movies.feature.person.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.designsystem.R.string.no_data
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.waitForActivityToBeDestroyed
import com.waffiq.bazz_movies.feature.person.R.id.background_dim_person
import com.waffiq.bazz_movies.feature.person.R.id.btn_back
import com.waffiq.bazz_movies.feature.person.R.id.collapse
import com.waffiq.bazz_movies.feature.person.R.id.divider1
import com.waffiq.bazz_movies.feature.person.R.id.iv_facebook
import com.waffiq.bazz_movies.feature.person.R.id.iv_instagram
import com.waffiq.bazz_movies.feature.person.R.id.iv_link
import com.waffiq.bazz_movies.feature.person.R.id.iv_picture
import com.waffiq.bazz_movies.feature.person.R.id.iv_x
import com.waffiq.bazz_movies.feature.person.R.id.progress_bar
import com.waffiq.bazz_movies.feature.person.R.id.rv_known_for
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.person.R.id.tv_biography
import com.waffiq.bazz_movies.feature.person.R.id.tv_born
import com.waffiq.bazz_movies.feature.person.R.id.tv_dead_header
import com.waffiq.bazz_movies.feature.person.R.id.tv_death
import com.waffiq.bazz_movies.feature.person.R.id.view_group_social_media
import com.waffiq.bazz_movies.feature.person.R.id.view_group_social_media2
import com.waffiq.bazz_movies.feature.person.testutils.Helper.isRefreshing
import com.waffiq.bazz_movies.feature.person.testutils.Helper.launchPersonActivity
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testCastItem
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testExternalIDPerson
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.testutils.Helper.testProfileItem
import com.waffiq.bazz_movies.feature.person.testutils.Helper.withCollapsingToolbarTitle
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestHelper
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestSetup
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@HiltAndroidTest
class PersonActivityTest : PersonActivityTestSetup by PersonActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockPersonViewModel: PersonViewModel = mockk(relaxed = true)

  @Before
  fun init() {
    hiltRule.inject()
    setupMocks()
    Intents.init()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupMocks() {
    setupBaseMocks()
    setupViewModelMocks(mockPersonViewModel)
    setupNavigatorMocks(mockNavigator)
  }

  @Test
  fun personScreen_whenAllDataProvided_showsAllViews() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(true)
        detailPersonLiveData.postValue(testDetailPerson)
        knownForLiveData.postValue(listOf(testCastItem))
        imagePersonLiveData.postValue(listOf(testProfileItem))
        externalIdPersonLiveData.postValue(testExternalIDPerson)
        loadingStateLiveData.postValue(false)
      }
      shortDelay()

      onView(withId(iv_picture)).check(matches(withContentDescription("with_profile")))
      onView(withId(collapse)).check(matches(isDisplayed()))
      onView(withId(tv_biography)).check(matches(withText(testDetailPerson.biography)))
      onView(withId(rv_known_for)).check(matches(isDisplayed()))

      onView(withId(tv_born)).perform(scrollTo())
      onView(withId(tv_born)).check(matches(isDisplayed()))
      onView(withId(tv_death)).check(matches(not(isDisplayed())))
      onView(withId(rv_photos)).check(matches(isDisplayed()))

      verify { mockPersonViewModel.getDetailPerson(testExternalIDPerson.id!!) }
      verify { mockPersonViewModel.getKnownFor(testExternalIDPerson.id!!) }
      verify { mockPersonViewModel.getImagePerson(testExternalIDPerson.id!!) }
      verify { mockPersonViewModel.getExternalIDPerson(testExternalIDPerson.id!!) }
    }
  }

  @Test
  fun launchPersonActivity_whenPersonIdIsNull_closesTheActivity() = runTest {
    context.launchPersonActivity(null) {
      InstrumentationRegistry.getInstrumentation().waitForIdleSync()

      val resumedActivities = mutableListOf<Activity>()
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        val activities = ActivityLifecycleMonitorRegistry.getInstance()
          .getActivitiesInStage(Stage.RESUMED)
        resumedActivities.addAll(activities)
      }

      // assert that PersonActivity is NOT resumed (means it was finished)
      assertTrue(resumedActivities.none { it is PersonActivity })
    }
  }


  @Test
  fun personScreen_whenNavigateUpPressed_finishesActivity() = runTest {
    val monitor = InstrumentationRegistry.getInstrumentation()
      .addMonitor(PersonActivity::class.java.name, null, false)

    context.launchPersonActivity { scenario ->
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
      }
      shortDelay()

      onView(withId(btn_back)).perform(click())
      scenario.waitForActivityToBeDestroyed()
    }

    InstrumentationRegistry.getInstrumentation().removeMonitor(monitor)
  }

  @Test
  fun collapseTitle_withName_showsCorrectly() = runTest {
    val data = testMediaCastItem.copy(
      name = "Test Name 1",
      originalName = null
    )

    context.launchPersonActivity(data) {
      checkCollapseTitle(data.name)
    }
  }

  @Test
  fun collapseTitle_withOriginalName_showsCorrectly() = runTest {
    val data = testMediaCastItem.copy(
      name = null,
      originalName = "Original Name 1"
    )

    context.launchPersonActivity(data) {
      checkCollapseTitle(data.originalName)
    }
  }

  @Test
  fun collapseTitle_noName_showsNotAvailable() = runTest {
    val data = testMediaCastItem.copy(
      name = null,
      originalName = null
    )

    context.launchPersonActivity(data) {
      checkCollapseTitle(context.getString(not_available))
    }
  }

  @Test
  fun dataPerson_whenNoId_shouldNoProblem() = runTest {
    context.launchPersonActivity(testMediaCastItem.copy(id = null)) {
      verify(exactly = 0) { mockPersonViewModel.getKnownFor(any()) }
      verify(exactly = 0) { mockPersonViewModel.getImagePerson(any()) }
      verify(exactly = 0) { mockPersonViewModel.getDetailPerson(any()) }
      verify(exactly = 0) { mockPersonViewModel.getExternalIDPerson(any()) }
    }
  }

  @Test
  fun personScreen_whenLoading_showsProgressBar() = runTest {
    context.launchPersonActivity {
      loadingStateLiveData.postValue(true)

      onView(withId(progress_bar)).check(matches(isDisplayed()))
      onView(withId(background_dim_person)).check(matches(isDisplayed()))

      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
      }

      onView(withId(progress_bar)).check(matches(not(isDisplayed())))
      onView(withId(background_dim_person)).check(matches(not(isDisplayed())))
    }
  }

  @Test
  fun errorState_whenErrorOccurs_displaysSnackbar() = runTest {
    val errorMessage = "Network error occurred"

    context.launchPersonActivity { scenario ->
      scenario.onActivity {
        errorStateLiveData.postValue(Event(errorMessage))
      }

      onView(withText(errorMessage)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun buttonBack_whenPressed_closesPersonActivity() = runTest {
    context.launchPersonActivity { scenario ->
      onView(withId(btn_back)).perform(click())

      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertEquals(Lifecycle.State.DESTROYED, scenario.state)
    }
  }

  @Test
  fun photoProfile_whenNull_showsNoProfile() = runTest {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = null)) {
      onView(withId(iv_picture)).check(matches(withContentDescription("no_profile")))
    }
  }

  @Test
  fun photoProfile_whenEmpty_showsNoProfile() = runTest {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = "")) {
      onView(withId(iv_picture)).check(matches(withContentDescription("no_profile")))
    }
  }

  @Test
  fun swipeRefresh_whenScroll_runsCorrectly() = runTest {
    context.launchPersonActivity {
      onView(withId(rv_known_for)).perform(scrollTo())
      onView(withId(rv_known_for)).perform(swipeDown())
      onView(withId(swipe_refresh)).check(matches(not(isRefreshing())))
    }
  }

  @Test
  fun homePageLink_withUrlWhenClicked_opensBrowser() = runTest {
    val testDetailPersonWithHomepage = testDetailPerson.copy(
      homepage = "https://example.com",
      imdbId = "nm1234567"
    )

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPersonWithHomepage)
      }
      shortDelay()

      onView(withId(rv_photos)).perform(scrollTo())
      checkHomePageLink(isDisplayed())
      onView(withId(iv_link)).perform(click())

      intended(
        allOf(
          hasAction(Intent.ACTION_VIEW),
          hasData(testDetailPersonWithHomepage.homepage)
        )
      )
    }
  }

  @Test
  fun homePageLink_withNullUrl_hidesLink() = runTest {
    context.launchPersonActivity {
      detailPersonLiveData.postValue(testDetailPerson.copy(homepage = null))
      checkHomePageLink(not(isDisplayed()))
    }
  }

  @Test
  fun homePageLink_withEmptyUrl_hidesLink() = runTest {
    context.launchPersonActivity {
      detailPersonLiveData.postValue(testDetailPerson.copy(homepage = ""))
      checkHomePageLink(not(isDisplayed()))
    }
  }

  @Test
  fun socialMediaLinks_withValidIds_shouldVisible() = runTest {
    val testExternalIds = testExternalIDPerson.copy(
      instagramId = "test_instagram",
      twitterId = "test_twitter",
      facebookId = "test_facebook"
    )

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        externalIdPersonLiveData.postValue(testExternalIds)
      }
      onView(withId(view_group_social_media)).check(matches(isDisplayed()))
      onView(withId(iv_instagram)).check(matches(isDisplayed()))
      onView(withId(iv_x)).check(matches(isDisplayed()))
      onView(withId(iv_facebook)).check(matches(isDisplayed()))
    }
  }

  @Test
  fun socialMediaLinks_withoutIds_shouldHidden() = runTest {
    val testExternalIds = testExternalIDPerson.copy(
      instagramId = null,
      twitterId = null,
      facebookId = null,
      tiktokId = null,
      youtubeId = null,
    )

    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        externalIdPersonLiveData.postValue(testExternalIds)
      }

      onView(withId(view_group_social_media))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
  }

  @Test
  fun birthInfo_whenEmpty_showsNoData() = runTest {
    mockkObject(PersonPageHelper)
    every { PersonPageHelper.formatBirthInfo(any(), any()) } returns ""

    context.launchPersonActivity {
      verify { PersonPageHelper.formatBirthInfo(any(), any()) }
      onView(withId(tv_born)).check(matches(withText(context.getString(no_data))))
    }

    unmockkObject(PersonPageHelper)
  }

  @Test
  fun deathInfo_withDeathday_shouldVisible() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(deathday = "2023-01-01"))
      }
      onView(withId(tv_death)).perform(scrollTo())
      checkDeathInfo(isDisplayed())
    }
  }

  @Test
  fun deathInfo_whenNull_shouldHidden() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(deathday = null))
      }
      checkDeathInfo(not(isDisplayed()))
    }
  }

  @Test
  fun deathInfo_whenEmpty_shouldHidden() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(deathday = ""))
      }
      checkDeathInfo(not(isDisplayed()))
    }
  }

  @Test
  fun biography_whenEmpty_displaysNoBiography() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(biography = ""))
      }
      onView(withId(tv_biography))
        .check(matches(withText(context.getString(no_biography))))
    }
  }

  @Test
  fun biography_whenNull_displaysNoBiography() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(biography = null))
      }
      onView(withId(tv_biography))
        .check(matches(withText(context.getString(no_biography))))
    }
  }

  @Test
  fun personScreen_whenInitialized_showsAllViews() = runTest {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, testMediaCastItem)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { scenario ->
      onView(withId(iv_picture)).check(matches(isDisplayed()))
    }
  }

  // helper action
  private fun checkCollapseTitle(title: String?) {
    onView(withId(rv_photos)).perform(scrollTo())
    onView(isAssignableFrom(CollapsingToolbarLayout::class.java))
      .check(matches(withCollapsingToolbarTitle(title)))
  }

  private fun checkDeathInfo(viewMatcher: Matcher<View>) {
    onView(withId(rv_known_for)).perform(scrollTo())
    onView(withId(tv_death)).check(matches(viewMatcher))
    onView(withId(tv_dead_header)).check(matches(viewMatcher))
  }

  private fun checkHomePageLink(viewMatcher: Matcher<View>) {
    onView(withId(iv_link)).check(matches(viewMatcher))
    onView(withId(divider1)).check(matches(viewMatcher))
  }
}
