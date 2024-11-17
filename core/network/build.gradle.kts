import org.gradle.kotlin.dsl.android
import java.util.Properties
import kotlin.apply

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
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
  implementation(libs.androidx.paging.common)
  implementation(libs.jetbrains.coroutines.core)

  // retrofit & moshi
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.moshi.kotlin)
  ksp(libs.moshi.kotlin.codegen)
  implementation(libs.okhttp.logging.interceptor)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}
