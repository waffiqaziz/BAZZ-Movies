plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.android.library.kover)
  alias(libs.plugins.bazzmovies.kotest)
  id("kotlin-parcelize")
}

android.namespace = "com.waffiq.bazz_movies.feature.favorite"

dependencies {
  implementation(project(":core:favoritewatchlist"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.viewpager2)

  // room
  implementation(libs.androidx.room.common)
  ksp(libs.androidx.room.compiler)

  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.junit.ktx) // used to run robolectric
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric) // used to test FavoriteViewPagerAdapter
  testImplementation(libs.turbine)

  // Allow JUnit4 tests on JUnit5 runner
  // https://github.com/kotest/kotest/issues/737#issuecomment-509998038
  testRuntimeOnly(libs.junit.vintage)

  androidTestImplementation(libs.androidx.core.testing)
  androidTestImplementation(libs.androidx.datastore.preferences)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
}
