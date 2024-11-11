plugins {
  alias(libs.plugins.dependency.analysis)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.waffiq.bazz_movies.core.database"
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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  api(project(":core-model"))

  // room & paging
  implementation(libs.androidx.room.ktx)
  api(libs.androidx.room.runtime)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite)
  ksp(libs.androidx.room.room.compiler)
  implementation(libs.jetbrains.parcelize.runtime)
  api(libs.jetbrains.coroutines.core)

  // Hilt
  implementation(libs.androidx.annotation)
  api(libs.google.dagger)
  api(libs.javax.inject)
  api(libs.hilt.android)
  implementation(libs.google.hilt.core)
  ksp(libs.hilt.android.compiler)
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}
