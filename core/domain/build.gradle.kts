plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.core.domain"

dependencies {
  testImplementation(libs.junit)
  testImplementation(libs.robolectric)
}
