package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.designsystem.R.string.no_biography
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesHaveText
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.core.utils.openurl.UriLauncher
import com.waffiq.bazz_movies.feature.person.R.id.btn_link
import com.waffiq.bazz_movies.feature.person.R.id.divider1
import com.waffiq.bazz_movies.feature.person.R.id.rv_known_for
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.tv_biography
import com.waffiq.bazz_movies.feature.person.R.id.tv_dead_header
import com.waffiq.bazz_movies.feature.person.R.id.tv_death
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testDetailPerson
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testImagesList
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testKnownForList
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testMediaCastItem
import com.waffiq.bazz_movies.feature.person.testutils.TestHelper.withCollapsingToolbarTitle
import com.waffiq.bazz_movies.feature.person.ui.PersonActivity
import com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.HiltAndroidRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
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

  protected val detailPersonLiveData = MutableLiveData<DetailPerson>()
  protected val imageListLiveData = MutableLiveData<List<ProfilesItem>>()
  protected val creditPersonLiveData = MutableLiveData<List<CastItem>>()
  protected val errorStateLiveData = MutableLiveData<Event<String>>()
  protected val loadingStateLiveData = MutableLiveData<Boolean>()
  protected lateinit var context: Context

  @Before
  open fun setup() {
    Intents.init()
    hiltRule.inject()
    setupMocks()
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
    setupUriLauncherMocks(mockUriLauncher)
  }

  private fun setupBaseMocks() {
    loadingStateLiveData.postValue(false)
    creditPersonLiveData.postValue(testKnownForList)
    detailPersonLiveData.postValue(testDetailPerson)
    imageListLiveData.postValue(testImagesList)
  }

  private fun setupViewModelMocks(mockPersonViewModel: PersonViewModel) {
    every { mockPersonViewModel.detailPerson } returns detailPersonLiveData
    every { mockPersonViewModel.castList } returns creditPersonLiveData
    every { mockPersonViewModel.imageList } returns imageListLiveData
    every { mockPersonViewModel.errorState } returns errorStateLiveData
    every { mockPersonViewModel.loadingState } returns loadingStateLiveData

    every { mockPersonViewModel.getDetailPerson(any()) } just Runs
  }

  private fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs
  }

  private fun setupUriLauncherMocks(mockUriLauncher: UriLauncher) {
    every { mockUriLauncher.launch(any()) } just Runs
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

  protected fun checkCollapseTitle(title: String?) {
    rv_photos.performScrollTo()
    onView(isAssignableFrom(CollapsingToolbarLayout::class.java))
      .check(matches(withCollapsingToolbarTitle(title)))
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
}
