package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class AccountMediaCategory {
  FAVORITE,
  WATCHLIST,
  ;

    fun asApiValue(): String =
    when (this) {
      FAVORITE -> "favorite"
      WATCHLIST -> "watchlist"
    }
}
