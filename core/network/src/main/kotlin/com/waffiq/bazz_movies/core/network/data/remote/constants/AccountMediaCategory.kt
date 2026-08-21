package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class AccountMediaCategory {
  FAVORITE,
  WATCHLIST,
}

val AccountMediaCategory.value: String
  get() = name.lowercase()
