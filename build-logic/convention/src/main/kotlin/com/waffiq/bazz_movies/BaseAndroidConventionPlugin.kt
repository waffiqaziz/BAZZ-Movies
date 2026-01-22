package com.waffiq.bazz_movies

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import com.android.build.api.dsl.Packaging
import com.android.build.api.dsl.TestExtension
import com.android.build.api.dsl.TestOptions
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

internal fun Project.configureCommonAndroidSettings(
  extension: CommonExtension,
) {
  group = "com.waffiq.bazzmovies"

  when (extension) {
    is ApplicationExtension -> extension.applyCommonSettings(this)
    is LibraryExtension -> extension.applyCommonSettings(this)
    is DynamicFeatureExtension -> extension.applyCommonSettings(this)
    is TestExtension -> extension.applyCommonSettings(this)
  }
}

private fun ApplicationExtension.applyCommonSettings(project: Project) {
  packaging.configurePackaging()
  testOptions.configureTestOptions()
  lint.configureLint(project)
}

private fun LibraryExtension.applyCommonSettings(project: Project) {
  packaging.configurePackaging()
  testOptions.configureTestOptions()
  lint.configureLint(project)
}

private fun DynamicFeatureExtension.applyCommonSettings(project: Project) {
  packaging.configurePackaging()
  testOptions.configureTestOptions()
  lint.configureLint(project)
}

private fun TestExtension.applyCommonSettings(project: Project) {
  packaging.configurePackaging()
  testOptions.configureTestOptions()
  lint.configureLint(project)
}

private fun Packaging.configurePackaging() {
  resources {
    excludes.addAll(
      listOf(
        "META-INF/LICENSE.md",
        "META-INF/LICENSE.txt",
        "META-INF/NOTICE.md",
        "META-INF/NOTICE.txt",
        "META-INF/LICENSE-notice.md"
      )
    )
  }
}

private fun TestOptions.configureTestOptions() {
  animationsDisabled = true
  unitTests.apply {
    isReturnDefaultValues = true
    isIncludeAndroidResources = true
  }
  unitTests.all {
    it.testLogging {
      events("passed", "skipped", "failed")
      showExceptions = true
      showCauses = true
      showStackTraces = true
      exceptionFormat = TestExceptionFormat.FULL
    }
  }
}

private fun Lint.configureLint(project: Project) {
  baseline = project.file("lint-baseline.xml")
  abortOnError = false
  disable += "ResourceName"
}
