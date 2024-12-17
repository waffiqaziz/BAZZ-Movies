import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("bazzmovies.android.library")
        apply("bazzmovies.hilt")
      }
      extensions.configure<LibraryExtension> {
        testOptions.animationsDisabled = true
        defaultConfig.consumerProguardFiles("consumer-rules.pro")
        buildFeatures.viewBinding = true
        buildTypes {
          getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
          }

          if (!buildTypes.names.contains("staging")) {
            create("staging") {
              isMinifyEnabled = true
              proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
              )
            }
          }

          getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
              getDefaultProguardFile("proguard-android-optimize.txt"),
              "proguard-rules.pro"
            )
          }
        }

        packaging {
          resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("META-INF/LICENSE-notice.md")
          }
        }

        @Suppress("UnstableApiUsage")
        testOptions {
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
      }

      dependencies {
        add("implementation", project(":core:designsystem"))
      }
    }
  }
}
