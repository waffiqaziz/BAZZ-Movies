package com.waffiq.bazz_movies.utils.helpers.details

import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.domain.model.detail.Video

object DetailMovieTvHelper {
  private fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun detailCrew(crew: List<MovieTvCrewItemResponse>): Pair<MutableList<String>, MutableList<String>> {
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
      val filteredNames = crew.filter { it.job == jobTitle }.map { it.name }.joinToString()
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
