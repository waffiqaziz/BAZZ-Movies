package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import android.app.LocaleManager
import android.content.Context
import android.text.SpannableString
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Event
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils.snackBarAlreadyFavorite
import com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers.SnackbarAlreadyUtils.snackBarAlreadyWatchlist
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
  fun snackBarAlreadyWatchlist_returnNullWhenContentIsNotHandled() {
    every { mockEventMessage.getContentIfNotHandled() } returns null

    val result = snackBarAlreadyWatchlist(
      context,
      mockView,
      mockViewGuide,
      mockEventMessage
    )

    assertEquals(null, result)
  }

  @Test
  fun snackBarAlreadyFavorite_returnNullWhenContentIsNotHandled() {
    every { mockEventMessage.getContentIfNotHandled() } returns null

    val result = snackBarAlreadyFavorite(
      context,
      mockView,
      mockViewGuide,
      mockEventMessage
    )

    assertEquals(null, result)
  }

  @Test
  fun snackBarAlreadyFavorite_showWhenContentIsAvailable() {
    val view = mockk<View>(relaxed = true)
    val viewGuide = mockk<View>(relaxed = true)
    val eventMessage = mockk<Event<String>>()
    val mockSnackbar = mockk<Snackbar>(relaxed = true)

    every { eventMessage.getContentIfNotHandled() } returns "Test Item"

    // directly mock the ContextCompat.getString call
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getString(context, any<Int>()) } returns "is already in your favorites"

    // mock HtmlCompat
    mockkStatic(HtmlCompat::class)
    every {
      HtmlCompat.fromHtml(any<String>(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    } returns SpannableString("Test")

    // mock Snackbar
    mockkStatic(Snackbar::class)
    every {
      Snackbar.make(view, any<CharSequence>(), Snackbar.LENGTH_SHORT)
    } returns mockSnackbar
    every { mockSnackbar.setAnchorView(viewGuide) } returns mockSnackbar

    val result = snackBarAlreadyFavorite(
      context,
      view,
      viewGuide,
      eventMessage
    )

    // verify the snackbar was shown
    verify { mockSnackbar.show() }

    // assert that the result is the mockSnackbar
    assertEquals(mockSnackbar, result)
  }

  @Test
  fun snackBarAlreadyWatchlist_showWhenContentIsAvailable() {
    val view = mockk<View>(relaxed = true)
    val viewGuide = mockk<View>(relaxed = true)
    val eventMessage = mockk<Event<String>>()
    val mockSnackbar = mockk<Snackbar>(relaxed = true)

    every { eventMessage.getContentIfNotHandled() } returns "Test Item"

    // directly mock the ContextCompat.getString call
    mockkStatic(ContextCompat::class)
    every { ContextCompat.getString(context, any<Int>()) } returns "is already in your favorites"

    // mock HtmlCompat
    mockkStatic(HtmlCompat::class)
    every {
      HtmlCompat.fromHtml(any<String>(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    } returns SpannableString("Test")

    // mock Snackbar
    mockkStatic(Snackbar::class)
    every {
      Snackbar.make(view, any<CharSequence>(), Snackbar.LENGTH_SHORT)
    } returns mockSnackbar
    every { mockSnackbar.setAnchorView(viewGuide) } returns mockSnackbar

    val result = snackBarAlreadyWatchlist(
      context,
      view,
      viewGuide,
      eventMessage
    )

    // verify the snackbar was shown
    verify { mockSnackbar.show() }

    // assert that the result is the mockSnackbar
    assertEquals(mockSnackbar, result)
  }
}
