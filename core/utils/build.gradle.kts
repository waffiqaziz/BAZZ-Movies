import org.gradle.kotlin.dsl.android

plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
}

android.namespace = "com.waffiq.bazz_movies.core.utils"

dependencies {
  implementation(project(":core:designsystem"))
  implementation(project(":core:domain"))
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.paging.runtime)

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.robolectric)
}
