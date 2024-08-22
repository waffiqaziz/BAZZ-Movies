package com.waffiq.bazz_movies.utils.helpers

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.util.Locale
import java.util.TimeZone

object GetRegionHelper {
  private fun getNetworkLocation(context: Context): String {
    val telMgr: TelephonyManager =
      context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    return when (telMgr.simState) {
      TelephonyManager.SIM_STATE_ABSENT ->
        TimeZone.getDefault().id.lowercase()

      TelephonyManager.SIM_STATE_READY ->
        telMgr.networkCountryIso.lowercase()

      TelephonyManager.SIM_STATE_CARD_IO_ERROR -> ""

      TelephonyManager.SIM_STATE_CARD_RESTRICTED -> ""

      TelephonyManager.SIM_STATE_NETWORK_LOCKED -> ""

      TelephonyManager.SIM_STATE_NOT_READY -> ""

      TelephonyManager.SIM_STATE_PERM_DISABLED -> ""

      TelephonyManager.SIM_STATE_PIN_REQUIRED -> ""

      TelephonyManager.SIM_STATE_PUK_REQUIRED -> ""

      TelephonyManager.SIM_STATE_UNKNOWN -> ""

      else -> ""
    }
  }


  fun getLocation(context: Context): String {
    // Get network location or fallback to locale-based location
    return getNetworkLocation(context).ifEmpty {
      val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
      } else {
        @Suppress("DEPRECATION")
        context.resources.configuration.locale
      }
      locale.country.lowercase(Locale.getDefault())
    }
  }
}