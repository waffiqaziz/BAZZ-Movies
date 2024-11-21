import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class GlideConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("com.google.devtools.ksp")

      dependencies {
        add("ksp", libs.findLibrary("glide-compiler").get())
        add("implementation", libs.findLibrary("glide").get())
      }
    }
  }
}
