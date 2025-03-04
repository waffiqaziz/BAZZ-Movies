import com.waffiq.bazz_movies.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class JvmLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "org.jetbrains.kotlin.jvm")
      configureKotlinJvm()
      dependencies {
        add("testImplementation", kotlin("test"))
      }
    }
  }
}
