import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
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
  implementation(project(":core-model"))
  implementation(project(":core-movie"))
  implementation(project(":core-network"))

  implementation(libs.androidx.lifecycle.livedata.core)
  implementation(libs.androidx.lifecycle.viewmodel)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}