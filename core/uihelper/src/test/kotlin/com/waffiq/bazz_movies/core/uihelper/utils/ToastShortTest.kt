package com.waffiq.bazz_movies.core.uihelper.utils

import android.content.Context
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.toastShort
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
class ToastShortTest {
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @Test
  fun toastShort_displayCorrectToastMessage() {
    val rawText = "Hello, <b>World</b>!"
    val expectedText = HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    context.toastShort(rawText)

    val shownToastText = ShadowToast.getTextOfLatestToast()
    val shownToastDuration = ShadowToast.getLatestToast()?.duration

    assertEquals(expectedText, shownToastText)
    assertEquals(Toast.LENGTH_SHORT, shownToastDuration)
  }
}
