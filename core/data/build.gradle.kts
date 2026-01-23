plugins {
  alias(libs.plugins.bazzmovies.android.library)
}

android {
  namespace = "com.waffiq.bazz_movies.core.data"

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = false
    }
  }
}
