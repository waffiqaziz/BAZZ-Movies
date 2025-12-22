import com.android.build.gradle.api.AndroidBasePlugin
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "dagger.hilt.android.plugin")
      apply(plugin = "com.google.devtools.ksp")

      dependencies {
        add("ksp", libs.findLibrary("hilt-android-compiler").get())
        add("ksp", libs.findLibrary("kotlin-metadata-jvm").get())
        add("implementation", libs.findLibrary("hilt-android").get())
      }

      /** Add support for Android modules, based on [AndroidBasePlugin] */
      pluginManager.withPlugin("com.android.base") {
        pluginManager.apply("dagger.hilt.android.plugin")
        dependencies {
          add("implementation", libs.findLibrary("hilt.android").get())
        }
      }
    }
  }
}
