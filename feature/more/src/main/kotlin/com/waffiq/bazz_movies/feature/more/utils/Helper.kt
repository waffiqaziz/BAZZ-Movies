package com.waffiq.bazz_movies.feature.more.utils

object Helper {
  fun String.validName(fallback: String): String = this.ifEmpty { fallback }
}
