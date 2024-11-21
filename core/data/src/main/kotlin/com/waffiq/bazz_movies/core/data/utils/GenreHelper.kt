package com.waffiq.bazz_movies.core.data.utils

import com.waffiq.bazz_movies.core.data.GenresItem

/**
 * Used to retrieve genre names and codes.
 */
object GenreHelper {
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
    10768 to "War & Politics"
  )

  private fun getGenreName(int: Int): String {
    return genreNameMap[int] ?: ""
  }

  fun transformListGenreIdsToJoinName(data: List<Int>): String {
    return data
      .map { getGenreName(it) }
      .filter { it.isNotEmpty() }
      .joinToString(", ")
  }

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
    "War & Politics" to 10768
  )

  private fun getGenreCode(genreName: String): Int {
    return genreCodeMap[genreName] ?: 0
  }

  @Suppress("UNUSED")
  fun transformToGenreCode(data: List<String>): String {
    var temp = ""

    /**
     *  "," Comma's are treated like an AND and query while "|"Pipe's are an OR.
     *  https://www.themoviedb.org/talk/635968b34a4bf6007c5997f3
     *
     *  used to get id genre
     */
    data.forEach { temp = temp + getGenreCode(it) + "|" } // using OR
    temp = temp.dropLast(2)
    return temp
  }

  fun transformListGenreToJoinString(list: List<GenresItem>?): String? =
    list?.map { it.name }?.joinToString(separator = ", ")

  fun transformToGenreIDs(list: List<GenresItem>?): List<Int>? =
    list?.map { it.id ?: 0 }
}
