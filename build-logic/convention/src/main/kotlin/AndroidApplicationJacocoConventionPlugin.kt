import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.waffiq.bazz_movies.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationJacocoConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "jacoco")
      val androidExtension = extensions.getByType<BaseAppModuleExtension>()

      androidExtension.buildTypes.configureEach {
        enableAndroidTestCoverage = true
        enableUnitTestCoverage = true
      }

      configureJacoco(extensions.getByType<ApplicationAndroidComponentsExtension>())
    }
  }
}
