package com.waffiq.bazz_movies.core.utils.openurl

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.string.no_browser_installed
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
class UriLauncherImplTest {

  private val realContext = ApplicationProvider.getApplicationContext<Application>()

  private val contextSpy = spyk(realContext)
  private val launcher = UriLauncherImpl(contextSpy)

  @Test
  fun launch_withValidUrl_shouldStartActivityWithCorrectIntentAndFlags() {
    val url = "https://www.imdb.com"

    launcher.launch(url)

    verify(exactly = 1) { contextSpy.startActivity(any()) }

    val shadowApp = shadowOf(realContext)
    val startedIntent = shadowApp.nextStartedActivity

    assertNotNull("An intent should have been started", startedIntent)
    assertEquals(Intent.ACTION_VIEW, startedIntent.action)
    assertEquals(url, startedIntent.dataString)

    val hasNewTaskFlag = startedIntent.flags and Intent.FLAG_ACTIVITY_NEW_TASK != 0
    assertEquals(true, hasNewTaskFlag)
  }

  @Test
  fun launch_whenNoActivityCanHandleIntent_shouldShowToastMessage() {
    val url = "https://www.imdb.com"
    val expectedMessage = "No browser installed"

    every { contextSpy.getString(no_browser_installed) } returns expectedMessage
    every { contextSpy.startActivity(any()) } throws ActivityNotFoundException("Activity not found")

    launcher.launch(url)

    val latestToast = ShadowToast.getLatestToast()
    assertNotNull("A warning Toast should have been displayed", latestToast)
    assertEquals(expectedMessage, ShadowToast.getTextOfLatestToast())
  }
}
