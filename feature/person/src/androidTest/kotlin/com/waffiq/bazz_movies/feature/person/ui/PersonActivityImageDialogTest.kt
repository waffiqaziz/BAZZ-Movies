package com.waffiq.bazz_movies.feature.person.ui

import androidx.test.espresso.Espresso.pressBack
import androidx.test.platform.app.InstrumentationRegistry
import com.waffiq.bazz_movies.core.instrumentationtest.CustomRecyclerViewActions.clickItemAt
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performClick
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performScrollTo
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeLeft
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewActions.performSwipeRight
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.doesNotExist
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isNotDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.Helper.shortDelay
import com.waffiq.bazz_movies.feature.person.R.id.btn_close_dialog
import com.waffiq.bazz_movies.feature.person.R.id.dots_indicator
import com.waffiq.bazz_movies.feature.person.R.id.rv_photos
import com.waffiq.bazz_movies.feature.person.R.id.text_counter_indicator
import com.waffiq.bazz_movies.feature.person.R.id.view_pager_dialog
import com.waffiq.bazz_movies.feature.person.testutils.BasePersonActivityTest
import com.waffiq.bazz_movies.feature.person.testutils.DummyData.testProfileItem
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PersonActivityImageDialogTest : BasePersonActivityTest() {

  @Before
  override fun setup() {
    super.setup()
    loadingStateLiveData.postValue(false)
  }

  @Test
  fun imageDialog_whenImageClicked_showsDialog() =
    runTest {
      context.launchPersonActivity {
        performClickListPhotos(0)

        // only one images, dot and text indicator is not displayed
        dots_indicator.isNotDisplayed()
        text_counter_indicator.isNotDisplayed()

        // close dialog
        btn_close_dialog.performClick()
        shortDelay()

        // verify dialog is dismissed
        view_pager_dialog.doesNotExist()
      }
    }

  @Test
  fun imageDialog_whenThreeImages_showsCorrectPosition() =
    runTest {
      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imageListLiveData.postValue(List(3) { testProfileItem })
        }
        performClickListPhotos(1) // at least multiple images
        dots_indicator.isDisplayed()
      }
    }

  @Test
  fun imageDialog_whenElevenImages_showsTextIndicator() =
    runTest {
      context.launchPersonActivity {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
          imageListLiveData.postValue(List(11) { testProfileItem })
        }

        // click the second image
        performClickListPhotos(1)
        text_counter_indicator.isDisplayed()

        // perform swipe action
        "2 / 11".isDisplayed()
        view_pager_dialog.performSwipeLeft()
        "3 / 11".isDisplayed()
        view_pager_dialog.performSwipeLeft()
        "4 / 11".isDisplayed()
        view_pager_dialog.performSwipeRight()
        "3 / 11".isDisplayed()
      }
    }

  @Test
  fun imageDialog_whenBackPressed_dismissesDialog() =
    runTest {
      context.launchPersonActivity {
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
