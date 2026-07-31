package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SnackbarUtilsTestWithMockk {

  private lateinit var parentView: FrameLayout
  private lateinit var anchorView: FrameLayout
  private lateinit var mockSnackbar: Snackbar

  private val snackbarShouldNull = "Snackbar should be null"
  private val snackbarShouldNotNull = "Snackbar should not be null"

  @Before
  fun setup() {
    parentView = mockk(relaxed = true)
    anchorView = mockk(relaxed = true)
    mockSnackbar = mockk(relaxed = true)

    mockkStatic(Snackbar::class)
    mockkStatic(ContextCompat::class)
    every { Snackbar.make(any<View>(), any<String>(), any<Int>()) } returns mockSnackbar
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun snackBarWarning_whenMessageIsHandled_returnSnackbar() {
    stubParentAttached(true)
    stubColorResolved()

    val snackbar = snackBarWarning(parentView, anchorView, "Message")
    assertNotNull(snackbarShouldNotNull, snackbar)
    assertTrue(snackbar is Snackbar)
  }

  @Test
  fun snackBarWarning_withoutMessage_returnNull() {
    stubParentAttached(true)
    stubColorResolved()

    assertNull(snackbarShouldNull, snackBarWarning(parentView, null, " "))
    assertNull(snackbarShouldNull, snackBarWarning(parentView, null, ""))
  }

  @Test
  fun snackBarWarning_whenAnchorViewNull_returnSnackbar() {
    stubParentAttached(true)
    stubColorResolved()

    assertNotNull(snackbarShouldNotNull, snackBarWarning(parentView, null, "Message"))
  }

  @Test
  fun snackBarWarning_whenAnchorViewProvided_returnSnackbar() {
    stubParentAttached(true)
    stubColorResolved()
    every { parentView.context } returns mockk(relaxed = true)

    snackBarWarning(parentView, anchorView, "Message")
    verify { mockSnackbar.anchorView = anchorView }

    clearMocks(mockSnackbar)

    snackBarWarning(parentView, null, "Message")
    verify(exactly = 0) { mockSnackbar.anchorView = any() }
  }

  @Test
  fun snackBarWarning_whenAnchorViewVaries_correctlyHandlesTheAnchor() {
    stubParentAttached(true)
    stubColorResolved()
    every { parentView.context } returns mockk(relaxed = true)

    val result1 = snackBarWarning(parentView, anchorView, "Message")
    assertNotNull(result1)
    verify { mockSnackbar.anchorView = anchorView }

    clearMocks(mockSnackbar)

    val result2 = snackBarWarning(parentView, null, "Message")
    assertNotNull(result2)
    verify(exactly = 0) { mockSnackbar.anchorView = any() }
  }

  private fun stubParentAttached(attached: Boolean) {
    every { parentView.isAttachedToWindow } returns attached
  }

  private fun stubColorResolved() {
    every { ContextCompat.getColor(any(), any()) } returns Color.RED
  }
}
