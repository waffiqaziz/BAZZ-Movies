import org.gradle.kotlin.dsl.android
import org.gradle.kotlin.dsl.libs

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
}

android {
  namespace = "com.waffiq.bazz_movies.feature.favorite"
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
  implementation(project(":core-movie"))
  implementation(project(":core-user"))
  implementation(project(":core-model"))
  implementation(project(":core-database"))
  implementation(project(":core-network"))
  implementation(project(":core-ui"))
  implementation(project(":navigation"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.google.material)
  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // room & paging
  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.room.common)
  ksp(libs.androidx.room.room.compiler)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}