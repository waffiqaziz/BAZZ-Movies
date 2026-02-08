package com.waffiq.bazz_movies

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures the detekt plugin.
 */
internal fun Project.configureDetekt(extension: DetektExtension) {
  extension.apply {
    description = "Custom DETEKT build to build baseline for all modules"
    ignoreFailures.set(false)
    toolVersion.set(libs.findVersion("detekt").get().toString())
    source.setFrom("src/main/java", "src/main/kotlin")
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    baseline.set(file("$rootDir/config/baseline.xml"))
    parallel.set(true)
    buildUponDefaultConfig.set(true)
    autoCorrect.set(true)
    allRules.set(false)
    ignoredBuildTypes.set(listOf("release"))
  }

  dependencies {
    "detektPlugins"(libs.findLibrary("detekt-rules-ktlint-wrapper").get())
  }

  tasks.withType(Detekt::class.java).configureEach {
    exclude("**/build/**")
    jvmTarget.set(JavaVersion.VERSION_21.toString())
    reports {
      checkstyle.required.set(false)
      html.required.set(true)
      html.outputLocation.set(file("build/reports/detekt.html"))
      sarif.required.set(false)
      markdown.required.set(false)
    }
  }

  tasks.withType(DetektCreateBaselineTask::class.java).configureEach {
    jvmTarget.set(JavaVersion.VERSION_21.toString())
  }
}
