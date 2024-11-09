import java.util.Properties
import kotlin.apply

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
  alias(libs.plugins.dependency.analysis)
}

android {
  namespace = "com.waffiq.bazz_movies.core.network"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

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

  buildTypes {
    buildTypes {
      getByName("debug") {
        isMinifyEnabled = false
      }

      create("staging") {
        isMinifyEnabled = true
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }

      getByName("release") {
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  api(libs.androidx.paging.common)
  api(libs.jetbrains.coroutines.core)

  // retrofit & moshi
  api(libs.retrofit)
  api(libs.okhttp)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.okhttp.logging.interceptor)
  api(libs.moshi)
  implementation(libs.moshi.kotlin)
  ksp(libs.moshi.kotlin.codegen)

  // Hilt
  api(libs.google.dagger)
  api(libs.javax.inject)
  api(libs.hilt.android)
  implementation(libs.google.hilt.core)
  ksp(libs.hilt.android.compiler)
}