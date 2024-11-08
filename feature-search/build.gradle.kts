import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.dependency.analysis)
}

android {
  namespace = "com.waffiq.bazz_movies.feature.search"
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
  api(project(":core"))
  api(project(":core-network"))
  api(project(":core-ui"))
  api(project(":navigation"))

  api(libs.androidx.coordinatorlayout)
  api(libs.androidx.fragment)
  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.androidx.paging.runtime)
  api(libs.androidx.recyclerview)
  api(libs.google.dagger)
  api(libs.google.hilt.core)
  api(libs.javax.inject)
  api(libs.jetbrains.coroutines.core)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.core)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.paging.common)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  api(libs.androidx.swiperefreshlayout)

  api(libs.google.material)
  api(libs.android.veil)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // hilt
  api(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}