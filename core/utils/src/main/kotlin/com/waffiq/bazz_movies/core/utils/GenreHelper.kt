package com.waffiq.bazz_movies.core.utils

import android.content.Context
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_action
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_action_and_adventure
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_adventure
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_animation
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_comedy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_crime
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_documentary
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_family
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_history
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_horror
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_kids
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_music
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_mystery
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_news
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_reality
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_romance
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_science_fiction
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_scifi_and_fantasy
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_soap
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_talk
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_thriller
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_tv_movie
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_war
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_war_and_politics
import com.waffiq.bazz_movies.core.designsystem.R.string.genre_western
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.model.media.Genre
import com.waffiq.bazz_movies.core.model.media.GenresItem

/**
 * A utility object for handling movie and TV show genres.
 * It includes functions to convert genre IDs to genre names and vice versa,
 * as well as other transformations related to genre data.
 *
 * All lookups are delegated to [Genre], which is the single source of truth
 * for id <-> name mapping.
 */
object GenreHelper {

  /**
   * Returns the string resource associated with the genre.
   */
  @Suppress("CyclomaticComplexMethod")
  fun Genre.toStringRes(): Int =
    when (this) {
      Genre.ACTION -> genre_action
      Genre.ACTION_AND_ADVENTURE -> genre_action_and_adventure
      Genre.ADVENTURE -> genre_adventure
      Genre.ANIMATION -> genre_animation
      Genre.COMEDY -> genre_comedy
      Genre.CRIME -> genre_crime
      Genre.DOCUMENTARY -> genre_documentary
      Genre.DRAMA -> genre_drama
      Genre.FAMILY -> genre_family
      Genre.FANTASY -> genre_fantasy
      Genre.HISTORY -> genre_history
      Genre.HORROR -> genre_horror
      Genre.KIDS -> genre_kids
      Genre.MUSIC -> genre_music
      Genre.MYSTERY -> genre_mystery
      Genre.NEWS -> genre_news
      Genre.REALITY -> genre_reality
      Genre.ROMANCE -> genre_romance
      Genre.SCI_FI_AND_FANTASY -> genre_scifi_and_fantasy
      Genre.SCIENCE_FICTION -> genre_science_fiction
      Genre.SOAP -> genre_soap
      Genre.TALK -> genre_talk
      Genre.THRILLER -> genre_thriller
      Genre.TV_MOVIE -> genre_tv_movie
      Genre.WAR -> genre_war
      Genre.WAR_AND_POLITICS -> genre_war_and_politics
      Genre.WESTERN -> genre_western
    }

  fun Int.toStringRes(): Int = toGenre()?.toStringRes() ?: not_available

  /**
   * Converts a genre ID into a [Genre].
   *
   * Returns null when the ID is not recognized.
   */
  fun Int.toGenre(): Genre? = Genre.fromId(this)

  /**
   * Converts a list of genre IDs into a comma-separated
   * localized string of genre names.
   *
   * Unknown genre IDs are filtered out.
   */
  fun List<Int>.toGenreString(context: Context): String =
    mapNotNull(Genre::fromId)
      .joinToString(", ") { context.getString(it.toStringRes()) }

  /**
   * Converts a list of genre IDs into a comma-separated
   * localized string of genre names.
   *
   * Returns [not_available] when the list is null,
   * empty, or contains no valid genre IDs.
   */
  fun Context.getGenre(genreIds: List<Int>?): String =
    genreIds
      ?.toGenreString(this)
      ?.takeIf { it.isNotEmpty() }
      ?: getString(not_available)

  /**
   * Converts a list of [GenresItem] into a list of genre IDs.
   *
   * Null items and null IDs are filtered out.
   */
  fun List<GenresItem?>?.toGenreIds(): List<Int> =
    this
      .orEmpty()
      .mapNotNull { it?.id }
}
