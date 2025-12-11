plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.android.library.kover)
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.favorite"

  // required for Kotest run with JUnit5
  // https://kotest.io/docs/5.9.x/quickstart
  testOptions {
    unitTests.all {
      it.useJUnitPlatform()
    }
  }
}

dependencies {
  implementation(project(":core:favoritewatchlist"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.swiperefreshlayout)

  // room
  implementation(libs.androidx.room.common)
  ksp(libs.androidx.room.compiler)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.bundles.kotest)
  testImplementation(libs.androidx.junit.ktx) // used to run robolectric
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric) // used to test FavoriteViewPagerAdapter
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
  testRuntimeOnly(libs.junit.vintage) // allow JUnit4 tests on JUnit5 runner

  androidTestImplementation(libs.androidx.core.testing)
  androidTestImplementation(libs.androidx.datastore.preferences)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.espresso.contrib)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.truth)

//  androidTestImplementation(libs.kotest.property)
//  androidTestImplementation(libs.kotest.runner.junit5)
//  androidTestImplementation(libs.kotest.kotest.assertions.core)
//  androidTestImplementation(libs.kotest.framework.concurrency)
}
