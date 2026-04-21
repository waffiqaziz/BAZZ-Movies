package com.waffiq.bazz_movies.feature.login.utils.openurl

fun interface UriLauncher {
  fun launch(url: String): Result<Unit>
}
