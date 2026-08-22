package com.waffiq.bazz_movies.feature.detail.utils.helpers

import android.content.Context
import android.view.KeyEvent
import com.waffiq.bazz_movies.core.designsystem.R.plurals
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.feature.detail.domain.model.keywords.MediaKeywordsItem
import com.waffiq.bazz_movies.feature.detail.domain.model.video.Videos
import kotlin.math.roundToInt

/**
 * Used for detail fragment
 */
object MediaHelper {
  private const val SIXTY = 60
  private const val DIGIT_NUMBER_INTEGER = 10
  private const val DIGIT_NUMBER_DOUBLE = 10.0

  private fun convertRuntime(t: Int): String {
    val hours: Int = t / SIXTY
    val minutes: Int = t % SIXTY
    return "${hours}h ${minutes}m"
  }

  fun Videos.toLink(): String {
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

  fun Context.getOverview(overview: String?): String =
    overview?.takeIf { it.isNotBlank() } ?: getString(no_overview)

  fun formatRating(rating: Number): String =
    ((rating.toFloat() * DIGIT_NUMBER_INTEGER).roundToInt() / DIGIT_NUMBER_DOUBLE).toString()

  fun Context.getEpisodesFormatted(episodes: Int?, seasons: Int?): String {
    if (episodes == null || seasons == null) return "-"

    val episodesText = resources.getQuantityString(plurals.episodes, episodes, episodes)
    val seasonsText = resources.getQuantityString(plurals.seasons, seasons, seasons)
    return "$episodesText ($seasonsText)"
  }

  fun showDuration(duration: String?, status: String?) =
    !duration.isNullOrEmpty() && !status.isNullOrEmpty()
}
