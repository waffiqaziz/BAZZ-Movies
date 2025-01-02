package com.waffiq.bazz_movies.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormatterTest {

  @Test
  fun `test dateFormatterStandard with valid date`() {
    val input = "1979-04-04"
    val expectedOutput = "Apr 04, 1979"
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterStandard with null input`() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterStandard with empty input`() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterStandard with invalid date format`() {
    val input = "04-04-1979"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterStandard(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterISO8601 with valid date`() {
    val input = "2024-01-19T00:00:00.000Z"
    val expectedOutput = "Jan 19, 2024"
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterISO8601 with null input`() {
    val input: String? = null
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterISO8601 with empty input`() {
    val input = ""
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterISO8601 with invalid date format`() {
    val input = "2024-01-19"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test dateFormatterISO8601 with incorrect ISO format`() {
    val input = "2024-01-19T00:00:00Z"
    val expectedOutput = ""
    val actualOutput = DateFormatter.dateFormatterISO8601(input)
    assertEquals(expectedOutput, actualOutput)
  }
}
