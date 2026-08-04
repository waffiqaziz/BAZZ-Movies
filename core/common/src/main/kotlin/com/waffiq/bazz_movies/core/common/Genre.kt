package com.waffiq.bazz_movies.core.common

@Suppress("MagicNumber")
enum class Genre(
  val id: Int,
  val genreName: String,
  val mediaTypes: Set<MediaType>,
) {
  ACTION(28, "Action", setOf(MediaType.MOVIE)),
  ACTION_AND_ADVENTURE(10759, "Action & Adventure", setOf(MediaType.TV)),
  ADVENTURE(12, "Adventure", setOf(MediaType.MOVIE)),
  ANIMATION(16, "Animation", setOf(MediaType.MOVIE, MediaType.TV)),
  COMEDY(35, "Comedy", setOf(MediaType.MOVIE, MediaType.TV)),
  CRIME(80, "Crime", setOf(MediaType.MOVIE, MediaType.TV)),
  DOCUMENTARY(99, "Documentary", setOf(MediaType.MOVIE, MediaType.TV)),
  DRAMA(18, "Drama", setOf(MediaType.MOVIE, MediaType.TV)),
  FAMILY(10751, "Family", setOf(MediaType.MOVIE, MediaType.TV)),
  FANTASY(14, "Fantasy", setOf(MediaType.MOVIE)),
  HISTORY(36, "History", setOf(MediaType.MOVIE)),
  HORROR(27, "Horror", setOf(MediaType.MOVIE)),
  KIDS(10762, "Kids", setOf(MediaType.TV)),
  MUSIC(10402, "Music", setOf(MediaType.MOVIE)),
  MYSTERY(9648, "Mystery", setOf(MediaType.MOVIE, MediaType.TV)),
  NEWS(10763, "News", setOf(MediaType.TV)),
  REALITY(10764, "Reality", setOf(MediaType.TV)),
  ROMANCE(10749, "Romance", setOf(MediaType.MOVIE)),
  SCI_FI_AND_FANTASY(10765, "Sci-Fi & Fantasy", setOf(MediaType.TV)),
  SCIENCE_FICTION(878, "Science Fiction", setOf(MediaType.MOVIE)),
  SOAP(10766, "Soap", setOf(MediaType.TV)),
  TALK(10767, "Talk", setOf(MediaType.TV)),
  THRILLER(53, "Thriller", setOf(MediaType.MOVIE)),
  TV_MOVIE(10770, "TV Movie", setOf(MediaType.MOVIE)),
  WAR(10752, "War", setOf(MediaType.MOVIE)),
  WAR_AND_POLITICS(10768, "War & Politics", setOf(MediaType.TV)),
  WESTERN(37, "Western", setOf(MediaType.MOVIE, MediaType.TV)),
  ;

    companion object {
    private val byId: Map<Int, Genre> = entries.associateBy { it.id }
    private val byName: Map<String, Genre> = entries.associateBy { it.genreName }

    fun fromId(id: Int): Genre? = byId[id]

    fun fromName(name: String): Genre? = byName[name]

    fun forMediaType(mediaType: MediaType): List<Genre> =
      entries.filter { mediaType in it.mediaTypes }
  }
}
