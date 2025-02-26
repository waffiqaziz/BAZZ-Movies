package com.waffiq.bazz_movies.core.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import java.util.Locale
import java.util.TimeZone

/**
 * Helper object to determine the user's region using SIM card information or default phone settings.
 *
 * [GetRegionHelper] provides a mechanism to detect the user's region based on the state of the SIM card
 * in the device's telephony system using [getNetworkLocation]. If no valid region can be derived
 * from the SIM card, it falls back to using the phone's locale settings.
 */
object GetRegionHelper {

  /**
   * Retrieves the region based on the device's network and SIM card status.
   *
   * This function determines the network location using the SIM state. It returns the SIM's country code
   * if the SIM is present and ready. If the SIM is absent, it defaults to the device's current time zone.
   * In all other cases, it returns an empty string if a valid network region cannot be determined.
   *
   * @param context The application context used to access system services.
   * @return A string representing the network's region code or an empty string if it cannot be determined.
   */
  private fun getNetworkLocation(context: Context): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    return when (telephonyManager.simState) {
      TelephonyManager.SIM_STATE_ABSENT ->
        // No SIM card available, return the device's default time zone ID (lowercase).
        TimeZoneHelper.getDefaultTimeZoneId().lowercase().ifBlank { "" }

      TelephonyManager.SIM_STATE_READY ->
        // SIM card is active; return the network's country code (lowercase).
        telephonyManager.networkCountryIso.lowercase()

      else ->
        // For other SIM states, such as locked or unavailable, return an empty string.
        ""
    }
  }

  /**
   * Retrieves the most accurate available location for the user.
   *
   * This function tries to determine the user's location using network data first. If no network location
   * can be derived, it falls back to the primary device locale to obtain the user's country setting.
   *
   * @param context The application context used to access locale configurations.
   * @return A string representing the user's region in lowercase.
   */
  fun getLocation(context: Context): String {
    // Attempt to get network-based location, fall back to device locale if empty.
    return getNetworkLocation(context).ifEmpty {
      val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // For Android Nougat (API level 24) and above, support multiple locales;
        // use the first locale as the highest-priority locale selected by the user.
        context.resources.configuration.locales.getOrNull(0)
      } else { // for android API 23 a.k.a Marshmallow
        @Suppress("DEPRECATION")
        // Fallback to the single locale for older Android versions.
        context.resources.configuration.locale
      }
      locale?.country?.lowercase(Locale.getDefault()) ?: ""
    }
  }

  /**
   * Helper extension for getting a locale safely for API 24 and up
   */
  @RequiresApi(Build.VERSION_CODES.N)
  internal fun LocaleList.getOrNull(index: Int): Locale? {
    return if (index in 0 until size()) get(index) else null
  }
}

/**
 * Used as wrapper time zone
 *
 * @return A string region code. Example : "ID", "MY", "RU", etc
 */
object TimeZoneHelper {
  fun getDefaultTimeZoneId(): String {
    return TimeZone.getDefault().id
  }
}
