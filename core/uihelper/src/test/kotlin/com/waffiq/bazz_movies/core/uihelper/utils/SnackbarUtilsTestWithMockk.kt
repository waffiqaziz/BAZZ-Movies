package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SnackbarUtilsTestWithMockk {
  private lateinit var eventMessage: Event<String>
  private lateinit var parentView: FrameLayout
  private lateinit var anchorView: FrameLayout

  @Before
  fun setup() {
    parentView = mockk(relaxed = true)
    anchorView =  mockk(relaxed = true)
    eventMessage = mockk()
  }

  @Test
  fun snackBarWarning_messageHandled_returnSnackbar() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNotNull("Snackbar should not be null", snackbar)
    assertTrue(snackbar is Snackbar)

    val snackbar2 = snackBarWarning(parentView, anchorView, "Message")
    assertNotNull("Snackbar should not be null", snackbar2)
    assertTrue(snackbar2 is Snackbar)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_parentNotAttached_returnNull() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns false

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNull("Snackbar should be null", snackbar)

    val snackbar2 = snackBarWarning(parentView, null, "Message")
    assertNull("Snackbar should be null", snackbar2)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_noMessage_returnNull() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED

    every { eventMessage.getContentIfNotHandled() } returns ""
    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNull("Snackbar should be null", snackbar)

    every { eventMessage.getContentIfNotHandled() } returns null
    val snackbar2 = snackBarWarning(parentView, null, eventMessage)
    assertNull("Snackbar should be null", snackbar2)

    val snackbar3 = snackBarWarning(parentView, null, " ")
    assertNull("Snackbar should be null", snackbar3)

    val snackbar4 = snackBarWarning(parentView, null, "")
    assertNull("Snackbar should be null", snackbar4)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_anchorNull_returnSnackbar() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED

    every { eventMessage.getContentIfNotHandled() } returns "Message"
    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNotNull("Snackbar should not be null", snackbar)

    val snackbar2 = snackBarWarning(parentView, null, "Message")
    assertNotNull("Snackbar should not be null", snackbar2)

    unmockkAll()
  }
}