package com.waffiq.bazz_movies.core.utils

import java.util.Locale

object LanguageHelper {

  fun getLanguageName(code: String?): String {
    return when {
      code.isNullOrBlank() -> ""
      code == "xx" -> "No Language"
      else -> {
        val locale = Locale.forLanguageTag(code)
        val displayName = locale.getDisplayLanguage(Locale.ENGLISH)
        if (displayName != code) displayName else ""
      }
    }
  }
}
