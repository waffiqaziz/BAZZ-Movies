import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "com.waffiq.bazz_movies.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.android.tools.common)
  compileOnly(libs.firebase.crashlytics.gradlePlugin)
  compileOnly(libs.firebase.performance.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
  compileOnly(libs.room.gradlePlugin)
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "bazzmovies.android.application"
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibrary") {
      id = "bazzmovies.android.library"
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = "bazzmovies.android.feature"
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("hilt") {
      id = "bazzmovies.hilt"
      implementationClass = "HiltConventionPlugin"
    }
    register("ksp") {
      id = "bazzmovies.glide"
      implementationClass = "GlideConventionPlugin"
    }
    register("androidRoom") {
      id = "bazzmovies.android.room"
      implementationClass = "AndroidRoomConventionPlugin"
    }
    register("androidFirebase") {
      id = "bazzmovies.android.application.firebase"
      implementationClass = "AndroidApplicationFirebaseConventionPlugin"
    }
    register("androidLint") {
      id = "bazzmovies.android.lint"
      implementationClass = "AndroidLintConventionPlugin"
    }
    register("jvmLibrary") {
      id = "bazzmovies.jvm.library"
      implementationClass = "JvmLibraryConventionPlugin"
    }
  }
}
