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
  namespace = "com.waffiq.bazz_movies.feature.person"
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
  api(project(":core-network"))
  api(project(":core-ui"))
  api(project(":navigation"))
  implementation(project(":core"))

  api(libs.androidx.constraintlayout)
  api(libs.androidx.coordinatorlayout)
  api(libs.androidx.core)
  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.androidx.recyclerview)
  api(libs.androidx.viewpager2)
  api(libs.google.dagger)
  api(libs.google.hilt.core)
  api(libs.javax.inject)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.lifecycle.common)
  implementation(libs.jetbrains.coroutines.core)
  implementation(libs.jetbrains.parcelize.runtime)

  implementation(libs.androidx.core.ktx)
  api(libs.androidx.appcompat)
  api(libs.androidx.swiperefreshlayout)
  api(libs.google.material)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  api(libs.expandable.textview)

  // hilt
  api(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}