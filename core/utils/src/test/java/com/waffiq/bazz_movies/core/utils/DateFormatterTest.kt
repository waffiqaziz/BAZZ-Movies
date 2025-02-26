package com.waffiq.bazz_movies.core.utils

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
  fun dateFormatterStandard_shouldReturnFormattedDate_withValidInput() {
    val input = "1979-04-04"
    val expectedOutput = "Apr 04, 1979"
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_shouldReturnEmpty_withNullInput() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_shouldReturnEmpty_withEmptyInput() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterStandard_shouldReturnEmpty_withInvalidDateFormat() {
    val input = "04-04-1979"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnFormattedDate_withValidInput() {
    val input = "2024-01-19T00:00:00.000Z"
    val expectedOutput = "Jan 19, 2024"
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withNullInput() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withEmptyInput() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withInvalidDateFormat() {
    val input = "2024-01-19"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun dateFormatterISO8601_shouldReturnEmpty_withIncorrectISOFormat() {
    val input = "2024-01-19T00:00:00Z"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputIsNull() {
    val actual = DateFormatter.parseDate(null, "yyyy-MM-dd")
    assertNull(actual)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputIsEmpty() {
    val actual = DateFormatter.parseDate("", "yyyy-MM-dd")
    assertNull(actual)
  }

  @Test
  fun formatDate_shouldReturnEmpty_whenParsedDateIsNull() {
    mockkObject(DateFormatter)
    every { DateFormatter.parseDate(any(), any()) } returns null

    val result = DateFormatter.formatDate("2024-01-19", "yyyy-MM-dd")
    assertEquals("", result)
  }

  @Test
  fun parseDate_shouldReturnNull_whenPatternIsInvalid() {
    val actual = DateFormatter.parseDate("2024-01-19", "invalid-pattern")
    assertNull(actual)
  }

  @Test
  fun parseDate_shouldReturnNull_whenInputDoesNotMatchPattern() {
    val actual = DateFormatter.parseDate("2024/01/19", "yyyy-MM-dd")
    assertNull(actual)
  }
}
