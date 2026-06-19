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
    val localPropertiesFile = File(project.rootDir, "local.properties")
    val properties = Properties().apply {
      localPropertiesFile.inputStream().use { load(it) }
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
  implementation(project(":core:coroutines"))
  implementation(project(":core:models"))

  implementation(libs.androidx.paging.common)
  implementation(libs.kotlinx.coroutines.core)

  // retrofit & moshi
  api(libs.bundles.retrofit)
  implementation(libs.androidx.paging.runtime.ktx)
  ksp(libs.moshi.kotlin.codegen)

  testImplementation(project(":core:test"))
  testImplementation(libs.androidx.paging.runtime)
  testImplementation(libs.json) // using to test JSONObject
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.truth)
  testImplementation(libs.turbine)
}

kotlin {
  compilerOptions {
    // Suppress redundant .toInt() warnings in Moshi codegen generated adapters
    // This is a known kotlin issue:
    // - https://youtrack.jetbrains.com/issue/KT-80060/
    // - https://youtrack.jetbrains.com/projects/KT/issues/KT-83441/
    // - https://youtrack.jetbrains.com/issue/KT-80060/
    //
    // Fixed on Kotlin 2.4.0, but due to QodeQL not yet supported, see
    // https://github.com/github/codeql/issues/21938
    // We will remove this until its supported.
    freeCompilerArgs.addAll(
      "-Xwarning-level=REDUNDANT_CALL_OF_CONVERSION_METHOD:disabled"
    )
  }
}
