package com.waffiq.bazz_movies.core.utils

import com.waffiq.bazz_movies.core.domain.GenresItem

/**
 * A utility object for handling movie and TV show genres.
 * It includes functions to convert genre IDs to genre names and vice versa,
 * as well as other transformations related to genre data.
 */
@Suppress("MagicNumber")
object GenreHelper {

  // a map that associates genre IDs with genre names for movies and TV shows.
  private val genreNameMap = mapOf(
    // movie genres
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    99 to "Documentary",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    10770 to "TV Movie",
    53 to "Thriller",
    10752 to "War",
    37 to "Western",

    // tv genres
    10759 to "Action & Adventure",
    10762 to "Kids",
    10763 to "News",
    10764 to "Reality",
    10765 to "Sci-Fi & Fantasy",
    10766 to "Soap",
    10767 to "Talk",
    10768 to "War & Politics",
  )

  // Private function to retrieve the genre name for a given genre ID.
  // If no matching genre is found, it returns an empty string.
  fun getGenreName(int: Int): String = genreNameMap[int].orEmpty()

  /**
   * Transforms a list of genre IDs into a comma-separated string of genre names.
   * It filters out empty genre names (for invalid genre IDs).
   *
   * @param listGenreIds A list of genre IDs to be transformed.
   * @return A string of genre names separated by commas.
   */
  fun transformListGenreIdsToJoinName(listGenreIds: List<Int>): String =
    listGenreIds
      .map { getGenreName(it) }
      .filter { it.isNotEmpty() }
      .joinToString(", ")

  // a map that associates genre names with their respective genre IDs for movies and TV shows.
  private val genreCodeMap = mapOf(
    // movie genres
    "Action" to 28,
    "Adventure" to 12,
    "Animation" to 16,
    "Comedy" to 35,
    "Crime" to 80,
    "Documentary" to 99,
    "Drama" to 18,
    "Family" to 10751,
    "Fantasy" to 14,
    "History" to 36,
    "Horror" to 27,
    "Music" to 10402,
    "Mystery" to 9648,
    "Romance" to 10749,
    "Science Fiction" to 878,
    "TV Movie" to 10770,
    "Thriller" to 53,
    "War" to 10752,
    "Western" to 37,

    // tv genres
    "Action & Adventure" to 10759,
    "Kids" to 10762,
    "News" to 10763,
    "Reality" to 10764,
    "Sci-Fi & Fantasy" to 10765,
    "Soap" to 10766,
    "Talk" to 10767,
    "War & Politics" to 10768,
  )

  // Private function to retrieve the genre ID for a given genre name.
  // If no matching genre is found, it returns 0.
  private fun getGenreCode(genreName: String): Int = genreCodeMap[genreName] ?: 0

  /**
   * Transforms a list of genre names into a string of genre IDs.
   * The genre IDs are joined by a pipe ("|") for use in queries (OR condition).
   *
   * A comma (",") is treated as an AND operator, and pipes ("|") represent OR in the context of
   * genre queries.
   *
   * @param listGenre A list of genre names to be transformed into genre IDs.
   * @return A string of genre IDs joined by a pipe ("|"), or an empty string if no valid genre
   *         codes are found.
   */
  fun transformToGenreCode(listGenre: List<String>): String {
    var temp = ""

    /*
     *  "," Comma's are treated like an AND and query while "|" Pipe's are an OR.
     *  https://www.themoviedb.org/talk/635968b34a4bf6007c5997f3
     *
     *  used to get id genre
     */
    listGenre.forEach { temp = temp + getGenreCode(it) + "|" } // using OR
    temp = temp.dropLast(1)
    return if (temp == "0") "" else temp
  }

  /**
   * Transforms a list of `GenresItem` objects into a comma-separated string of genre names.
   *
   * @param listGenresItem A list of `GenresItem` objects, each containing a genre name.
   * @return A comma-separated string of genre names, or null if the list is null.
   */
  fun transformListGenreToJoinString(listGenresItem: List<GenresItem?>?): String? {
    if (listGenresItem.isNullOrEmpty()) return null
    val names = listGenresItem.mapNotNull { it?.name }
    return if (names.isEmpty()) null else names.joinToString(", ")
  }

  /**
   * Transforms a list of `GenresItem` objects into a list of genre IDs.
   *
   * @param listGenresItem A list of `GenresItem` objects, each containing a genre ID.
   * @return A list of genre IDs, or null if the list is null.
   */
  fun transformToGenreIDs(listGenresItem: List<GenresItem?>?): List<Int>? =
    listGenresItem?.map { it?.id ?: 0 }
}
