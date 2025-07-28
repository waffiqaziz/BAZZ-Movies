import com.android.build.gradle.LibraryExtension
import com.waffiq.bazz_movies.configureCommonAndroidSettings
import com.waffiq.bazz_movies.configureMockitoAgent
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "bazzmovies.android.library")
      apply(plugin = "bazzmovies.hilt")
      apply(plugin = "bazzmovies.hilt.test")
      apply(plugin = "bazzmovies.detekt")
      apply(plugin = "bazzmovies.android.library.jacoco")

      extensions.configure<LibraryExtension> {
        configureCommonAndroidSettings(this)
        configureMockitoAgent()
        buildFeatures.viewBinding = true
        defaultConfig {
          consumerProguardFiles("consumer-rules.pro")

          // custom test runner
          testInstrumentationRunner =
            "com.waffiq.bazz_movies.core.instrumentationtest.CustomTestRunner"
        }

        testCoverage {
          jacocoVersion = libs.findVersion("jacoco").get().toString()
        }
      }

      dependencies {
        add("implementation", project(":core:designsystem"))
        add("implementation", project(":navigation"))

        add("testImplementation", kotlin("test"))
        add("testImplementation", project(":core:test"))

        add("androidTestImplementation", kotlin("test"))
        add("androidTestImplementation", project(":core:instrumentationtest"))
      }
    }
  }
}
