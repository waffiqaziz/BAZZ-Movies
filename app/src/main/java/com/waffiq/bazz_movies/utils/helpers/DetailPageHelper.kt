package com.waffiq.bazz_movies.utils.helpers

import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCrewItemResponse
import com.waffiq.bazz_movies.domain.model.detail.DetailMovie
import com.waffiq.bazz_movies.domain.model.detail.GenresItem
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDateRegion
import com.waffiq.bazz_movies.domain.model.detail.ReleaseDatesItem
import com.waffiq.bazz_movies.domain.model.detail.tv.DetailTv
import com.waffiq.bazz_movies.utils.Helper.dateFormatterISO8601
import com.waffiq.bazz_movies.utils.Helper.dateFormatterStandard

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

  // region CALCULATE AGE RATING MOVIE
  fun getAgeRating(
    data: DetailMovie?,
    userRegion: String,
  ): String {

    // get age rating based on region
    val certification = getTransformAgeRatingMovie(data, userRegion)

    // if certification return empty, get age rating from US as default
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingMovie(data, "false")
  }

  private fun getTransformAgeRatingMovie(data: DetailMovie?, region: String): String {
    return if (region != "false") {
      data?.releaseDates?.listReleaseDatesItem
        ?.find { it?.iso31661 == region }
        ?.let { regionItem ->
          regionItem.listReleaseDatesitemValue
            ?.find { it.certification?.isNotEmpty() == true }
            ?.certification
        } ?: ""

    } else {
      data?.releaseDates?.listReleaseDatesItem
        ?.asSequence() // Convert to sequence for lazy evaluation
        ?.flatMap {
          it?.listReleaseDatesitemValue?.asSequence() ?: emptySequence()
        } // Flatten the list of release dates
        ?.find { it.certification?.isNotEmpty() == true } // Find the first non-null and non-empty certification
        ?.certification ?: ""
    }
  }

  // endregion CALCULATE AGE RATING MOVIE

  // region CALCULATE AGE RATING TV
  fun getAgeRating(
    data: DetailTv?,
    userRegion: String,
  ): String {

    // get age rating based on region
    val certification = getTransformAgeRatingTv(data, userRegion)

    // if certification return empty, get age rating from others
    return certification.takeIf { it.isNotEmpty() } ?: getTransformAgeRatingTv(data, "false")
  }

  private fun getTransformAgeRatingTv(data: DetailTv?, region: String): String =
    if (region != "false") {
      data?.contentRatings?.contentRatingsItem
        ?.filter { it?.iso31661 == "US" || it?.iso31661 == region } // Filter by US or specific region
        ?.mapNotNull { contentRatingsItem -> // Map to ratings and exclude empty/null values
          contentRatingsItem?.rating?.takeIf { it.isNotEmpty() }
        }
        ?.joinToString(separator = ", ") ?: ""
    } else {
      data?.contentRatings?.contentRatingsItem
        ?.mapNotNull { contentRatingsItem ->  // Map to ratings and exclude empty/null values
          contentRatingsItem?.rating?.takeIf { it.isNotEmpty() }
        }
        ?.joinToString(separator = ", ") ?: ""
    }
  // endregion CALCULATE AGE RATING TV

  // region GET REGION AND RELEASE DATE
  fun getReleaseDateRegion(data: DetailMovie?, userRegion: String): ReleaseDateRegion {
    // Step 1: Check if user region matches
    val userRegionAndDate =
      getMatchingRegionAndReleaseDate(data?.releaseDates?.listReleaseDatesItem, userRegion)

    if (userRegionAndDate != null) {
      return ReleaseDateRegion(
        regionRelease = userRegionAndDate.first,
        releaseDate = dateFormatterISO8601(userRegionAndDate.second)
      )
    }

    // Step 2: Fallback to production country and its release date
    val productionCountryRegionAndDate = ReleaseDateRegion(
      regionRelease = data?.listProductionCountriesItem?.firstOrNull { !it?.iso31661.isNullOrEmpty() }?.iso31661
        ?: "",
      releaseDate = dateFormatterStandard(data?.releaseDate)
    )

    if (productionCountryRegionAndDate.releaseDate.isNotEmpty() && productionCountryRegionAndDate.regionRelease.isNotEmpty()) {
      return productionCountryRegionAndDate
    }

    // Step 3: Fallback to any valid region and release date
    val fallback = getAnyValidRegionAndReleaseDate(data?.releaseDates?.listReleaseDatesItem)
    return ReleaseDateRegion(
      regionRelease = fallback.first,
      releaseDate = dateFormatterISO8601(fallback.second)
    )
  }

  private fun getMatchingRegionAndReleaseDate(
    data: List<ReleaseDatesItem?>?,
    region: String
  ): Pair<String, String>? {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it, region) }
      ?.let {
        Pair(
          it.iso31661 ?: "",
          it.listReleaseDatesitemValue?.firstOrNull()?.releaseDate ?: ""
        )
      }
  }

  private fun getAnyValidRegionAndReleaseDate(data: List<ReleaseDatesItem?>?): Pair<String, String> {
    return data?.firstOrNull { isValidRegionAndReleaseDate(it) }
      ?.let {
        Pair(
          it.iso31661 ?: "",
          it.listReleaseDatesitemValue?.firstOrNull()?.releaseDate ?: ""
        )
      }
      ?: Pair("", "")
  }

  private fun isValidRegionAndReleaseDate(
    item: ReleaseDatesItem?,
    region: String? = null
  ): Boolean {
    return item?.iso31661 != null &&
      (region == null || item.iso31661 == region) &&
      item.listReleaseDatesitemValue?.any { !it.releaseDate.isNullOrEmpty() } == true
  }
  // endregion GET REGION AND RELEASE DATE

  fun getTransformGenreName(list: List<GenresItem>?): String? =
    list?.map { it.name }?.joinToString(separator = ", ")

  fun getTransformTMDBScore(tmdbScore: Double?): String? =
    tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformGenreIDs(list: List<GenresItem>?): List<Int>? =
    list?.map { it.id ?: 0 }

  fun getTransformDuration(runtime: Int?): String? {
    return if (runtime == 0 || runtime == null) null else convertRuntime(runtime)
  }
}