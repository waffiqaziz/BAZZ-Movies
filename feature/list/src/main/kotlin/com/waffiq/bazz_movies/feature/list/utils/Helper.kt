package com.waffiq.bazz_movies.feature.list.utils

object Helper {
  fun String.capitaliseEachWord(): String {
    val regex = "(\\b[a-z](?!\\s))".toRegex()
    return this.replace(regex) { it.value.uppercase() }
  }
}
