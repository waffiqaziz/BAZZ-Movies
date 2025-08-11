plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
}

android{
  namespace = "com.waffiq.bazz_movies.core.designsystem"
  buildFeatures.viewBinding = true

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = false
    }
  }
}

dependencies {
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.core.splashscreen)

  api(libs.google.material)
}
