plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.room)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android.namespace = "com.waffiq.bazz_movies.core.database"

dependencies {
  api(project(":core:domain"))
  api(project(":core:common"))
  implementation(project(":core:coroutines"))
  implementation(project(":core:utils"))
}
