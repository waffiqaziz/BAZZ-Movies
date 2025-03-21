package com.waffiq.bazz_movies

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

/**
 * Configures the detekt plugin.
 */
internal fun Project.configureDetekt(extension: DetektExtension) {
  extension.apply {
    toolVersion = "1.23.7"
    source.setFrom("src/main/java", "src/main/kotlin")
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/baseline.xml")
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    allRules = true
    ignoredBuildTypes = listOf("release")
  }

  tasks.withType(Detekt::class.java).configureEach {
    jvmTarget = JavaVersion.VERSION_17.toString()
    reports {
      xml.required.set(false)
      html.required.set(true)
      html.outputLocation.set(file("build/reports/detekt.html"))
      txt.required.set(false)
      sarif.required.set(false)
      md.required.set(false)
    }
  }
}
