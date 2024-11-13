plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
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
  implementation(project(":core-network"))
  implementation(project(":core-ui"))
  implementation(project(":core-model"))
  implementation(project(":navigation"))
  implementation(project(":core-movie"))
  implementation(project(":core-user"))
  implementation(project(":feature-detail"))

  implementation(libs.shimmer)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.swiperefreshlayout)

  implementation(libs.androidx.paging.runtime)
  implementation(libs.androidx.viewpager2)
  implementation(libs.expandable.textview)

  implementation(libs.google.material)
  implementation(libs.android.veil)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}
