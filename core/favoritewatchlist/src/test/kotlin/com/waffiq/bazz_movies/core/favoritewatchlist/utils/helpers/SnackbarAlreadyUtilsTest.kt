package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.app.LocaleManager
import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils.snackBarAlready
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SnackbarAlreadyUtilsTest {

  @MockK
  private lateinit var context: Context

  @MockK
  private lateinit var mockView: View

  @MockK
  private lateinit var mockViewGuide: View

  @MockK
  private lateinit var mockEventMessage: Event<String>

  @MockK
  private lateinit var mockSnackBar: Snackbar

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    MockKAnnotations.init(this, relaxed = true)

    // mock system service for Locale
    every { context.getSystemService(Context.LOCALE_SERVICE) } returns mockk<LocaleManager>()
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun snackBarAlreadyFavorite_whenContentIsNotHandled_returnsNull() {
    every { mockEventMessage.getContentIfNotHandled() } returns null

    val result = snackBarAlready(
      context,
      mockView,
      mockViewGuide,
      mockEventMessage,
      true
    )

    assertEquals(null, result)
  }

  @Test
  fun snackBarAlreadyWatchlist_whenContentIsNotHandled_returnsNull() {
    every { mockEventMessage.getContentIfNotHandled() } returns null

    val result = snackBarAlready(
      context,
      mockView,
      mockViewGuide,
      mockEventMessage,
      false
    )

    assertEquals(null, result)
  }

  @Test
  fun snackBarAlreadyFavorite_whenContentIsAvailable_showsSnackbar() {
    every { mockEventMessage.getContentIfNotHandled() } returns "Test Item"

    // directly mock the ContextCompat.getString call
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getString(context, any<Int>()) } returns "is already in your favorites"

    // mock Snackbar
    mockkStatic(Snackbar::class)
    every {
      Snackbar.make(mockView, any<CharSequence>(), Snackbar.LENGTH_SHORT)
    } returns mockSnackBar
    every { mockSnackBar.setAnchorView(mockViewGuide) } returns mockSnackBar

    val result = snackBarAlready(
      context,
      mockView,
      mockViewGuide,
      mockEventMessage,
      true
    )

    // verify the snackbar was shown
    verify { mockSnackBar.show() }

    // assert that the result is the mockSnackbar
    assertEquals(mockSnackBar, result)
  }

  @Test
  fun snackBarAlreadyWatchlist_whenContentIsAvailable_showsSnackbar() {
    val view = mockk<View>(relaxed = true)
    val viewGuide = mockk<View>(relaxed = true)
    val eventMessage = mockk<Event<String>>()
    val mockSnackbar = mockk<Snackbar>(relaxed = true)

    every { eventMessage.getContentIfNotHandled() } returns "Test Item"

    // directly mock the ContextCompat.getString call
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getString(context, any<Int>()) } returns "is already in your watchlist"

    // mock Snackbar
    mockkStatic(Snackbar::class)
    every {
      Snackbar.make(view, any<CharSequence>(), Snackbar.LENGTH_SHORT)
    } returns mockSnackbar
    every { mockSnackbar.setAnchorView(viewGuide) } returns mockSnackbar

    val result = snackBarAlready(
      context,
      view,
      viewGuide,
      eventMessage,
      false
    )

    // verify the snackbar was shown
    verify { mockSnackbar.show() }

    // assert that the result is the mockSnackbar
    assertEquals(mockSnackbar, result)
  }
}
