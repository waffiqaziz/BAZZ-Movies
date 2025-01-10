import com.waffiq.bazz_movies.configureDetekt
import com.waffiq.bazz_movies.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "io.gitlab.arturbosch.detekt")
      configureDetekt(extensions.getByType<DetektExtension>())
      dependencies { "detektPlugins"(libs.findLibrary("detekt-formatting").get()) }
    }
  }
}
