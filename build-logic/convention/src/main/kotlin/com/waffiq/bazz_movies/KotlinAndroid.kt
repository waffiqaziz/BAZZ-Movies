package com.waffiq.bazz_movies

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid(
  commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
  commonExtension.apply {
    compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

    defaultConfig {
      minSdk = libs.findVersion("minSdk").get().toString().toInt()
    }

    compileOptions {
      // Up to Java 11 APIs are available through desugaring
      // https://developer.android.com/studio/write/java11-minimal-support-table
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
//      isCoreLibraryDesugaringEnabled = true
    }
  }
  configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
  extensions.configure<JavaPluginExtension> {
    // Up to Java 11 APIs are available through desugaring
    // https://developer.android.com/studio/write/java11-minimal-support-table
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinTopLevelExtension> Project.configureKotlin() = configure<T> {
  // Treat all Kotlin warnings as errors (disabled by default)
  // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
  val warningsAsErrors: String? by project

  // Accessing compiler options and ensuring freeCompilerArgs is properly configured
  when (this) {
    is KotlinAndroidProjectExtension -> compilerOptions
    is KotlinJvmProjectExtension -> compilerOptions
    else -> error("Unsupported project extension $this ${T::class}")
  }.apply {
    jvmTarget = JvmTarget.JVM_17
    allWarningsAsErrors = warningsAsErrors.toBoolean()

    // Directly adding to the freeCompilerArgs list
    if (project.path != ":core:model" && project.path != ":core:data") {
      freeCompilerArgs.addAll(
        listOf(
          // Enable experimental coroutines APIs, including Flow
          "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          "-opt-in=kotlinx.coroutines.FlowPreview"
        )
      )
    }
  }
}
