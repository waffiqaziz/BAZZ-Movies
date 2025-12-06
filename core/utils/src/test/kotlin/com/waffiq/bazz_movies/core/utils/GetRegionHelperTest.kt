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
import java.util.Locale
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
class GetRegionHelperTest {

  private val context = mockk<Context>(relaxed = true)
  private val configuration = mockk<Configuration>(relaxed = true)
  private val resources = mockk<Resources>(relaxed = true)
  private val telephonyManager = mockk<TelephonyManager>(relaxed = true)

  @Before
  fun setup() {
    every { context.resources } returns resources
    every { resources.configuration } returns configuration
    every { context.getSystemService(Context.TELEPHONY_SERVICE) } returns telephonyManager
  }

  @After
  fun cleanup() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    unmockkObject(TimeZoneHelper)
  }

  // region test helper
  private fun mockSimState(simState: Int, networkCountryIso: String? = null) {
    every { telephonyManager.simState } returns simState
    every { telephonyManager.networkCountryIso } returns networkCountryIso
  }

  private fun mockTimeZoneHelper(timeZoneId: String = "") {
    mockkObject(TimeZoneHelper)
    every { TimeZoneHelper.getDefaultTimeZoneId() } returns timeZoneId
  }

  private fun mockLocaleApi23(locale: Locale?) {
    setFinalField(configuration, locale)
  }

  private fun mockLocaleApi24(vararg locales: Locale) {
    every { configuration.locales } returns LocaleList(*locales)
  }

  private fun setFinalField(target: Any, value: Any?) {
    val field = target.javaClass.getDeclaredField("locale")
    field.isAccessible = true
    field.set(target, value)
  }
  // endregion test helper

  // region SIM state scenarios
  @Test
  fun getLocation_whenSimAbsent_returnsTimeZoneBasedLocation() {
    val originalTimeZone = TimeZone.getDefault()
    val testTimeZone = TimeZone.getTimeZone("America/New_York")
    TimeZone.setDefault(testTimeZone)

    try {
      mockSimState(TelephonyManager.SIM_STATE_ABSENT)

      assertEquals("america/new_york", GetRegionHelper.getLocation(context))
    } finally {
      TimeZone.setDefault(originalTimeZone)
    }
  }

  @Test
  fun getLocation_whenSimReady_returnsNetworkCountryIso() {
    mockSimState(TelephonyManager.SIM_STATE_READY, "us")

    assertEquals("us", GetRegionHelper.getLocation(context))
  }

  @Test
  fun getLocation_whenSimUnknown_returnsEmptyString() {
    mockSimState(TelephonyManager.SIM_STATE_UNKNOWN)

    assertEquals("", GetRegionHelper.getLocation(context))
  }
  // endregion SIM state scenarios

  // region locale fallback scenarios
  @Test
  fun getLocation_whenSimAbsentAndNoTimeZone_fallsBackToDeviceLocale() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)

    val locale = Locale.of("en", "GB")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      mockLocaleApi24(locale)
    } else {
      mockLocaleApi23(locale)
    }

    assertEquals("gb", GetRegionHelper.getLocation(context))
  }
  // endregion locale fallback scenarios

  // region API 23 specific tests
  @Test
  @Config(sdk = [23])
  fun getLocation_api23_whenNoSimAndNoLocale_returnsEmpty() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi23(null)

    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [23])
  fun getLocation_api23_withLocale_returnsLowercaseCountry() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi23(Locale.of("es", "ES"))

    assertEquals("es", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [23])
  fun getLocation_api23_withEmptyCountry_returnsEmptyString() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi23(Locale.of("", ""))

    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [23])
  fun getLocation_api23_withBlankCountry_returnsBlankString() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi23(Locale.of("en", "   "))

    assertEquals("   ", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [23])
  fun getLocation_api23_normalizesCountryToLowercase() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)

    val testCases = listOf("Us", "uS", "US", "DE")
    val expected = listOf("us", "us", "us", "de")

    testCases.zip(expected).forEach { (country, expectedResult) ->
      mockLocaleApi23(Locale.of("en", country))
      assertEquals(expectedResult, GetRegionHelper.getLocation(context))
    }
  }

  @Test
  @Config(sdk = [23])
  fun getLocation_api23_independentOfDefaultLocale() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi23(Locale.of("en", "JP"))

    val originalDefault = Locale.getDefault()
    try {
      listOf(Locale.FRANCE, Locale.ROOT).forEach { defaultLocale ->
        Locale.setDefault(defaultLocale)
        assertEquals("jp", GetRegionHelper.getLocation(context))
      }
    } finally {
      Locale.setDefault(originalDefault)
    }
  }
  // endregion API 23 specific tests

  // region API 24+ specific tests
  @Test
  @Config(sdk = [24])
  fun getLocation_api24_whenNoSimAndNoLocale_returnsEmpty() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi24()

    assertEquals("", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [24])
  fun getLocation_api24_withLocale_returnsLowercaseCountry() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi24(Locale.of("en", "US"))

    assertEquals("us", GetRegionHelper.getLocation(context))
  }

  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun getLocation_api24_withEmptyLocaleList_handlesCorrectly() {
    mockTimeZoneHelper()
    mockSimState(TelephonyManager.SIM_STATE_ABSENT)
    mockLocaleApi24()

    assertEquals("", GetRegionHelper.getLocation(context))
  }
  // endregion API 24+ specific tests

  // LocaleList utility tests
  @Test
  @Config(sdk = [Build.VERSION_CODES.N])
  fun localeListGetOrNull_whenOutOfBounds_returnsNull() {
    val localeList = LocaleList(Locale.of("en", "US"), Locale.of("fr", "FR"))

    assertNull(localeList.getOrNull(-1))
    assertNull(localeList.getOrNull(2))
  }
}
