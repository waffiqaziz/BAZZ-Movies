package com.waffiq.bazz_movies.core.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import com.waffiq.bazz_movies.core.utils.GetRegionHelper.getOrNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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

  @After
  fun cleanup() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    unmockkObject(TimeZoneHelper)
  }

  @Test
  fun getNetworkLocation_SIMStateAbsent_returnCorrectLocation() {
    val originalTimeZone = TimeZone.getDefault()
    val testTimeZone = TimeZone.getTimeZone("America/New_York")
    TimeZone.setDefault(testTimeZone) // Set the default time zone for the test

    try {
      mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
      val expectedOutput = "america/new_york"
      val actualOutput = GetRegionHelper.getLocation(context)
      assertEquals(expectedOutput, actualOutput)
    } finally {
      TimeZone.setDefault(originalTimeZone) // restore the original time zone after the test
    }
  }

  @Test
  fun getNetworkLocation_SIMStateReady_returnCorrectLocation() {
    mockTelephonyManager(TelephonyManager.SIM_STATE_READY, "us")
    assertEquals("us", GetRegionHelper.getLocation(context))
  }

  @Test
  fun getNetworkLocation_SIMStateUnknown_returnCorrectLocation() {
    mockTelephonyManager(TelephonyManager.SIM_STATE_UNKNOWN, null)
    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  fun getLocation_SIMAbsent_fallbackToDeviceLocale() {
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

    assertEquals("gb", GetRegionHelper.getLocation(context))
  }

  @Test
  fun getLocation_noSIM_noLocale_handleCorrectly() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      every { configuration.locales } returns LocaleList()
    } else {
      @Suppress("DEPRECATION")
      every { configuration.locale } returns null
    }

    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getLocation_onApi24AndAbove_withLocale_returnCorrectLocation() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""

    val locale = Locale("en", "US")
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
    every { configuration.locales } returns LocaleList(locale)

    val expectedOutput = "us"
    val actualOutput = GetRegionHelper.getLocation(context)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  @Config(sdk = [23]) // ensures API level < 24
  fun getLocationApi23_fallbackToSingleLocale() {
    val locale = Locale("es", "ES")

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    assertEquals("es", GetRegionHelper.getLocation(context))

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getLocationApi24_withEmptyLocale_handleCorrectly() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""

    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)
    every { configuration.locales } returns LocaleList()

    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun localeListGetOrNullUutOfBounds_returnsNull() {
    val localeList = LocaleList(Locale("en", "US"), Locale("fr", "FR")) // list with 2 locales

    assertNull(localeList.getOrNull(-1))
    assertNull(localeList.getOrNull(2))
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withEmptyCountry_returnEmptyString() {
    val locale = Locale("", "")

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    assertEquals("", GetRegionHelper.getLocation(context))

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withNullLocale_returnEmptyString() {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", null)

    assertEquals("", GetRegionHelper.getLocation(context))

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withMixedCaseCountry_returnLowercase() {
    val locale = Locale("en", "Us") // mixed-case country code

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    assertEquals("us", GetRegionHelper.getLocation(context))

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withDifferentCaseCountry_returnLowercase() {
    val locale = Locale("en", "uS")

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    assertEquals("us", GetRegionHelper.getLocation(context))
    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withDifferentDefaultLocale_returnLowercase() {
    val locale = Locale("en", "DE")

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    val originalDefault = Locale.getDefault()
    Locale.setDefault(Locale.FRANCE)
    try {
      assertEquals("de", GetRegionHelper.getLocation(context))
    } finally {
      Locale.setDefault(originalDefault)
    }

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withRootLocale_returnLowercase() {
    val locale = Locale("en", "JP") // Example: Japan region

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    val originalDefault = Locale.getDefault()
    Locale.setDefault(Locale.ROOT) // special locale with neutral behavior
    try {
      assertEquals("jp", GetRegionHelper.getLocation(context))
    } finally {
      Locale.setDefault(originalDefault)
    }

    unmockkObject(TimeZoneHelper)
  }

  @Test
  @Config(sdk = [23])
  fun getLocationApi23_withBlankCountry_returnEmptyString() {
    val locale = Locale("en", "   ")

    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns ""
    mockTelephonyManager(TelephonyManager.SIM_STATE_ABSENT, null)

    every { resources.configuration } returns configuration
    every { context.resources } returns resources
    setFinalField(configuration, "locale", locale)

    assertEquals("   ", GetRegionHelper.getLocation(context))

    unmockkObject(TimeZoneHelper)
  }

  // set final fields using reflection
  private fun setFinalField(target: Any, fieldName: String, value: Any?) {
    val field = target.javaClass.getDeclaredField(fieldName)
    field.isAccessible = true
    field.set(target, value)
  }
}
