import com.android.build.gradle.LibraryExtension
import com.waffiq.bazz_movies.configureKover
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryKoverConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      val androidExtension = extensions.getByType<LibraryExtension>()

      androidExtension.buildTypes.configureEach {
        enableAndroidTestCoverage = true
        enableUnitTestCoverage = true
      }

      configureKover()
    }
  }
}
