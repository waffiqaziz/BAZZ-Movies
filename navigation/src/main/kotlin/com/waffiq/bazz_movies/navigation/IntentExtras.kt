package com.waffiq.bazz_movies.navigation

import android.content.Intent
import android.os.Parcelable
import androidx.core.content.IntentCompat

@Suppress("TooGenericExceptionCaught")
inline fun <reified T : Parcelable> extractParcelableExtraFromIntent(
  intent: Intent,
  key: String,
): T? =
  try {
    intent.setExtrasClassLoader(T::class.java.classLoader)

    // IntentCompat handles the API version branching safely,
    // resolve the Android 13 (API 33) framework bug.
    // https://issuetracker.google.com/issues/274185314
    IntentCompat.getParcelableExtra(intent, key, T::class.java)
  } catch (_: Exception) {
    null
  }
