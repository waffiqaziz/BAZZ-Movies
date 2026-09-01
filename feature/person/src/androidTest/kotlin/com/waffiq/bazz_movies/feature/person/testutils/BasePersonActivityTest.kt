package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.uiAutomator
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.person.R.id.btn_link
import com.waffiq.bazz_movies.feature.person.R.id.divider1
import com.waffiq.bazz_movies.feature.person.R.id.rv_known_for
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.tv_biography
import com.waffiq.bazz_movies.feature.person.R.id.tv_dead_header
import com.waffiq.bazz_movies.feature.person.R.id.tv_death
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testImagesList
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testKnownForList
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BasePersonActivityTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var mockPersonViewModel: PersonViewModel

  @Inject
  lateinit var mockNavigator: INavigator

  @Inject
  lateinit var mockUriLauncher: UriLauncher

  protected val detailPersonState =
    MutableStateFlow<UIState<DetailPerson>>(UIState.Success(testDetailPerson))
  protected val imageList = MutableStateFlow(testImagesList)
  protected val castList = MutableStateFlow(testKnownForList)
  protected lateinit var context: Context

  @Before
  open fun setup() {
    Intents.init()
    hiltRule.inject()
    setupViewModelMocks()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  private fun setupViewModelMocks() {
    every { mockPersonViewModel.detailPersonState } returns detailPersonState
    every { mockPersonViewModel.castList } returns castList
    every { mockPersonViewModel.imageList } returns imageList

    every { mockPersonViewModel.getDetailPerson(any()) } just Runs
  }

  private fun initializeTest(context: Context) {
    this.context = context
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
      Glide.get(context).clearMemory()
    }
  }

  protected fun Context.launchPersonActivity(
    person: MediaCastItem,
    block: (ActivityScenario<PersonActivity>) -> Unit,
  ) {
    val intent = Intent(this, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, person)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { scenario ->
      scenario.onActivity { /* do nothing */ }
      shortDelay()
      block(scenario)
    }
  }

  protected fun Context.launchPersonActivity(block: (ActivityScenario<PersonActivity>) -> Unit) {
    this.launchPersonActivity(testMediaCastItem) { block(it) }
  }

  protected fun Context.launchNullPersonActivity(
    person: MediaCastItem? = null,
    block: (ActivityScenario<PersonActivity>) -> Unit,
  ) {
    val intent = Intent(this, PersonActivity::class.java).apply {
      putExtra(PersonActivity.EXTRA_PERSON, person)
    }

    ActivityScenario.launch<PersonActivity>(intent).use { scenario ->
      block(scenario)
    }
  }

  // helper action
  protected fun noBiography() {
    rv_photos.performScrollTo()
    tv_biography.doesHaveText(context.getString(no_biography))
  }

  protected fun checkDeathInfo(viewMatcher: Matcher<View>) {
    rv_known_for.performScrollTo()
    onView(withId(tv_death)).check(matches(viewMatcher))
    onView(withId(tv_dead_header)).check(matches(viewMatcher))
  }

  protected fun checkHomePageLink(viewMatcher: Matcher<View>) {
    onView(withId(btn_link)).check(matches(viewMatcher))
    onView(withId(divider1)).check(matches(viewMatcher))
  }

  protected fun performSwipeRefresh() {
    uiAutomator {
      // scroll down so the heigh for scroll up is enough
      device.swipe(
        device.displayWidth / 2,
        device.displayHeight * 3 / 4,
        device.displayWidth / 2,
        device.displayHeight / 4,
        20,
      )
      device.waitForIdle()

      // perform scroll up till max height to trigger swipe refresh
      device.swipe(
        device.displayWidth / 2,
        device.displayHeight / 3,
        device.displayWidth / 2,
        device.displayHeight * 12 / 10,
        100,
      )
      device.waitForIdle()
      shortDelay()
    }
  }
}
