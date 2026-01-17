package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.waffiq.bazz_movies.core.uihelper.utils.SpannableUtils.buildActionMessage
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SpannableUtilsTest {

  @Test
  fun buildActionMessage_withTitleAndText_returnsBoldTitle() {
    val result = buildActionMessage("Error:", "Something went wrong")

    assertEquals("Error: Something went wrong", result.toString())
    val spans = result.getSpans(0, result.length, StyleSpan::class.java)
    assertEquals(1, spans.size)
    assertEquals(Typeface.BOLD, spans[0].style)
  }

  @Test
  fun buildActionMessage_withTitleAndText_boldSpanCoversOnlyTitle() {
    val title = "Warning:"
    val result = buildActionMessage(title, "Check your input")

    val spans = result.getSpans(0, result.length, StyleSpan::class.java)
    val spanStart = result.getSpanStart(spans[0])
    val spanEnd = result.getSpanEnd(spans[0])

    assertEquals(0, spanStart)
    assertEquals(title.length, spanEnd)
  }

  @Test
  fun buildActionMessage_withEmptyTitle_returnsTextOnly() {
    val result = buildActionMessage("", "Just text")

    assertEquals(" Just text", result.toString())
    val spans = result.getSpans(0, result.length, StyleSpan::class.java)
    assertEquals(1, spans.size)
    assertEquals(0, result.getSpanEnd(spans[0]))
  }

  @Test
  fun buildActionMessage_withEmptyText_returnsTitleOnly() {
    val result = buildActionMessage("Title", "")

    assertEquals("Title ", result.toString())
    val spans = result.getSpans(0, result.length, StyleSpan::class.java)
    assertEquals(1, spans.size)
    assertEquals(5, result.getSpanEnd(spans[0]))
  }
}
