plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.waffiq.bazz_movies.core.movie"
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
  implementation(project(":core-ui"))
  implementation(project(":core-model"))
  implementation(project(":core-network"))
  implementation(project(":core-database"))
  implementation(project(":navigation"))

  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.core.ktx)
  implementation(libs.google.material)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

//  testImplementation(libs.junit)
//  androidTestImplementation(libs.junit)
//  androidTestImplementation(libs.androidx.test.rules)
//  androidTestImplementation(libs.androidx.test.runner)
//  androidTestImplementation(libs.androidx.test.ext.junit)
//  androidTestImplementation(libs.espresso.core)

  // paging
  implementation(libs.androidx.paging.runtime)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}