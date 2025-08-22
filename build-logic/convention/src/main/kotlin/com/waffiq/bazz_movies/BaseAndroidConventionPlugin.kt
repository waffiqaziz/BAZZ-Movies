package com.waffiq.bazz_movies

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

internal fun Project.configureCommonAndroidSettings(
  extension: CommonExtension<*, *, *, *, *, *>
) {
  group = "com.waffiq.bazzmovies"
  with(extension) {
    packaging {
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

    testOptions {
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

    lint {
      baseline = file("lint-baseline.xml")
      abortOnError = false
      disable += "ResourceName"
    }
  }
}
