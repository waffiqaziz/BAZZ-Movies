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

    javaCompileOptions {
      annotationProcessorOptions {
        arguments["room.schemaLocation"] = "$projectDir/src/main/schemas"
      }
    }
  }

  buildTypes {
    getByName("debug") {
      isDebuggable = true
      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }

    create("staging") {
      isDebuggable = true
      resValue("string", "app_name", "BAZZ Movies Debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"

      isShrinkResources = true
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }

    getByName("release") {
      isDebuggable = false
      isShrinkResources = true
      isMinifyEnabled = true
      resValue("string", "app_name", "@string/app_name_release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")

      configure<CrashlyticsExtension> {
        mappingFileUploadEnabled = true
      }
    }
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true

    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "11"
  }

  buildFeatures {
    viewBinding = true
    buildConfig = true
  }

//  // disable when build ABB only enable when build APK
//  lint {
//    checkReleaseBuilds = false
//    abortOnError = false
//  }
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core-ui"))
  implementation(project(":feature-detail"))
  implementation(project(":feature-home"))
  implementation(project(":feature-person"))
  implementation(project(":feature-search"))
  implementation(project(":navigation"))

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
//  debugImplementation(libs.leakcanary)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // third-party library
  implementation(libs.expandable.textview)
  implementation(libs.my.country.picker)

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
