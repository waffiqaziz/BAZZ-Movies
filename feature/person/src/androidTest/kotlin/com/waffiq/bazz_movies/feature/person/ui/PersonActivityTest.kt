package com.waffiq.bazz_movies.feature.person.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.hasContentDescription
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.isGone
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.ViewMatcher.performSwipeDown
import com.waffiq.bazz_movies.feature.person.R.id.background_dim_person
import com.waffiq.bazz_movies.feature.person.R.id.btn_back
import com.waffiq.bazz_movies.feature.person.R.id.btn_facebook
import com.waffiq.bazz_movies.feature.person.R.id.btn_instagram
import com.waffiq.bazz_movies.feature.person.R.id.btn_link
import com.waffiq.bazz_movies.feature.person.R.id.btn_x
import com.waffiq.bazz_movies.feature.person.R.id.collapse
import com.waffiq.bazz_movies.feature.person.R.id.divider1
import com.waffiq.bazz_movies.feature.person.R.id.iv_picture
import com.waffiq.bazz_movies.feature.person.R.id.progress_bar
import com.waffiq.bazz_movies.feature.person.R.id.rv_known_for
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.swipe_refresh
import com.waffiq.bazz_movies.feature.person.R.id.tv_biography
import com.waffiq.bazz_movies.feature.person.R.id.tv_born
import com.waffiq.bazz_movies.feature.person.R.id.tv_dead_header
import com.waffiq.bazz_movies.feature.person.R.id.tv_death
import com.waffiq.bazz_movies.feature.person.R.id.view_group_social_media
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testCastItem
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testExternalIDPerson
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testProfileItem
import com.waffiq.bazz_movies.feature.person.testutils.DefaultPersonActivityTestHelper
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestHelper
import com.waffiq.bazz_movies.feature.person.testutils.TestHelper.isRefreshing
import com.waffiq.bazz_movies.feature.person.testutils.TestHelper.launchPersonActivity
import com.waffiq.bazz_movies.feature.person.testutils.TestHelper.withCollapsingToolbarTitle
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper
import com.waffiq.bazz_movies.feature.person.utils.helper.PersonPageHelper.formatBirthInfo
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
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@HiltAndroidTest
class PersonActivityTest : PersonActivityTestHelper by DefaultPersonActivityTestHelper() {

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

      iv_picture.hasContentDescription("with_profile")
      collapse.isDisplayed()
      tv_biography.performScrollTo()
      tv_biography.doesHaveText(testDetailPerson.biography ?: "")
      rv_known_for.isDisplayed()

      rv_photos.performScrollTo()
      tv_born.isDisplayed()
      tv_death.isNotDisplayed()
      rv_photos.isDisplayed()

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

      btn_back.performClick()
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

      progress_bar.isDisplayed()
      background_dim_person.isDisplayed()

      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        loadingStateLiveData.postValue(false)
      }

      progress_bar.isNotDisplayed()
      background_dim_person.isNotDisplayed()
    }
  }

  @Test
  fun errorState_whenErrorOccurs_displaysSnackbar() = runTest {
    val errorMessage = "Network error occurred"

    context.launchPersonActivity { scenario ->
      scenario.onActivity {
        errorStateLiveData.postValue(Event(errorMessage))
      }

      errorMessage.isDisplayed()
    }
  }

  @Test
  fun buttonBack_whenPressed_closesPersonActivity() = runTest {
    context.launchPersonActivity { scenario ->
      btn_back.performClick()

      scenario.moveToState(Lifecycle.State.DESTROYED)
      assertEquals(Lifecycle.State.DESTROYED, scenario.state)
    }
  }

  @Test
  fun photoProfile_whenNull_showsNoProfile() = runTest {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = null)) {
      iv_picture.hasContentDescription("no_profile")
    }
  }

  @Test
  fun photoProfile_whenEmpty_showsNoProfile() = runTest {
    context.launchPersonActivity(testMediaCastItem.copy(profilePath = "")) {
      iv_picture.hasContentDescription("no_profile")
    }
  }

  @Test
  fun swipeRefresh_whenScroll_runsCorrectly() = runTest {
    context.launchPersonActivity {
      rv_known_for.performScrollTo()
      rv_known_for.performSwipeDown()
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

      rv_photos.performScrollTo()
      checkHomePageLink(isDisplayed())
      btn_link.performClick()

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
      view_group_social_media.isDisplayed()
      btn_instagram.isDisplayed()
      btn_x.isDisplayed()
      btn_facebook.isDisplayed()
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

      view_group_social_media.isGone()
    }
  }

  @Test
  fun birthInfo_whenEmpty_showsNoData() = runTest {
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
  fun deathInfo_withDeathday_shouldVisible() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(deathday = "2023-01-01"))
      }
      tv_death.performScrollTo()
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
      noBiography()
    }
  }

  @Test
  fun biography_whenNull_displaysNoBiography() = runTest {
    context.launchPersonActivity {
      InstrumentationRegistry.getInstrumentation().runOnMainSync {
        detailPersonLiveData.postValue(testDetailPerson.copy(biography = null))
      }
      noBiography()
    }
  }

  @Test
  fun personScreen_whenInitialized_showsAllViews() = runTest {
    val intent = Intent(context, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, testMediaCastItem)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { _ ->
      iv_picture.isDisplayed()
    }
  }

  // helper action
  private fun noBiography(){
    tv_biography.performScrollTo()
    tv_biography.doesHaveText(context.getString(no_biography))
  }
  private fun checkCollapseTitle(title: String?) {
    rv_photos.performScrollTo()
    onView(isAssignableFrom(CollapsingToolbarLayout::class.java))
      .check(matches(withCollapsingToolbarTitle(title)))
  }

  private fun checkDeathInfo(viewMatcher: Matcher<View>) {
    rv_known_for.performScrollTo()
    onView(withId(tv_death)).check(matches(viewMatcher))
    onView(withId(tv_dead_header)).check(matches(viewMatcher))
  }

  private fun checkHomePageLink(viewMatcher: Matcher<View>) {
    onView(withId(btn_link)).check(matches(viewMatcher))
    onView(withId(divider1)).check(matches(viewMatcher))
  }
}
