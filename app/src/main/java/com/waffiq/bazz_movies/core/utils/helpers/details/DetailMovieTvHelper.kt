package com.waffiq.bazz_movies.core.utils.helpers.details

import com.waffiq.bazz_movies.core.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.core.domain.model.detail.Video

/**
 * Used for detail fragment
 */
object DetailMovieTvHelper {
  private fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun detailCrew(crew: List<MovieTvCrewItemResponse>): Pair<MutableList<String>, MutableList<String>> {
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

  fun transformLink(video: Video): String {
    return video.results
      .filter { it.official == true && it.type.equals("Trailer", ignoreCase = true) }
      .map { it.key }.firstOrNull()?.trim()
      ?: video.results.map { it.key }.firstOrNull()?.trim()
      ?: ""
  }

  fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformDuration(runtime: Int?): String? {
    return if (runtime == 0 || runtime == null) null else convertRuntime(runtime)
  }
}
