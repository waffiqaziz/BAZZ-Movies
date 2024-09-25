package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

/**
 * Used to get user region via SIM Card and default phone configuration
 */
object GetRegionHelper {
  private fun getNetworkLocation(context: Context): String {
    val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    return when (telMgr.simState) {
      // If the SIM card is absent, returns the default time zone ID of the device
      TelephonyManager.SIM_STATE_ABSENT -> TimeZone.getDefault().id.lowercase()

      // If the SIM card is ready (i.e., active and operational), returns the country code
      // of the mobile network (telMgr.networkCountryIso), which indicates the country where
      // the network is registered.
      TelephonyManager.SIM_STATE_READY -> telMgr.networkCountryIso.lowercase()

      // Other SIM States indicating no valid network location can be determined.
      else -> ""
    }
  }

  fun getLocation(context: Context): String {
    // if the network location is empty, fallback to the locale's country
    return getNetworkLocation(context).ifEmpty {
      val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        // Starting from Android Nougat (API level 24), device can support multiple locales, so
        // get this first locale which highest-priority locale chosen by the user
        context.resources.configuration.locales.get(0)
      } else {
        @Suppress("DEPRECATION")
        context.resources.configuration.locale
      }
      locale.country.lowercase(Locale.getDefault())
    }
  }
}
