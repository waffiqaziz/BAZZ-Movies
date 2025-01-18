import org.jetbrains.kotlin.gradle.dsl.JvmTarget
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
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.android.tools.common)
  compileOnly(libs.detekt.gradlePlugin)
  compileOnly(libs.firebase.crashlytics.gradlePlugin)
  compileOnly(libs.firebase.performance.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
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
      id = libs.plugins.bazzmovies.android.application.asProvider().get().pluginId
      implementationClass = "AndroidApplicationConventionPlugin"
    }
    register("androidLibrary") {
      id = libs.plugins.bazzmovies.android.library.get().pluginId
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = libs.plugins.bazzmovies.android.feature.get().pluginId
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("hilt") {
      id = libs.plugins.bazzmovies.hilt.asProvider().get().pluginId
      implementationClass = "HiltConventionPlugin"
    }
    register("hiltTest") {
      id = libs.plugins.bazzmovies.hilt.test.get().pluginId
      implementationClass = "HiltTestConventionPlugin"
    }
    register("ksp") {
      id = libs.plugins.bazzmovies.glide.get().pluginId
      implementationClass = "GlideConventionPlugin"
    }
    register("androidRoom") {
      id = libs.plugins.bazzmovies.android.room.get().pluginId
      implementationClass = "AndroidRoomConventionPlugin"
    }
    register("androidFirebase") {
      id = libs.plugins.bazzmovies.android.application.firebase.get().pluginId
      implementationClass = "AndroidApplicationFirebaseConventionPlugin"
    }
    register("jvmLibrary") {
      id = libs.plugins.bazzmovies.jvm.library.get().pluginId
      implementationClass = "JvmLibraryConventionPlugin"
    }
    register("kotlinDetekt") {
      id = libs.plugins.bazzmovies.detekt.get().pluginId
      implementationClass = "DetektConventionPlugin"
    }
  }
}
