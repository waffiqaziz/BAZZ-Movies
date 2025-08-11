plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.navigation"

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = false
    }
  }
}

dependencies {
  implementation(project(":core:domain"))
}
