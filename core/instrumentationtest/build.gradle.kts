plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.hilt)
}

android{
  namespace = "com.waffiq.bazz_movies.core.instrumentationtest"

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = false
    }
  }
}

dependencies {
  implementation(project(":core:designsystem"))
  implementation(libs.androidx.appcompat)
  implementation(libs.hilt.test)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.test.core)
  implementation(libs.androidx.test.runner)
}
