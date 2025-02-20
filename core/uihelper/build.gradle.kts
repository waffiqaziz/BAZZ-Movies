plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
}

android.namespace = "com.waffiq.bazz_movies.core.uihelper"

dependencies {
  api(project(":core:common"))
  implementation(project(":core:designsystem"))

  api(libs.androidx.core.ktx)
  implementation(libs.androidx.paging.runtime)

  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.junit)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
}