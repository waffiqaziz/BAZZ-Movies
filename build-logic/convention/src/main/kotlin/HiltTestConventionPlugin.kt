import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltTestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "dagger.hilt.android.plugin")
      apply(plugin = "com.google.devtools.ksp")

      dependencies {
        add("androidTestImplementation", libs.findLibrary("hilt-test").get())
        add("kspAndroidTest", libs.findLibrary("hilt-android-compiler").get())
      }
    }
  }
}
