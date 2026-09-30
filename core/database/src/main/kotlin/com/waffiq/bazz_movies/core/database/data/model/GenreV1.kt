package com.waffiq.bazz_movies.core.database.data.model

import com.waffiq.bazz_movies.core.database.utils.LegacyGenreMapper

/**
 Represents the genre format used by app versions 1.7.0 and earlier.

 This to kept backward compatibility when reading legacy data  that stores genres by their names.
 The genre names are mapped to their  corresponding IDs in [LegacyGenreMapper] so that the legacy
 data can be converted to the current genre format.
 */
@Suppress("MagicNumber")
enum class GenreV1(val id: Int, val genreName: String) {
  ACTION(28, "Action"),
  ACTION_AND_ADVENTURE(10759, "Action & Adventure"),
  ADVENTURE(12, "Adventure"),
  ANIMATION(16, "Animation"),
  COMEDY(35, "Comedy"),
  CRIME(80, "Crime"),
  DOCUMENTARY(99, "Documentary"),
  DRAMA(18, "Drama"),
  FAMILY(10751, "Family"),
  FANTASY(14, "Fantasy"),
  HISTORY(36, "History"),
  HORROR(27, "Horror"),
  KIDS(10762, "Kids"),
  MUSIC(10402, "Music"),
  MYSTERY(9648, "Mystery"),
  NEWS(10763, "News"),
  REALITY(10764, "Reality"),
  ROMANCE(10749, "Romance"),
  SCI_FI_AND_FANTASY(10765, "Sci-Fi & Fantasy"),
  SCIENCE_FICTION(878, "Science Fiction"),
  SOAP(10766, "Soap"),
  TALK(10767, "Talk"),
  THRILLER(53, "Thriller"),
  TV_MOVIE(10770, "TV Movie"),
  WAR(10752, "War"),
  WAR_AND_POLITICS(10768, "War & Politics"),
  WESTERN(37, "Western"),
  ;

    companion object {
    private val byName = entries.associateBy { it.genreName.lowercase() }
    fun fromName(name: String): GenreV1? = byName[name.trim().lowercase()]
  }
}
