plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.dependency.analysis)
}

android {
  namespace = "com.waffiq.bazz_movies.feature.home"
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
  implementation(project(":navigation"))
  implementation(project(":core-user"))
  implementation(project(":feature-detail"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  api(libs.androidx.swiperefreshlayout)

  api(libs.androidx.coordinatorlayout)
  api(libs.androidx.core)
  api(libs.androidx.fragment)
  api(libs.androidx.lifecycle.common)
  api(libs.androidx.lifecycle.viewmodel)
  api(libs.androidx.paging.runtime)
  api(libs.androidx.recyclerview)
  api(libs.androidx.viewpager2)
  api(libs.google.dagger)
  api(libs.google.hilt.core)
  api(libs.javax.inject)
  api(libs.jetbrains.coroutines.core)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.lifecycle.livedata.core)
  implementation(libs.androidx.paging.common)
  implementation(libs.expandable.textview)

  api(libs.google.material)
  api(libs.android.veil)

  implementation(libs.google.material)
  implementation(libs.android.veil)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // hilt
  api(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}