plugins {
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
  implementation(project(":core-ui"))
  implementation(project(":core-network"))
  implementation(project(":core-movie"))
  implementation(project(":core-model"))
  implementation(project(":core-user"))
  implementation(project(":navigation"))
  implementation(project(":core-database"))

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.google.material)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  implementation(libs.country.picker.android)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // Hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}