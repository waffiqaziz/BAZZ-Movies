plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.hilt)
  alias(libs.plugins.bazzmovies.shared.test)
}

android.namespace = "com.waffiq.bazz_movies.core.instrumentationtest"

dependencies {
  implementation(project(":core:designsystem"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.espresso.contrib)
  implementation(libs.androidx.test.core)
  implementation(libs.androidx.test.runner)
  implementation(libs.hilt.test)
  implementation(libs.mockk.android)
}
