package com.waffiq.bazz_movies.core.data.utils

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class HelperTest {

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getDateTwoWeeksFromToday_api26OrHigher_returnsCorrectPattern() {
    val result = Helper.getDateTwoWeeksFromToday()

    val expected = Calendar.getInstance().apply {
      add(Calendar.WEEK_OF_YEAR, 2)
    }.let {
      SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.time)
    }

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getDateTwoWeeksFromToday_belowAPI26_returnsCorrectPattern() {
    val result = Helper.getDateTwoWeeksFromToday()

    val expected = Calendar.getInstance().apply {
      add(Calendar.WEEK_OF_YEAR, 2)
    }.let {
      SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.time)
    }

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getDateTwoWeeksFromToday_customPatternAPI26_returnsCorrectPattern() {
    val result = Helper.getDateTwoWeeksFromToday("dd/MM/yyyy")

    val expected = Calendar.getInstance().apply {
      add(Calendar.WEEK_OF_YEAR, 2)
    }.let {
      SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.time)
    }

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getDateTwoWeeksFromToday_customPatternBelowAPI26_returnsCorrectPattern() {
    val result = Helper.getDateTwoWeeksFromToday("dd/MM/yyyy")

    val expected = Calendar.getInstance().apply {
      add(Calendar.WEEK_OF_YEAR, 2)
    }.let {
      SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.time)
    }

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getDateToday_api26_returnsToday() {
    val result = Helper.getDateToday()

    val expected = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      .format(Calendar.getInstance().time)

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getDateToday_belowAPI26_returnsToday() {
    val result = Helper.getDateToday()

    val expected = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      .format(Calendar.getInstance().time)

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.O])
  fun getDateToday_customPatternAPI26_returnsToday() {
    val result = Helper.getDateToday("dd/MM/yyyy")

    val expected = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
      .format(Calendar.getInstance().time)

    assertEquals(expected, result)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getDateToday_customPatternBelowAPI26_returnsToday() {
    val result = Helper.getDateToday("dd/MM/yyyy")

    val expected = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
      .format(Calendar.getInstance().time)

    assertEquals(expected, result)
  }
}
