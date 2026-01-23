import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.waffiq.bazz_movies.configureJacoco
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.testing.jacoco.plugins.JacocoPlugin

class AndroidApplicationJacocoConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      apply<JacocoPlugin>()
      val androidExtension = extensions.getByType<ApplicationExtension>()

      androidExtension.buildTypes.configureEach {
        enableAndroidTestCoverage = true
        enableUnitTestCoverage = true
      }

      // Set the JaCoCo version to match the one used by the Android Gradle Plugin
      androidExtension.testCoverage {
        jacocoVersion = libs.findVersion("jacoco").get().toString()
      }
      configureJacoco(
        commonExtension = extensions.getByType<ApplicationExtension>(),
        androidComponentsExtension = extensions.getByType<ApplicationAndroidComponentsExtension>(),
      )
    }
  }
}
