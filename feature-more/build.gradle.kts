plugins {
  alias(libs.plugins.dependency.analysis)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.waffiq.bazz_movies.feature.more"
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
  api(project(":core-ui"))
  api(project(":core-network"))
  api(project(":core-movie"))
  api(project(":core-user"))
  api(project(":navigation"))
  api(project(":core-database"))

  api(libs.androidx.coordinatorlayout)
  api(libs.androidx.fragment)
  api(libs.androidx.lifecycle.livedata.core)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.jetbrains.coroutines.core)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core)
  implementation(libs.androidx.lifecycle.common)

  api(libs.country.picker.android)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  api(libs.google.material)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // Hilt
  api(libs.google.dagger)
  api(libs.javax.inject)
  api(libs.hilt.android)
  api(libs.google.hilt.core)
  ksp(libs.hilt.android.compiler)
}