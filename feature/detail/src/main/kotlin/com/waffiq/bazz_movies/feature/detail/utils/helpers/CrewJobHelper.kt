package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem

object CrewJobHelper {

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
      "Novel" to "Novel",
    )

    // Group crew members by their job title for efficient lookup
    // This avoids filtering the entire crew list multiple times
    val crewByJob = crew.groupBy { it.job }

    // Process each job title and create pairs of (displayName, joinedNames)
    return jobToNamesMap.mapNotNull { (jobTitle, displayName) ->
      val members = crewByJob[jobTitle].orEmpty().filter { !it.name.isNullOrEmpty() }
      if (members.isNotEmpty()) displayName to members.joinToString { it.name.toString() } else null
    }.unzip() // Split pairs into two separate lists: [displayNames], [joinedNames]
  }
}
