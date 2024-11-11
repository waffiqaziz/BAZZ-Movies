import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.dependency.analysis)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.waffiq.bazz_movies.core.user"
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
}

dependencies {
  api(project(":core-movie"))
  api(project(":core-network"))

  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  implementation(libs.androidx.lifecycle.livedata)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  api(libs.androidx.datastore.core)
  api(libs.androidx.datastore.preferences.core)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.jetbrains.parcelize.runtime)

  // Hilt
  api(libs.hilt.android)
  api(libs.google.dagger)
  api(libs.javax.inject)
  api(libs.jetbrains.coroutines.core)
  implementation(libs.google.hilt.core)
  ksp(libs.hilt.android.compiler)
}