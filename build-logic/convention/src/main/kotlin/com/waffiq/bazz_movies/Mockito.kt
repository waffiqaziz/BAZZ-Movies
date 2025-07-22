package com.waffiq.bazz_movies

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

// configure Mockito agent
// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
internal fun Project.configureMockitoAgent() {
  configurations.maybeCreate("mockitoAgent")

  // add Mockito dependency to the agent configuration
  dependencies {
    "mockitoAgent"(libs.findLibrary("mockito-core").get()) {
      isTransitive = false
    }
  }

  // configure test tasks to use Mockito as Java agent
  tasks.withType<Test>().configureEach {
    jvmArgs("-javaagent:${configurations.getByName("mockitoAgent").asPath}")
  }
}
