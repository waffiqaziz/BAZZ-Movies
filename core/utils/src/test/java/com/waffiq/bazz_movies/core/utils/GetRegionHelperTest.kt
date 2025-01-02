package com.waffiq.bazz_movies.core.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
class GetRegionHelperTest {

  private val context = mockk<Context>(relaxed = true)
  private val configuration = mockk<Configuration>(relaxed = true)
  private val resources = mockk<Resources>(relaxed = true)
  private val telephonyManager = mockk<TelephonyManager>(relaxed = true)

  private fun mockTelephonyManager(simState: Int, networkCountryIso: String?) {
    every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
    every { telephonyManager.simState } returns simState
    every { telephonyManager.networkCountryIso } returns networkCountryIso
  }

  @Before
  fun setup() {
    every { context.resources } returns resources
    every { resources.configuration } returns configuration
  }

  @Test
  fun `test getNetworkLocation with SIM state absent`() {
    val originalTimeZone = TimeZone.getDefault()
    val testTimeZone = TimeZone.getTimeZone("America/New_York")
    TimeZone.setDefault(testTimeZone) // Set the default time zone for the test

    try {
      mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
      val expectedOutput = "america/new_york"
      val actualOutput = GetRegionHelper.getLocation(context)
      assertEquals(expectedOutput, actualOutput)
    } finally {
      TimeZone.setDefault(originalTimeZone) // Restore the original time zone after the test
    }
  }

  @Test
  fun `test getNetworkLocation with SIM state ready`() {
    val countryIso = "us"
    mockTelephonyManager(TelephonyManager.SIM_STATE_READY, countryIso)
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(countryIso, actualOutput)
  }

  @Test
  fun `test getNetworkLocation with SIM state unknown`() {
    mockTelephonyManager(TelephonyManager.SIM_STATE_UNKNOWN, null)
    val expectedOutput = ""
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test getLocation fallback to device locale`() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    val locale = Locale("en", "GB") // Simulate a UK locale
    every { context.resources } returns resources
    every { resources.configuration } returns configuration

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      every { configuration.locales } returns LocaleList(locale)
    } else {
      @Suppress("DEPRECATION")
      every { configuration.locale } returns locale
    }

    val expectedOutput = "gb"
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun `test getLocation edge case with no SIM and no locale`() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      every { configuration.locales } returns LocaleList()
    } else {
      @Suppress("DEPRECATION")
      every { configuration.locale } returns null
    }

    val expectedOutput = ""
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)

    unmockkObject(TimeZoneHelper)
  }


  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun `test getLocation on API 24 and above with locale`() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""

    val locale = Locale("en", "US")
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
    every { configuration.locales } returns LocaleList(locale)

    val expectedOutput = "us"
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.M])
  fun `test getLocation on API 21 with locale`() {
    // TODO: Not yet fixed

//    Locale.setDefault( Locale("es", "ES"))
//    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
//
//    val expectedOutput = "fr" // Expecting "fr" for French locale
//    val actualOutput = GetRegionHelper.getLocation(context)
//    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun `test getLocation on API 24 with empty locale`() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""

    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
    every { configuration.locales } returns LocaleList()

    val expectedOutput = ""
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Suppress("DEPRECATION")
  @Config(sdk = [Build.VERSION_CODES.M])
  fun `test getLocation on API 21 with null locale`() {

    // TODO : not yet fixed

//    mockkObject(TimeZoneHelper)
//    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
//
//    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
//    every { configuration.locale } returns null
//
//    val expectedOutput = ""
//    val actualOutput = GetRegionHelper.getLocation(context)
//    assertEquals(expectedOutput, actualOutput)
//
//    unmockkObject(TimeZoneHelper)
  }
}
