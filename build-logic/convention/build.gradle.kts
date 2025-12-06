import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

group = "com.waffiq.bazz_movies.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile> {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_21)
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
  compileOnly(libs.kover.gradlePlugin)
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
      id = libs.plugins.bazzmovies.android.library.asProvider().get().pluginId
      implementationClass = "AndroidLibraryConventionPlugin"
    }
    register("androidFeature") {
      id = libs.plugins.bazzmovies.android.feature.get().pluginId
      implementationClass = "AndroidFeatureConventionPlugin"
    }
    register("androidApplicationJacoco") {
      id = libs.plugins.bazzmovies.android.application.jacoco.get().pluginId
      implementationClass = "AndroidApplicationJacocoConventionPlugin"
    }
    register("androidLibraryJacoco") {
      id = libs.plugins.bazzmovies.android.library.jacoco.get().pluginId
      implementationClass = "AndroidLibraryJacocoConventionPlugin"
    }
    register("androidLibraryKover") {
      id = libs.plugins.bazzmovies.android.library.kover.get().pluginId
      implementationClass = "AndroidLibraryKoverConventionPlugin"
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
    register("sharedTest") {
      id = libs.plugins.bazzmovies.shared.test.get().pluginId
      implementationClass = "SharedTestConventionPlugin"
    }
  }
}
