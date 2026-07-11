package com.waffiq.bazz_movies.navigation

import android.content.Intent
import android.os.Parcelable
import android.util.Log
import androidx.core.content.IntentCompat

/*
  JaCoCo skips coverage checks on anything annotated with a name containing "Generated" this uses
  that on purpose.

  Coverage for Kotlin inline functions is calculated from the call site, not the function itself.
  This function is only ever called from a test class, and others module which cannot covered and
  JaCoCo's Gradle plugin doesn't analyze test classes (only main code), so it never sees the call
  site and always reports this as uncovered, even though it's tested. This is a confirmed
  JaCoCo/Gradle limitation:

  - https://github.com/jacoco/jacoco/issues/1873#issuecomment-2785754199
  - https://github.com/jacoco/jacoco/issues/1921#issuecomment-3098557834
*/

@Retention(AnnotationRetention.BINARY)
annotation class ExcludedFromGeneratedCoverageReport

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
): T? = try {
  intent.setExtrasClassLoader(clazz.classLoader)
  // IntentCompat fixes a bug on Android 13 (API 33):
  // https://issuetracker.google.com/issues/274185314
  IntentCompat.getParcelableExtra(intent, key, clazz)
} catch (e: Exception) {
  Log.e("IntentExtras", e.toString())
  null
}

