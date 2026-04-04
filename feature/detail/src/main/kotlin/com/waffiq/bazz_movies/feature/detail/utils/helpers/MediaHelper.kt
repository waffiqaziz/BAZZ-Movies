package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Context
import android.view.KeyEvent
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_ORIGINAL
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.core.domain.Imageble
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.video.Video

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

  fun Video.toLink(): String {
    val preferred = results
      .filter {
        it.official == true && it.type.equals(
          "Trailer",
          ignoreCase = true,
        )
      } // get official and trailer
      .map { it.key } // get the key value (YouTube id video)
      .firstOrNull()
      ?.trim()

    return preferred ?: results.map { it.key }.firstOrNull()?.trim()
      .orEmpty() // if null use valid link
  }

  fun getTransformTMDBScore(tmdbScore: Double?): String? = tmdbScore?.takeIf { it > 0 }?.toString()

  fun getTransformDuration(runtime: Int?): String? =
    if (runtime == 0 || runtime == null) null else convertRuntime(runtime)

  fun getScoreFromOMDB(score: String?): Boolean = score != null && score != "N/A"

  fun isBackReleased(keyCode: Int, action: Int): Boolean =
    keyCode == KeyEvent.KEYCODE_BACK && action == KeyEvent.ACTION_UP

  fun getListOfKeywords(list: List<MediaKeywordsItem?>?) =
    list?.filter { it?.id != null && !it.name.isNullOrEmpty() }

  private fun String?.isValidImagePath(): Boolean {
    return !this.isNullOrBlank() && this != NOT_AVAILABLE
  }

  private val Imageble.backdropOriginalUrl: String?
    get() = when {
      backdropPath.isValidImagePath() ->
        TMDB_IMG_LINK_BACKDROP_ORIGINAL + backdropPath

      posterPath.isValidImagePath() ->
        TMDB_IMG_LINK_POSTER_W500 + posterPath

      else -> null
    }

  val Imageble.backdropOriginalSource: Any
    get() = backdropOriginalUrl ?: ic_backdrop_error_filled

  val Imageble.isBackdropNotAvailable: Boolean
    get() = backdropPath.isNullOrBlank() || backdropPath == NOT_AVAILABLE

  private val Imageble.posterUrl: String?
    get() = posterPath
      ?.takeIf { it.isNotBlank() && it != NOT_AVAILABLE }
      ?.let { TMDB_IMG_LINK_POSTER_W500 + it } // higher quality than on the list

  /** Used on detail page, to show poster in higher quality
   *
   * @return Poster URL if available, otherwise fallback to drawable
   */
  val Imageble.posterDetailSource: Any
    get() = posterUrl ?: ic_poster_error

  fun Context.getOverview(overview: String?): String =
    overview?.takeIf { it.isNotBlank() } ?: getString(no_overview)
}
