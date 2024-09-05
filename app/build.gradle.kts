import java.util.Properties

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.google.firebase.crashlytics")
  id("com.google.devtools.ksp")
}

apply(plugin = "kotlin-android")
apply(plugin = "com.google.gms.google-services")
apply(plugin = "com.google.firebase.crashlytics")

android {
  compileSdk = 34

  defaultConfig {
    applicationId = "com.bazz.bazz_movies"
    minSdk = 23
    targetSdk = 34
    versionCode = 9
    versionName = "1.0.8"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val properties = Properties().apply {
      load(project.rootProject.file("local.properties").inputStream())
    }

    // API KEY inside local.properties
    buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    buildConfigField("String", "API_KEY_OMDb", "\"${properties.getProperty("API_KEY_OMDb")}\"")

    // BASE URL
    buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/\"")
    buildConfigField("String", "OMDb_API_URL", "\"https://www.omdbapi.com/\"")

    // room database schema location for migration
    ksp {
      arg("room.schemaLocation", "$projectDir/schemas")
    }
  }

  buildTypes {
    getByName("debug") {
      isDebuggable = true

      // disable below for faster development flow.
//            shrinkResources = true
//            isMinifyEnabled = true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      resValue("string", "app_name", "@string/app_name_debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
    getByName("release") {
      isDebuggable = false
      isShrinkResources = true
      isMinifyEnabled = true
      resValue("string", "app_name", "@string/app_name_release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

      configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
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
  namespace = "com.waffiq.bazz_movies"
}

dependencies {
  // core
  implementation("androidx.core:core-ktx:1.13.0")
  implementation("androidx.activity:activity-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("androidx.cardview:cardview:1.0.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
  implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

  // splashscreen
  implementation("androidx.core:core-splashscreen:1.0.1")

  // viewpager2
  implementation("androidx.viewpager2:viewpager2:1.0.0")

  // lifecycle
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

  // datastore
  implementation("androidx.datastore:datastore-preferences:1.1.0")

  // room & paging
  implementation("androidx.room:room-runtime:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  implementation("androidx.room:room-paging:2.6.1")
  implementation("androidx.paging:paging-runtime-ktx:3.2.1")
  implementation("androidx.legacy:legacy-support-v4:1.0.0")
  ksp("androidx.room:room-compiler:2.6.1")

  // google material3
  implementation("com.google.android.material:material:1.11.0")

  // retrofit & moshi
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
  ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
  implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")

  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

  // testing
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test:rules:1.5.0")
  androidTestImplementation("androidx.test:runner:1.5.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("junit:junit:4.13.2")

  // glide
  implementation("com.github.bumptech.glide:glide:4.16.0")
  ksp("com.github.bumptech.glide:compiler:4.16.0")

  // ExpandableTextView
  implementation("io.github.glailton.expandabletextview:expandabletextview:1.0.4")

  // AndroidCountryPicker
  implementation("com.hbb20:ccp:2.7.0")

  // play integrity
  implementation("com.google.android.play:integrity:1.3.0")

  // Crashlytics
  implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
  implementation("com.google.firebase:firebase-crashlytics")
  implementation("com.google.firebase:firebase-analytics")
}

repositories {
  mavenCentral()
}