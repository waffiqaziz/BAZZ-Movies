import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  id("bazzmovies.android.feature")
  id("kotlin-parcelize")
  id("bazzmovies.glide")
}

android.namespace = "com.waffiq.bazz_movies.feature.search"

dependencies {
  implementation(project(":core:common"))
  implementation(project(":core:domain"))
  implementation(project(":core:network"))
  implementation(project(":core:uihelper"))
  implementation(project(":core:utils"))

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.androidx.paging.runtime)

  implementation(libs.google.material)
  implementation(libs.facebook.shimmer)

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.core.testing)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.android.kotlin)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
  testImplementation(libs.turbine)
}
