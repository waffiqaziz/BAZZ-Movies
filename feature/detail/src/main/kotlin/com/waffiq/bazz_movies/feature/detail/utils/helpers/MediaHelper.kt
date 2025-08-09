package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.Video

/**
 * Used for detail fragment
 */
object MediaHelper {
  private const val SIXTY = 60

  private fun convertRuntime(t: Int): String {
    val hours: Int = t / SIXTY
    val minutes: Int = t % SIXTY
    return "${hours}h ${minutes}m"
  }

  fun extractCrewDisplayNames(crew: List<MediaCrewItem>): Pair<List<String>, List<String>> {
    // Map of job titles to their display names
    // Key: actual job title in data, Value: user-friendly display name
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

    // Group crew members by their job title for efficient lookup
    // This avoids filtering the entire crew list multiple times
    val crewByJob = crew.groupBy { it.job }

    // Process each job title and create pairs of (displayName, joinedNames)
    return jobToNamesMap.mapNotNull { (jobTitle, displayName) ->
      crewByJob[jobTitle] // Get crew members for this job title
        ?.takeIf { it.isNotEmpty() } // Only proceed if we have crew members
        ?.let { members ->
          // Create pair: display name -> comma-separated crew names
          displayName to members.joinToString { it.name.orEmpty() }
        }
    }.unzip() // Split pairs into two separate lists: [displayNames], [joinedNames]
  }

  fun Video.toLink(): String {
    return this.results
      .filter { it.official == true && it.type.equals("Trailer", ignoreCase = true) }
      .map { it.key }.firstOrNull()?.trim()
      ?: this.results.map { it.key }.firstOrNull()?.trim().orEmpty()
  }

  fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformDuration(runtime: Int?): String? =
    if (runtime == 0 || runtime == null) null else convertRuntime(runtime)
}
