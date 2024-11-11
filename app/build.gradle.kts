import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
  alias(libs.plugins.dependency.analysis)
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
  compileSdk = libs.versions.compileSdk.get().toInt()
  namespace = "com.waffiq.bazz_movies"
  ndkVersion = libs.versions.ndkVersion.get()

  defaultConfig {
    applicationId = "com.bazz.bazz_movies"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 13
    versionName = "1.1.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    signingConfig = signingConfigs.getByName("debug")

    javaCompileOptions {
      annotationProcessorOptions {
        arguments["room.schemaLocation"] = "$projectDir/src/main/schemas"
      }
    }
  }

  buildTypes {
    getByName("debug") {
      isDebuggable = true
      isShrinkResources = false
      isMinifyEnabled = false

      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }

    create("staging") {
      isDebuggable = true
      isShrinkResources = true
      isMinifyEnabled = true

      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }

    getByName("release") {
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
  implementation(project(":core-movie"))
  implementation(project(":core-user"))
  implementation(project(":core-network"))
  implementation(project(":core-ui"))
  implementation(project(":core-model"))
  implementation(project(":core-database"))
  implementation(project(":feature-detail"))
  implementation(project(":feature-home"))
  implementation(project(":feature-favorite"))
  implementation(project(":feature-watchlist"))
  implementation(project(":feature-login"))
  implementation(project(":feature-more"))
  implementation(project(":feature-person"))
  implementation(project(":feature-search"))
  implementation(project(":navigation"))

  // jetpack library
  implementation(libs.androidx.core)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.fragment)
  implementation(libs.androidx.annotation)
  implementation(libs.androidx.coordinatorlayout)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core.splashscreen)

  implementation(libs.androidx.lifecycle.common)
  implementation(libs.androidx.lifecycle.livedata.core)
  implementation(libs.androidx.lifecycle.viewmodel)
  implementation(libs.androidx.lifecycle.viewmodel.savedstate)

  implementation(libs.androidx.navigation.ui)
  implementation(libs.androidx.navigation.fragment)
  implementation(libs.androidx.navigation.runtime)

  implementation(libs.androidx.paging.common)
  implementation(libs.androidx.paging.runtime)

  coreLibraryDesugaring(libs.desugar.jdk.libs)
  implementation(libs.google.material)

  // leakcanary
  debugImplementation(libs.leakcanary)

  // glide
  ksp(libs.glide.compiler)

  // third-party library
  implementation(libs.expandable.textview)

  // play integrity
  implementation(libs.play.integrity)

  // firebase
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  implementation(libs.androidx.datastore.core)
  implementation(libs.androidx.datastore.preferences.core)

  implementation(libs.okhttp)

  // hilt
  implementation(libs.guava)
  implementation(libs.google.dagger)
  implementation(libs.google.hilt.core)
  implementation(libs.javax.inject)
  implementation(libs.hilt.android)
  ksp(libs.hilt.android.compiler)
}
