import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.waffiq.bazz_movies.configureJacoco
import com.waffiq.bazz_movies.libs
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

      // Set the JaCoCo version to match the one used by the Android Gradle Plugin
      androidExtension.testCoverage {
        jacocoVersion = libs.findVersion("jacoco").get().toString()
      }

      configureJacoco(extensions.getByType<ApplicationAndroidComponentsExtension>())
    }
  }
}
