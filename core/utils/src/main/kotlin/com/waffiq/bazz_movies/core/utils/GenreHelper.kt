package com.waffiq.bazz_movies.core.utils

import android.content.Context
import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.models.GenresItem

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
   * Retrieves the genre name for a given genre ID.
   * Returns an empty string if no matching genre is found.
   */
  fun getGenreName(id: Int): String = Genre.fromId(id)?.genreName.orEmpty()

  /**
   * Transforms a list of genre IDs into a comma-separated string of genre names.
   * Filters out unknown genre IDs.
   *
   * @param listGenreIds A list of genre IDs to be transformed.
   * @return A string of genre names separated by commas, or null if none matched.
   */
  fun transformListGenreIdsToJoinName(listGenreIds: List<Int>): String? =
    listGenreIds
      .mapNotNull { Genre.fromId(it)?.genreName }
      .takeIf { it.isNotEmpty() }
      ?.joinToString(", ")

  /**
   * Transforms a string of genre into list of int (genre id).
   */
  fun String.toListGenreIds(): List<Int> =
    this.split(",").mapNotNull { name ->
      Genre.entries.find { it.genreName == name.trim() }?.id
    }

  /**
   * Transforms a list of genre IDs into genre names, or a fallback "not available" string.
   */
  fun Context.getGenre(data: List<Int>?): String =
    data
      ?.let { transformListGenreIdsToJoinName(it) }
      ?: getString(not_available)

  /**
   * Transforms a list of genre names into a string of genre IDs joined by "|" (OR),
   * for use in TMDB discover queries.
   *
   * Per TMDB's query syntax, commas ("," ) are AND pipes ("|") are OR:
   * https://www.themoviedb.org/talk/635968b34a4bf6007c5997f3
   *
   * @param listGenre A list of genre names to be transformed into genre IDs.
   * @return A string of genre IDs joined by "|", or an empty string if none matched.
   */
  fun transformToGenreCode(listGenre: List<String>): String =
    listGenre
      .mapNotNull { Genre.fromName(it)?.id }
      .joinToString("|")

  /**
   * Transforms a list of `GenresItem` objects into a comma-separated string of genre names.
   *
   * @param listGenresItem A list of `GenresItem` objects, each containing a genre name.
   * @return A comma-separated string of genre names, or null if the list is null/empty.
   */
  fun transformListGenreToJoinString(listGenresItem: List<GenresItem?>?): String? =
    listGenresItem
      ?.mapNotNull { it?.name }
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString(", ")

  /**
   * Transforms a list of `GenresItem` objects into a list of genre IDs.
   *
   * @param listGenresItem A list of `GenresItem` objects, each containing a genre ID.
   * @return A list of genre IDs, or null if the list is null.
   */
  fun transformToGenreIDs(listGenresItem: List<GenresItem?>?): List<Int>? =
    listGenresItem?.map { it?.id ?: 0 }
}
