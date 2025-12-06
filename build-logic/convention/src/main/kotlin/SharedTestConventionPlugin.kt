
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class SharedTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      extensions.configure<LibraryExtension> {

        testOptions {
          unitTests.apply {
            isIncludeAndroidResources = false
          }
        }
      }

      // prevent accidental production inclusion
      // Gradle won’t allow anyone to depend on it with implementation.
      configurations.configureEach {
        if (name == "implementation" || name == "api") {
          isCanBeConsumed = false
          isCanBeResolved = true
        }
      }
    }
  }
}
