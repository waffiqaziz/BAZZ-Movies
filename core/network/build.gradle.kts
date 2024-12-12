import org.gradle.kotlin.dsl.android
import java.util.Properties
import kotlin.apply

plugins {
  id("bazzmovies.android.library")
  id("bazzmovies.hilt")
}

android {
  namespace = "com.waffiq.bazz_movies.core.network"

  defaultConfig {
    val properties = Properties().apply {
      load(project.rootProject.file("local.properties").inputStream())
    }

    // API KEY inside local.properties
    buildConfigField("String", "API_KEY", "\"${properties["API_KEY"]}\"")
    buildConfigField("String", "API_KEY_OMDb", "\"${properties["API_KEY_OMDb"]}\"")

    // BASE URL
    buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/\"")
    buildConfigField("String", "OMDb_API_URL", "\"https://www.omdbapi.com/\"")
  }

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(libs.androidx.paging.common)
  implementation(libs.jetbrains.coroutines.core)

  // retrofit & moshi
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.moshi.kotlin)
  implementation(libs.okhttp.logging.interceptor)
  ksp(libs.moshi.kotlin.codegen)
}
