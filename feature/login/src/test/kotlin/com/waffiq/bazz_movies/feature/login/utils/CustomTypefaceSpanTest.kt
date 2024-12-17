package com.waffiq.bazz_movies.feature.login.utils

import android.graphics.Typeface
import android.text.TextPaint
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CustomTypefaceSpanTest {

  private lateinit var mockTypeface: Typeface
  private lateinit var mockTextPaint: TextPaint
  private lateinit var customTypefaceSpan: CustomTypefaceSpan

  @Before
  fun setup() {
    // Mock Typeface and TextPaint
    mockTypeface = mock(Typeface::class.java)
    mockTextPaint = mock(TextPaint::class.java)

    // Create instance of CustomTypefaceSpan
    customTypefaceSpan = CustomTypefaceSpan(mockTypeface)
  }

  @Test
  fun `updateDrawState should set the correct typeface`() {
    // Call the method
    customTypefaceSpan.updateDrawState(mockTextPaint)

    // Verify the typeface is set
    verify(mockTextPaint).typeface = mockTypeface
  }

  @Test
  fun `updateMeasureState should set the correct typeface`() {
    customTypefaceSpan.updateMeasureState(mockTextPaint)
    verify(mockTextPaint).typeface = mockTypeface
  }
}
