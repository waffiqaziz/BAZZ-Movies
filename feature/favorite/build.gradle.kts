import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs
import org.gradle.kotlin.dsl.testImplementation

plugins {
  alias(libs.plugins.bazzmovies.android.feature)
  alias(libs.plugins.bazzmovies.android.library.kover)
  id("kotlin-parcelize")
}

android {
  namespace = "com.waffiq.bazz_movies.feature.favorite"

  // required for Kotest
  // https://kotest.io/docs/quickstart/#test-framework
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
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)

  androidTestImplementation(libs.androidx.core.testing)
  androidTestImplementation(libs.androidx.datastore.preferences)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.kotlinx.coroutines.test)
  androidTestImplementation(libs.mockk.android)
  androidTestImplementation(libs.truth)

//  androidTestImplementation(libs.kotest.property)
//  androidTestImplementation(libs.kotest.runner.junit5)
//  androidTestImplementation(libs.kotest.kotest.assertions.core)
//  androidTestImplementation(libs.kotest.framework.concurrency)
}
