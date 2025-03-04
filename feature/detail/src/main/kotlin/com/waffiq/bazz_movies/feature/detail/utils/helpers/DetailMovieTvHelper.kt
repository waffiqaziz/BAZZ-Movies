package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.MovieTvCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video

/**
 * Used for detail fragment
 */
object DetailMovieTvHelper {
  private fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun detailCrew(crew: List<MovieTvCrewItem>): Pair<MutableList<String>, MutableList<String>> {
    // key-value pair
    val jobToNamesMap = mapOf(
      "Director" to "Director",
      "Story" to "Story",
      "Characters" to "Characters",
      "Executive Producer" to "Creator",
      "Writer" to "Writer",
      "Author" to "Author",
      "Screenplay" to "Screenplay",
      "Novel" to "Novel"
    )

    val job: MutableList<String> = ArrayList()
    val name: MutableList<String> = ArrayList()

    jobToNamesMap.forEach { (jobTitle, displayName) ->
      val filteredNames =
        // Filters the crew list to find members whose job matches the jobTitle in the map.
        crew.filter { it.job == jobTitle }
          // Maps the filtered crew members to their name field.
          .map { it.name }.joinToString()

      // If the list of names isn't empty, the displayName (from the map) is added to the job list,
      // and the corresponding names are added to the name list.
      if (filteredNames.isNotEmpty()) {
        job.add(displayName)
        name.add(filteredNames)
      }
    }

    return job to name
  }

  fun Video.toLink(): String {
    return this.results
      .filter { it.official == true && it.type.equals("Trailer", ignoreCase = true) }
      .map { it.key }.firstOrNull()?.trim()
      ?: this.results.map { it.key }.firstOrNull()?.trim()
      ?: ""
  }

  fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformDuration(runtime: Int?): String? {
    return if (runtime == 0 || runtime == null) null else convertRuntime(runtime)
  }
}
