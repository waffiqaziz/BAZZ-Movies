plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.core.models"

dependencies {
  implementation(project(":core:common"))

  testImplementation(libs.robolectric)
}
