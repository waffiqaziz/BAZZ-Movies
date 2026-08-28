package com.waffiq.bazz_movies.core.utils

import android.content.Context
import android.os.LocaleList
import android.telephony.TelephonyManager
import androidx.annotation.VisibleForTesting
import java.util.Locale

/**
 * Helper object get the user's region using SIM card information or default phone settings.
 */
object GetRegionHelper {

  /**
   * Retrieves the region based on the device's network and SIM card status.
   *
   * This function determines the network location using the SIM state. It returns based in order:
   * - SIM's country code if the SIM is present and ready
   * - Defaults to the device's current time zone
   * - Fallback to empty string if a valid network region cannot be determined.
   *
   * @param context The application context used to access system services.
   * @return A string representing the network's region code or an empty string if it cannot be
   *         determined.
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
   * Retrieves available location for the user.
   *
   * This function tries to determine the user's location using network data first.
   * If no network location can be derived, it falls back to the primary device locale
   * to obtain the user's country setting.
   *
   * @param context The application context used to access locale configurations.
   * @return A string representing the user's region in lowercase.
   */
  fun getLocation(context: Context): String {
    return getNetworkLocation(context).ifEmpty {
      val locale = context.resources.configuration.locales.getOrNull(0)
      locale?.country?.lowercase(Locale.getDefault()).orEmpty()
    }
  }

  /**
   * Helper extension for getting a locale safely for API 24 and up
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun LocaleList.getOrNull(index: Int): Locale? = if (index in 0 until size()) get(index) else null
}
