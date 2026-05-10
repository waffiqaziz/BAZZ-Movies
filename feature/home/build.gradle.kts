plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.glide)
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.home"

  testOptions {
    unitTests.apply {
      isIncludeAndroidResources = true
    }
  }
}

dependencies {
  implementation(project(":core:data"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.viewpager2)

  implementation(libs.google.material)
  implementation(libs.facebook.shimmer)

  testImplementation(libs.androidx.lifecycle.runtime.testing)
  testImplementation(libs.androidx.paging.common)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)

  androidTestImplementation(libs.androidx.core.testing)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.truth)
}
