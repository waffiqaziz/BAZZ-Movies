import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("kotlin-parcelize")
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

    val properties = Properties().apply {
      load(project.rootProject.file("local.properties").inputStream())
    }

    // API KEY inside local.properties
    buildConfigField("String", "API_KEY", "\"${properties["API_KEY"]}\"")
    buildConfigField("String", "API_KEY_OMDb", "\"${properties["API_KEY_OMDb"]}\"")

    // BASE URL
    buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/\"")
    buildConfigField("String", "OMDb_API_URL", "\"https://www.omdbapi.com/\"")

    // room database schema location for migration
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }

  buildTypes {
    debug {
      isDebuggable = true

      // disable below for faster development flow.
//      isShrinkResources = true
//      isMinifyEnabled = true
//      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      resValue("string", "app_name", "@string/app_name_debug")
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
  // core
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.activity.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  implementation(libs.androidx.swiperefreshlayout)
  implementation(libs.androidx.legacy.support.v4)

  // splashscreen
  implementation(libs.androidx.core.splashscreen)

  // viewpager2
  implementation(libs.androidx.viewpager2)

  // lifecycle
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  // datastore
  implementation(libs.androidx.datastore.preferences)

  // room & paging
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.paging)
  implementation(libs.androidx.paging.runtime.ktx)
  implementation(libs.androidx.legacy.support.v4)
  ksp(libs.androidx.room.room.compiler)

  // material3
  implementation(libs.google.material)

  // retrofit & moshi
  implementation(libs.retrofit)
  implementation(libs.retrofit.converter.moshi)
  implementation(libs.moshi.kotlin)
  ksp(libs.moshi.kotlin.codegen)
  implementation(libs.okhttp.logging.interceptor)

  // testing
  coreLibraryDesugaring(libs.desugar.jdk.libs)
  testImplementation(libs.junit)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.rules)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.ext.junit)

  // disable for faster build
  debugImplementation(libs.leakcanary)

  // glide
  implementation(libs.glide)
  ksp(libs.glide.compiler)

  // third-party library
  implementation(libs.expandable.textview)
  implementation(libs.country.picker)

  // firebase
  implementation(libs.play.integrity)
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.crashlytics)
  implementation(libs.firebase.analytics)

  // Dagger
  implementation(libs.dagger)
  ksp(libs.dagger.ksp)
}

repositories {
  mavenCentral()
}
