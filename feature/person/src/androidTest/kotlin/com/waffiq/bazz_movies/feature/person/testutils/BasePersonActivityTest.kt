package com.waffiq.bazz_movies.feature.person.testutils

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.models.MediaCastItem
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
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
  }

  protected fun setupBaseMocks() {
    loadingStateLiveData.postValue(false)
    creditPersonLiveData.postValue(testKnownForList)
    detailPersonLiveData.postValue(testDetailPerson)
    imageListLiveData.postValue(testImagesList)
  }

  protected fun setupViewModelMocks(mockPersonViewModel: PersonViewModel) {
    every { mockPersonViewModel.detailPerson } returns detailPersonLiveData
    every { mockPersonViewModel.castList } returns creditPersonLiveData
    every { mockPersonViewModel.imageList } returns imageListLiveData
    every { mockPersonViewModel.errorState } returns errorStateLiveData
    every { mockPersonViewModel.loadingState } returns loadingStateLiveData

    every { mockPersonViewModel.getDetailPerson(any()) } just Runs
  }

  protected fun setupNavigatorMocks(mockNavigator: INavigator) {
    every { mockNavigator.openDetails(any(), any()) } just Runs
    every { mockNavigator.openPersonDetails(any(), any()) } just Runs
  }

  protected fun initializeTest(context: Context) {
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
}
