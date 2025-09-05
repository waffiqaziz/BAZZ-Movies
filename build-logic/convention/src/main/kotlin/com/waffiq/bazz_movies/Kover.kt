package com.waffiq.bazz_movies

import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

private val koverExclusions = listOf(
  "*.R",
  "*.R$*",
  "*.BuildConfig",
  "**/databinding/**",

  // Hilt
  "*.di.*",
  "dagger.hilt.**",
  "hilt_aggregated_deps.*",
  "com.waffiq.bazz_movies.*.*_Factory",
  "com.waffiq.bazz_movies.*.*_MembersInjector",
  "*_HiltModules*",
  "*HiltModules*",

  // Room
  "com.waffiq.bazz_movies.*.*_Impl*",

  // BuildConfig
  "com.waffiq.bazz_movies.BuildConfig",

  // Moshi - Json Adapter
  "com.waffiq.bazz_movies.*.*JsonAdapter",
)

private const val THRESHOLD = 55

/**
 * Configures Kover code coverage
 */
internal fun Project.configureKover() {
  pluginManager.apply("org.jetbrains.kotlinx.kover")

  configure<KoverProjectExtension> {
    reports {
      filters {
        excludes {
          androidGeneratedClasses()

          // Exclude classes
          classes(koverExclusions)
          println("Kover exclusions applied: ${koverExclusions.joinToString()}")
        }
      }

      verify {
        rule("Coverage Line") {
          bound {
            minValue.set(THRESHOLD)
            coverageUnits.set(CoverageUnit.LINE)
            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
          }
        }
        rule("Coverage Branch") {
          bound {
            minValue.set(THRESHOLD)
            coverageUnits.set(CoverageUnit.BRANCH)
            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
          }
        }
        rule("Coverage Instruction") {
          bound {
            minValue.set(THRESHOLD)
            coverageUnits.set(CoverageUnit.INSTRUCTION)
            aggregationForGroup.set(AggregationType.COVERED_PERCENTAGE)
          }
        }
      }

      total {
        html() {
          onCheck.set(true)
          title.set("Code Coverage Report - ${project.path.removePrefix(":")}")
          htmlDir.set(layout.buildDirectory.dir("reports/kover/html"))
        }
        xml {
          onCheck.set(true)
          title.set("Code Coverage Report - ${project.path.removePrefix(":")}")
          xmlFile.set(layout.buildDirectory.file("reports/kover/report.xml"))
        }
      }
    }
//
//    tasks.withType<KoverVerifyTask>().configureEach {
//      doLast {
//        println("---- Kover Coverage Verify Report ----")
//        results.forEach { result ->
//          println("Rule: ${result.name}")
//          result.bounds.forEach { bound ->
//            println(
//              "  ${bound.metric} ${bound.unit}: ${bound.value} " +
//                "(min=${bound.minValue}, max=${bound.maxValue})"
//            )
//          }
//        }
//        println("--------------------------------------")
//      }
//    }
  }
}
