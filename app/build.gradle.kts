import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
}

// apply the Google Services and Crashlytics if exist
if (file("${project.rootDir}/app/google-services.json").exists()) {
  apply(plugin = libs.plugins.gms.googleServices.get().pluginId)
  apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
} else {
  println("google-services.json is missing. Plugins not applied.")
}

android {
  compileSdk = 34
  namespace = "com.waffiq.bazz_movies"

  defaultConfig {
    applicationId = "com.bazz.bazz_movies"
    minSdk = 23
    targetSdk = 34
    versionCode = 11
    versionName = "1.0.10"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    debug {
      isDebuggable = true

      // disable below for faster development flow.
//      isShrinkResources = true
//      isMinifyEnabled = true
//      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
    release {
      isDebuggable = false
      isShrinkResources = true
      isMinifyEnabled = true
      resValue("string", "app_name", "@string/app_name_release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      configure<CrashlyticsExtension> {
        mappingFileUploadEnabled = true
      }
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
    buildConfig = true
  }
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core_ui"))
  
  // jetpack library

  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.viewpager2)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.paging.runtime.ktx)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // leakcanary
  debugImplementation(libs.leakcanary)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // third-party library
  implementation(libs.expandable.textview)
  implementation(libs.country.picker)

  // play integrity
  implementation(libs.play.integrity)

  // firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  // hilt
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}

repositories {
  mavenCentral()
}
