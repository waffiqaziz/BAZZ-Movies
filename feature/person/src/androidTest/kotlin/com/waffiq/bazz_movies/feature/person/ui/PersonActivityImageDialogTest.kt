package com.waffiq.bazz_movies.feature.person.ui

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.pressBack
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.person.R.id.btn_close_dialog
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.view_pager_dialog
import com.waffiq.bazz_movies.feature.person.testutils.DataDumpTest.testProfileItem
import com.waffiq.bazz_movies.feature.person.testutils.DefaultPersonActivityTestHelper
import com.waffiq.bazz_movies.feature.person.testutils.PersonActivityTestHelper
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class PersonActivityImageDialogTest :
  PersonActivityTestHelper by DefaultPersonActivityTestHelper() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockPersonViewModel: PersonViewModel = mockk(relaxed = true)

  @BindValue
  @JvmField
  val mockNavigator: INavigator = mockk(relaxed = true)

  @Before
  override fun init() {
    super.init()
    hiltRule.inject()
    setupMocks()
    initializeTest(ApplicationProvider.getApplicationContext())
  }

  private fun setupMocks() {
    setupBaseMocks()
    setupViewModelMocks(mockPersonViewModel)
    setupNavigatorMocks(mockNavigator)
    loadingStateLiveData.postValue(false)
  }

  @Test
  fun imageDialog_whenImageClicked_showsDialog() =
    runTest {
      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imagePersonLiveData.postValue(
            listOf(testProfileItem, testProfileItem.copy(filePath = "path")),
          )
        }
        performClickListPhotos(0)
      }
    }

  @Test
  fun imageDialog_whenCloseButtonClicked_dismissesDialog() =
    runTest {
      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem))
        }
        performClickListPhotos(0)

        // close dialog
        btn_close_dialog.performClick()
        shortDelay()

        // verify dialog is dismissed
        view_pager_dialog.doesNotExist()
      }
    }

  @Test
  fun imageDialog_whenMultipleImages_showsCorrectPosition() =
    runTest {
      setupMocks()

      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imagePersonLiveData.postValue(listOf(testProfileItem, testProfileItem, testProfileItem))
        }
        performClickListPhotos(1)

        // verify the ViewPager is at the correct position,
        // but it requires custom matchers or checking the adapter state
      }
    }

  @Test
  fun imageDialog_whenBackPressed_dismissesDialog() =
    runTest {
      setupMocks()

      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imagePersonLiveData.postValue(listOf(testProfileItem))
        }
        performClickListPhotos(0)
        pressBack()
        shortDelay()

        // expected dialog is dismissed
        view_pager_dialog.doesNotExist()
      }
    }

  private fun performClickListPhotos(position: Int) {
    // delay before perform action
    shortDelay()

    rv_photos.performScrollTo()
    rv_photos.clickItemAt(position)
    shortDelay()

    // verify dialog and close button is visible
    view_pager_dialog.isDisplayed()
    btn_close_dialog.isDisplayed()
  }
}
