import com.android.build.api.dsl.LibraryExtension
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KotestConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      extensions.configure<LibraryExtension> {

        // required for Kotest run with JUnit5
        // https://kotest.io/docs/5.9.x/quickstart
        testOptions {
          unitTests.all {
            it.useJUnitPlatform()
          }
        }
      }

      dependencies {
        add("testImplementation", libs.findBundle("kotest").get())
      }
    }
  }
}
