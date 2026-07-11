package com.waffiq.bazz_movies.navigation

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.core.content.IntentCompat
import com.waffiq.bazz_movies.navigation.utils.ExcludedFromGeneratedCoverageReport

@ExcludedFromGeneratedCoverageReport
inline fun <reified T : Parcelable> extractParcelableExtraFromIntent(
  intent: Intent,
  key: String,
): T? = extractParcelableExtraFromIntentInternal(intent, key, T::class.java)

@PublishedApi
@Suppress("TooGenericExceptionCaught")
internal fun <T : Parcelable> extractParcelableExtraFromIntentInternal(
  intent: Intent,
  key: String,
  clazz: Class<T>,
): T? =
  try {
    intent.setExtrasClassLoader(clazz.classLoader)
    // IntentCompat fixes a bug on Android 13 (API 33):
    // https://issuetracker.google.com/issues/274185314
    IntentCompat.getParcelableExtra(intent, key, clazz)
  } catch (e: Exception) {
    Log.e("IntentExtras", e.toString())
    null
  }
