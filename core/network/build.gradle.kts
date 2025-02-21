import org.gradle.kotlin.dsl.android
import java.util.Properties
import kotlin.apply

plugins {
  alias(libs.plugins.bazzmovies.android.library)
  alias(libs.plugins.bazzmovies.android.library.jacoco)
  alias(libs.plugins.bazzmovies.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.core.network"
  buildFeatures.buildConfig = true

  defaultConfig {
    val properties = Properties().apply {
      load(project.rootProject.file("local.properties").inputStream())
    }

    // API KEY
    buildConfigField("String", "TMDB_API_KEY", "\"${properties["TMDB_API_KEY"]}\"")
    buildConfigField("String", "OMDB_API_KEY", "\"${properties["OMDB_API_KEY"]}\"")

    // BASE URL
    buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/\"")
    buildConfigField("String", "OMDb_API_URL", "\"https://www.omdbapi.com/\"")
  }
}

dependencies {
  implementation(project(":core:domain"))

  implementation(libs.androidx.paging.common)
  implementation(libs.jetbrains.coroutines.core)

  // retrofit & moshi
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp.logging.interceptor)
  ksp(libs.moshi.kotlin.codegen)

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.paging.runtime)
  testImplementation(libs.junit)
  testImplementation(libs.json) // using to test JSONObject
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
}
