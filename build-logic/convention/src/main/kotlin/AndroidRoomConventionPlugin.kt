import com.google.devtools.ksp.gradle.KspExtension
import com.waffiq.bazz_movies.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      apply(plugin = "com.google.devtools.ksp")

      extensions.configure<KspExtension> {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.generateKotlin", "true")
      }

      dependencies {
        add("implementation", libs.findLibrary("androidx.room.ktx").get())
        add("implementation", libs.findLibrary("androidx.room.paging").get())
        add("implementation", libs.findLibrary("androidx.paging.runtime.ktx").get())
        add("ksp", libs.findLibrary("androidx.room.compiler").get())
      }
    }
  }
}