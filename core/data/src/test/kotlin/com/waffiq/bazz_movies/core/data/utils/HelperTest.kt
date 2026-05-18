package com.waffiq.bazz_movies.core.data.utils

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateToday
import com.waffiq.bazz_movies.core.data.utils.Helper.getDateTwoWeeksFromToday
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.util.ReflectionHelpers
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class HelperTest {

  private val sdkVersions = listOf(Build.VERSION_CODES.O, Build.VERSION_CODES.N)

  private fun twoWeeksFromNow(pattern: String = "yyyy-MM-dd") =
    Calendar.getInstance()
      .apply { add(Calendar.WEEK_OF_YEAR, 2) }
      .let { SimpleDateFormat(pattern, Locale.getDefault()).format(it.time) }

  private fun today(pattern: String = "yyyy-MM-dd") =
    SimpleDateFormat(pattern, Locale.getDefault())
      .format(Calendar.getInstance().time)

  @Test
  fun getDateTwoWeeksFromToday_returnsCorrectPattern() {
    sdkVersions.forEach { sdk ->
      ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", sdk)
      assertEquals(twoWeeksFromNow(), getDateTwoWeeksFromToday())
    }
  }

  @Test
  fun getDateTwoWeeksFromToday_customPattern_returnsCorrectPattern() {
    sdkVersions.forEach { sdk ->
      ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", sdk)
      assertEquals(twoWeeksFromNow("dd/MM/yyyy"), getDateTwoWeeksFromToday("dd/MM/yyyy"))
    }
  }

  @Test
  fun getDateToday_returnsToday() {
    sdkVersions.forEach { sdk ->
      ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", sdk)
      assertEquals(today(), getDateToday())
    }
  }

  @Test
  fun getDateToday_customPattern_returnsToday() {
    sdkVersions.forEach { sdk ->
      ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", sdk)
      assertEquals(today("dd/MM/yyyy"), getDateToday("dd/MM/yyyy"))
    }
  }
}
