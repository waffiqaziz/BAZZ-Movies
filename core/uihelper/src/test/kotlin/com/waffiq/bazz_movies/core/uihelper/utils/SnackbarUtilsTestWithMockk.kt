package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SnackbarUtilsTestWithMockk {
  private lateinit var eventMessage: Event<String>
  private lateinit var parentView: FrameLayout
  private lateinit var anchorView: FrameLayout

  // error message for testing
  private val snackbarShouldNull = "Snackbar should be null"
  private val snackbarShouldNotNull = "Snackbar should not be null"

  @Before
  fun setup() {
    parentView = mockk(relaxed = true)
    anchorView =  mockk(relaxed = true)
    eventMessage = mockk()
  }

  @Test
  fun snackBarWarning_whenMessageIsHandled_returnSnackbar() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNotNull(snackbarShouldNotNull, snackbar)
    assertTrue(snackbar is Snackbar)

    val snackbar2 = snackBarWarning(parentView, anchorView, "Message")
    assertNotNull(snackbarShouldNotNull, snackbar2)
    assertTrue(snackbar2 is Snackbar)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenParentNotAttached_returnNull() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns false

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNull(snackbarShouldNull, snackbar)

    val snackbar2 = snackBarWarning(parentView, null, "Message")
    assertNull(snackbarShouldNull, snackbar2)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_withoutMessage_returnNull() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED

    every { eventMessage.getContentIfNotHandled() } returns ""
    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNull(snackbarShouldNull, snackbar)

    every { eventMessage.getContentIfNotHandled() } returns null
    val snackbar2 = snackBarWarning(parentView, null, eventMessage)
    assertNull(snackbarShouldNull, snackbar2)

    val snackbar3 = snackBarWarning(parentView, null, " ")
    assertNull(snackbarShouldNull, snackbar3)

    val snackbar4 = snackBarWarning(parentView, null, "")
    assertNull(snackbarShouldNull, snackbar4)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenAnchorViewNull_returnSnackbar() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED

    every { eventMessage.getContentIfNotHandled() } returns "Message"
    val snackbar = snackBarWarning(parentView, null, eventMessage)
    assertNotNull(snackbarShouldNotNull, snackbar)

    val snackbar2 = snackBarWarning(parentView, null, "Message")
    assertNotNull(snackbarShouldNotNull, snackbar2)

    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenAnchorViewProvided_returnSnackbar() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true
    every { parentView.context } returns mockk(relaxed = true)

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    // Test with anchorView provided
    val snackbar = snackBarWarning(parentView, anchorView, eventMessage)
    assertNotNull(snackbar)

    // Verify anchorView was set
    verify { mockSnackbar.anchorView = anchorView }

    clearMocks(mockSnackbar)

    // Test without anchorView (should not set anchorView)
    val snackbar2 = snackBarWarning(parentView, null, eventMessage)
    assertNotNull(snackbar2)

    // Verify anchorView was not set when null
    verify(exactly = 0) { mockSnackbar.anchorView = any() }

    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenCalledWithAllParameterVariations_createsSnackbarSuccessfully() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true
    every { parentView.context } returns mockk(relaxed = true)

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
    every { eventMessage.getContentIfNotHandled() } returns "Test Message"

    // Test all parameter combinations for Event<String> version
    snackBarWarning(parentView, anchorView, eventMessage) // all params
    snackBarWarning(parentView, null, eventMessage) // anchorView null
    snackBarWarning(parentView, eventMessage = eventMessage) // default anchorView
    snackBarWarning(view = parentView, eventMessage = eventMessage) // named params

    // Test all parameter combinations for String version
    snackBarWarning(parentView, anchorView, "Message") // all params
    snackBarWarning(parentView, null, "Message") // anchorView null
    snackBarWarning(parentView, message = "Message") // default anchorView
    snackBarWarning(view = parentView, message = "Message") // named params

    // All calls should succeed
    verify(exactly = 8) { Snackbar.make(any<View>(), any<String>(), any<Int>()) }

    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenAnchorViewVaries_correctlyHandlesTheAnchor() {
    val mockSnackbar = mockk<Snackbar>(relaxed = true)
    mockkStatic(Snackbar::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
    every { parentView.isAttachedToWindow } returns true
    every { parentView.context } returns mockk(relaxed = true)

    mockkStatic(ContextCompat::class)
    every { ContextCompat.getColor(any(), any()) } returns Color.RED

    // Test with non-null anchorView to ensure let block executes
    val result1 = snackBarWarning(parentView, anchorView, "Message")
    assertNotNull(result1)
    verify { mockSnackbar.anchorView = anchorView }

    clearMocks(mockSnackbar)

    // Test with null anchorView to ensure let block doesn't execute
    val result2 = snackBarWarning(parentView, null, "Message")
    assertNotNull(result2)
    verify(exactly = 0) { mockSnackbar.anchorView = any() }

    unmockkAll()
  }
}
