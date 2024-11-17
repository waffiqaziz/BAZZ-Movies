plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.waffiq.bazz_movies.core.database"
  compileSdk = libs.versions.compileSdk.get().toInt()

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
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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
  implementation(project(":core:model"))

  // room & paging
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.paging)
  implementation(libs.androidx.paging.runtime.ktx)
  ksp(libs.androidx.room.room.compiler)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}
