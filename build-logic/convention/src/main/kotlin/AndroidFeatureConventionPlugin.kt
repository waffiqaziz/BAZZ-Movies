import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
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
      }

      dependencies {
        add("implementation", project(":core:designsystem"))

        // add(
        //   "androidTestImplementation",
        //   libs.findLibrary("androidx.lifecycle.runtimeTesting").get()
        // )
      }
    }
  }
}
