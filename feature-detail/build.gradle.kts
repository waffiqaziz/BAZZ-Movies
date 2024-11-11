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
  namespace = "com.waffiq.bazz_movies.feature.detail"
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
  api(project(":core-movie"))
  api(project(":core-user"))
  api(project(":core-model"))
  api(project(":core-database"))
  api(project(":core-network"))
  api(project(":core-ui"))
  api(project(":navigation"))

  api(libs.androidx.core)
  implementation(libs.androidx.core.ktx)
  api(libs.androidx.coordinatorlayout)
  api(libs.androidx.constraintlayout)
  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.androidx.recyclerview)
  api(libs.androidx.cardview)
  api(libs.androidx.appcompat)
  api(libs.google.material)

  api(libs.androidx.paging.runtime)

  api(libs.jetbrains.coroutines.core)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  api(libs.androidx.swiperefreshlayout)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.livedata)
  implementation(libs.androidx.lifecycle.runtime)
  implementation(libs.androidx.paging.common)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  api(libs.expandable.textview)

  // hilt
  api(libs.google.hilt.core)
  api(libs.hilt.android)
  api(libs.google.dagger)
  api(libs.javax.inject)
  ksp(libs.hilt.android.compiler)
}