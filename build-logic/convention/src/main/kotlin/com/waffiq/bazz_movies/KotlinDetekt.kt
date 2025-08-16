package com.waffiq.bazz_movies

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures the detekt plugin.
 */
internal fun Project.configureDetekt(extension: DetektExtension) {
  extension.apply {
    description = "Custom DETEKT build to build baseline for all modules"
    ignoreFailures = false
    toolVersion = libs.findVersion("detekt").get().toString()
    source.setFrom("src/main/java", "src/main/kotlin")
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/baseline.xml")
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    allRules = true
    ignoredBuildTypes = listOf("release")

    dependencies { "detektPlugins"(libs.findLibrary("detekt-formatting").get()) }
  }

  tasks.withType(Detekt::class.java).configureEach {
    jvmTarget = JavaVersion.VERSION_21.toString()
    reports {
      xml.required.set(false)
      html.required.set(true)
      html.outputLocation.set(file("build/reports/detekt.html"))
      txt.required.set(false)
      sarif.required.set(false)
      md.required.set(false)
    }
  }
  tasks.withType(DetektCreateBaselineTask::class.java).configureEach {
    jvmTarget = JavaVersion.VERSION_21.toString()
  }
}
