package com.waffiq.bazz_movies.feature.login.utils.openurl

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.net.toUri
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.feature.login.utils.common.Constants.TMDB_LINK_SIGNUP
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class UriLauncherImplTest {

  private lateinit var context: Context
  private lateinit var launcher: UriLauncherImpl

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext()
    launcher = UriLauncherImpl(context)
  }

  @Test
  fun launch_whenBrowserIsAvailable_returnsSuccess() {
    registerBrowserForUrl()

    val result = launcher.launch("https://example.com")

    assertTrue(result.isSuccess)
  }

  @Test
  fun launch_withCorrectIntent_startsActivity() {
    registerBrowserForUrl()

    launcher.launch("https://example.com")

    val started = Shadows.shadowOf(context as Application).nextStartedActivity
    assertNotNull(started)
    assertEquals(Intent.ACTION_VIEW, started.action)
    assertEquals("https://example.com".toUri(), started.data)
  }

  @Test
  fun launch_whenNoBrowserAvailable_returnsFailure() {
    // No browser registered

    val result = launcher.launch("https://example.com")

    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is ActivityNotFoundException)
  }

  @Test
  fun launch_whenStartActivityThrows_returnsFailure() {
    val context = mockk<Context>(relaxed = true)
    val packageManager = mockk<PackageManager>()
    val resolveInfo = mockk<ResolveInfo>()

    every { context.packageManager } returns packageManager
    every { packageManager.resolveActivity(any<Intent>(),any<Int>()) } returns resolveInfo
    every { context.startActivity(any()) } throws RuntimeException("boom")

    val launcher = UriLauncherImpl(context)
    val result = launcher.launch(TMDB_LINK_SIGNUP)

    assertTrue(result.isFailure)
    assertTrue(result.exceptionOrNull() is RuntimeException)
  }

  private fun registerBrowserForUrl() {
    val packageManager = Shadows.shadowOf(context.packageManager)
    val componentName = ComponentName("com.example.browser", "com.example.browser.BrowserActivity")

    // register the activity
    packageManager.addActivityIfNotPresent(componentName)

    // intent filter
    packageManager.addIntentFilterForActivity(
      componentName,
      IntentFilter(Intent.ACTION_VIEW).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        addCategory(Intent.CATEGORY_BROWSABLE)
        addDataScheme("https")
        addDataScheme("http")
      }
    )
  }
}
