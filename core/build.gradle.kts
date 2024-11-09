plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.ksp)
  alias(libs.plugins.dependency.analysis)
}

android {
  namespace = "com.waffiq.bazz_movies.core"
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
  api(project(":core-ui"))
  api(project(":core-network"))

  api(libs.androidx.core)
  api(libs.androidx.fragment)
  api(libs.androidx.lifecycle.common)
  implementation(libs.androidx.core.ktx)

  api(libs.jetbrains.coroutines.core)

  // for item layout
  api(libs.androidx.recyclerview)
  api(libs.google.material)
  implementation(libs.androidx.constraintlayout)
  coreLibraryDesugaring(libs.desugar.jdk.libs)

  implementation(libs.androidx.annotation)

//  testImplementation(libs.junit)
//  androidTestImplementation(libs.junit)
//  androidTestImplementation(libs.androidx.test.rules)
//  androidTestImplementation(libs.androidx.test.runner)
//  androidTestImplementation(libs.androidx.test.ext.junit)
//  androidTestImplementation(libs.espresso.core)

  // room & paging
  api(libs.androidx.paging.common)
  api(libs.androidx.paging.runtime)
  implementation(libs.androidx.room.ktx)
  api(libs.androidx.room.runtime)
  implementation(libs.androidx.room.common)
  implementation(libs.androidx.sqlite)
  ksp(libs.androidx.room.room.compiler)
  implementation(libs.jetbrains.parcelize.runtime)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // Hilt
  api(libs.google.dagger)
  api(libs.javax.inject)
  api(libs.hilt.android)
  implementation(libs.google.hilt.core)
  ksp(libs.hilt.android.compiler)
}

ksp {
  arg("room.schemaLocation", "$projectDir/schemas")
}
