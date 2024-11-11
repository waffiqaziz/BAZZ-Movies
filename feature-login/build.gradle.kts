import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.dependency.analysis)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.feature.login"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
    }

    create("staging") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
  }
}

dependencies {
  api(project(":core-user"))
  api(project(":core-ui"))
  api(project(":navigation"))
  implementation(project(":core-network"))
  implementation(project(":core-movie"))

  implementation(libs.androidx.core.ktx)
  api(libs.androidx.appcompat)
  implementation(libs.google.material)

  api(libs.androidx.constraintlayout)
  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.google.dagger)
  api(libs.google.hilt.core)
  api(libs.javax.inject)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.core)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.jetbrains.coroutines.core)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // hilt
  api(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}