package com.waffiq.bazz_movies.core.uihelper.utils

import android.graphics.Typeface
import android.text.style.StyleSpan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SpannableUtilsTest {

  @Test
  fun buildActionMessage_titleProvided_boldsTitle() {
    val title = "Avatar"
    val actionText = "has been added to favorite"

    val result = SpannableUtils.buildActionMessage(title, actionText)

    assertEquals(
      "Avatar has been added to favorite",
      result.toString()
    )

    val spans = result.getSpans(
      0,
      result.length,
      StyleSpan::class.java
    )

    assertEquals(1, spans.size)
    assertEquals(Typeface.BOLD, spans[0].style)
  }

  @Test
  fun buildActionMessage_emptyTitle_noBoldApplied() {
    val title = ""
    val actionText = "has been added to favorite"

    val result = SpannableUtils.buildActionMessage(title, actionText)

    assertEquals(result.toString(), " has been added to favorite")
  }

  @Test
  fun bold_targetExists_appliesBoldSpan() {
    val text = "Avatar has been added to favorite"
    val target = "Avatar"

    val result = SpannableUtils.run { text.bold(target) }

    val spans = result.getSpans(
      0,
      result.length,
      StyleSpan::class.java
    )

    assertEquals(1, spans.size)
    assertEquals(Typeface.BOLD, spans[0].style)
  }

  @Test
  fun bold_targetNotFound_doesNotApplySpan() {
    val text = "has been added to favorite"

    val result = SpannableUtils.run { text.bold("Avatar") }

    val spans = result.getSpans(
      0,
      result.length,
      StyleSpan::class.java
    )

    assertTrue(spans.isEmpty())
  }
}
