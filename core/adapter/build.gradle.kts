plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.glide)
}

android.namespace = "com.waffiq.bazz_movies.core.adapter"

dependencies {
  api(project(":core:designsystem"))
  api(project(":core:uihelper"))
  api(project(":core:utils"))

  implementation(project(":core:models"))

  testImplementation(libs.androidx.test.core)
  testImplementation(libs.robolectric)
  testImplementation(libs.truth)
}