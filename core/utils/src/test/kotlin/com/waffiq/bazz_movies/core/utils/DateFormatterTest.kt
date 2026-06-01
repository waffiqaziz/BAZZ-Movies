package com.waffiq.bazz_movies.core.utils

import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterISO8601
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.DateFormatter.formatDate
import com.waffiq.bazz_movies.core.utils.DateFormatter.parseDate
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DateFormatterTest {

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun dateFormatterStandard_withValidInput_returnFormattedDate() {
    val input = "1979-04-04"
    val expectedOutput = "Apr 04, 1979"
    val actualOutput = dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_withNullInput_returnEmptyString() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_withEmptyInput_returnEmptyString() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_withInvalidDateFormat_returnEmptyString() {
    val input = "04-04-1979"
    val expectedOutput = ""
    val actualOutput = dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_withValidInput_returnsFormattedDate() {
    val input = "2024-01-19T00:00:00.000Z"
    val expectedOutput = "Jan 19, 2024"
    val actualOutput = dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_withNullInput_returnEmptyString() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_withEmptyInput_returnEmptyString() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withInvalidDateFormat() {
    val input = "2024-01-19"
    val expectedOutput = ""
    val actualOutput = dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withIncorrectISOFormat() {
    val input = "2024-01-19T00:00:00Z"
    val expectedOutput = ""
    val actualOutput = dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputIsNull() {
    val actual = parseDate(null, "yyyy-MM-dd")
    assertNull(actual)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputIsEmpty() {
    val actual = parseDate("", "yyyy-MM-dd")
    assertNull(actual)
  }

  @Test
  fun formatDate_shouldReturnEmpty_whenParsedDateIsNull() {
    mockkObject(DateFormatter)
    every { parseDate(any(), any()) } returns null

    val result = formatDate("2024-01-19", "yyyy-MM-dd")
    assertEquals("", result)
  }

  @Test
  fun parseDate_shouldReturnNull_whenPatternIsInvalid() {
    val actual = parseDate("2024-01-19", "invalid-pattern")
    assertNull(actual)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputDoesNotMatchPattern() {
    val actual = parseDate("2024/01/19", "yyyy-MM-dd")
    assertNull(actual)
  }
}
