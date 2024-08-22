package com.waffiq.bazz_movies.utils.helpers

import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.GenresItem
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDateRegion
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItem
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.utils.Helper

object DetailPageHelper {
  private fun convertRuntime(t: Int): String {
    val hours: Int = t / 60
    val minutes: Int = t % 60
    return "${hours}h ${minutes}m"
  }

  fun detailCrew(crew: List<MovieTvCrewItemResponse>): Pair<MutableList<String>, MutableList<String>> {
    val job: MutableList<String> = ArrayList()
    val name: MutableList<String> = ArrayList()

    val director = crew.map { it }.filter {
      it.job == "Director"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val story = crew.map { it }.filter {
      it.job == "Story"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val writer = crew.map { it }.filter {
      it.job == "Writer"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val characters = crew.map { it }.filter {
      it.job == "Characters"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val creator = crew.map { it }.filter {
      it.job == "Executive Producer"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val author = crew.map { it }.filter {
      it.job == "Author"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val screenplay = crew.map { it }.filter {
      it.job == "Screenplay"
    }.map { it.name }.toString().dropLast(1).substring(1)

    val novel = crew.map { it }.filter {
      it.job == "Novel"
    }.map { it.name }.toString().dropLast(1).substring(1)

    if (director.isNotEmpty()) {
      job.add("Director")
      name.add(director)
    }
    if (story.isNotEmpty()) {
      job.add("Story")
      name.add(story)
    }
    if (creator.isNotEmpty()) {
      job.add("Creator")
      name.add(creator)
    }
    if (characters.isNotEmpty()) {
      job.add("Characters")
      name.add(characters)
    }
    if (writer.isNotEmpty()) {
      job.add("Writer")
      name.add(writer)
    }
    if (author.isNotEmpty()) {
      job.add("Author")
      name.add(author)
    }
    if (screenplay.isNotEmpty()) {
      job.add("Screenplay")
      name.add(screenplay)
    }
    if (novel.isNotEmpty()) {
      job.add("Novel")
      name.add(novel)
    }

    return job to name
  }

  fun getAgeRating(
    data: DetailMovie?,
    userRegion: String,
  ): String {

    // get age rating based on region
    val certification = getTransformAgeRatingMovie(data, userRegion)

    // if certification return empty, get age rating from US as default
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingMovie(data, "US")
  }

  private fun getTransformAgeRatingMovie(data: DetailMovie?, region: String): String {
    return data?.releaseDates?.listReleaseDatesItem
      ?.find { it?.iso31661 == region }
      ?.listReleaseDatesitemValue
      ?.find { it.certification?.isNotEmpty() == true }
      ?.certification ?: ""
  }

  fun getAgeRating(
    data: DetailTv?,
    userRegion: String,
  ): String {

    // get age rating based on region
    val certification = getTransformAgeRatingTv(data, userRegion)

    // if certification return empty, get age rating from US as default
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingTv(data, "US")
  }

  private fun getTransformAgeRatingTv(data: DetailTv?, region: String): String {
    return data?.contentRatingsResponse?.contentRatingsItemResponse?.filter {
      it?.iso31661 == "US" || it?.iso31661 == region
    }?.map { it?.rating }.toString().replace("[", "").replace("]", "")
      .replace(" ", "").replace(",", ", ")
  }

  fun getReleaseDateRegion(data: DetailMovie?, userRegion: String): ReleaseDateRegion {
    /* match between release date and region
       Step 1. Check if release date and region is matching
       Step 2. If matching return as should be, if not get release date based region US
    */
    val isMatch = matchRegionAndReleaseDate(data, userRegion)

    return if (isMatch) {
      ReleaseDateRegion(
        regionRelease = userRegion,
        releaseDate = Helper.dateFormatterISO8601(
          getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion)
        )
      )
    } else {
      ReleaseDateRegion(
        regionRelease = "US",
        releaseDate = Helper.dateFormatterISO8601(
          getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, "US")
        )
      )
    }
  }

  private fun matchRegionAndReleaseDate(data: DetailMovie?, userRegion: String): Boolean {
    val releaseDate =
      getTransformReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion).isNotEmpty()
    val region =
      getTransformRegion(data?.releaseDates?.listReleaseDatesItem, userRegion).isNotEmpty()

    return releaseDate && region
  }

  private fun getTransformReleaseDate(data: List<ReleaseDatesItem?>?, region: String): String =
    data?.find { it?.iso31661 == region }
      ?.listReleaseDatesitemValue
      ?.firstOrNull()
      ?.releaseDate
      ?: ""

  private fun getTransformRegion(data: List<ReleaseDatesItem?>?, region: String): String =
    data?.find { it?.iso31661 == region }?.iso31661 ?: ""

  fun getTransformGenreName(list: List<GenresItem>?): String? =
    list?.map { it.name }?.joinToString(separator = ", ")

  fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformGenreIDs(list: List<GenresItem>?): List<Int>? =
    list?.map { it.id ?: 0 }

  fun getTransformDuration(runtime: Int?): String? =
    runtime?.let { convertRuntime(it) }
}