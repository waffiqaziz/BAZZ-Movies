package com.waffiq.bazz_movies.core.uihelper.utils

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.isButtonNavigationEnabled
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class GestureHelperIsButtonNavigationEnabledTest {
  private lateinit var activity: Activity
  private lateinit var contentResolver: ContentResolver

  @Before
  fun setUp() {
    activity = Robolectric.buildActivity(Activity::class.java).get()
    contentResolver = activity.contentResolver
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q]) // API 29
  fun isButtonNavigationEnabled_whenButtonNavigationEnabled_returnsTrue() {
    // set button navigation (gesture mode = 0)
    Settings.Secure.putInt(contentResolver, "secure_gesture_navigation", 0)
    assertTrue(activity.isButtonNavigationEnabled())
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q]) // API 29
  fun isButtonNavigationEnabled_whenGestureNavigationEnabled_returnsFalse() {
    // set gesture navigation (gesture mode = 1)
    Settings.Secure.putInt(contentResolver, "secure_gesture_navigation", 1)
    assertFalse(activity.isButtonNavigationEnabled())
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.Q]) // API 29
  fun isButtonNavigationEnabled_whenSettingNotFound_returnsTrue() {
    // for SettingNotFoundException case, use a spyk on the activity
    val spyActivity = spyk(activity)
    val spyContentResolver = spyk(contentResolver)

    // make the activity return spy content resolver
    every { spyActivity.contentResolver } returns spyContentResolver

    // make the content resolver throw an exception when getInt is called
    // use a slot to capture the Uri parameter
    val uriSlot = slot<Uri>()
    every {
      spyContentResolver.query(capture(uriSlot), any(), any(), any(), any())
    } answers {
      if (uriSlot.captured.toString().contains("secure_gesture_navigation")) {
        throw Settings.SettingNotFoundException("Setting not found") // throw exception
      } else {
        null
      }
    }

    val result = spyActivity.isButtonNavigationEnabled()

    assertTrue(result) // expect default to true when setting not found
  }
}
