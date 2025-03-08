plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
}

android.namespace = "com.waffiq.bazz_movies.core.common"

dependencies {
  testImplementation(libs.junit)
}
