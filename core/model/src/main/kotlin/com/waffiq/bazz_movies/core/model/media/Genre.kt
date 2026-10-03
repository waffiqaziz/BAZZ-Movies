package com.waffiq.bazz_movies.core.model.media

@Suppress("MagicNumber")
enum class Genre(val id: Int, val mediaTypes: Set<MediaType>) {
  ACTION(28, setOf(MediaType.MOVIE)),
  ACTION_AND_ADVENTURE(10759, setOf(MediaType.TV)),
  ADVENTURE(12, setOf(MediaType.MOVIE)),
  ANIMATION(16, setOf(MediaType.MOVIE, MediaType.TV)),
  COMEDY(35, setOf(MediaType.MOVIE, MediaType.TV)),
  CRIME(80, setOf(MediaType.MOVIE, MediaType.TV)),
  DOCUMENTARY(99, setOf(MediaType.MOVIE, MediaType.TV)),
  DRAMA(18, setOf(MediaType.MOVIE, MediaType.TV)),
  FAMILY(10751, setOf(MediaType.MOVIE, MediaType.TV)),
  FANTASY(14, setOf(MediaType.MOVIE)),
  HISTORY(36, setOf(MediaType.MOVIE)),
  HORROR(27, setOf(MediaType.MOVIE)),
  KIDS(10762, setOf(MediaType.TV)),
  MUSIC(10402, setOf(MediaType.MOVIE)),
  MYSTERY(9648, setOf(MediaType.MOVIE, MediaType.TV)),
  NEWS(10763, setOf(MediaType.TV)),
  REALITY(10764, setOf(MediaType.TV)),
  ROMANCE(10749, setOf(MediaType.MOVIE)),
  SCI_FI_AND_FANTASY(10765, setOf(MediaType.TV)),
  SCIENCE_FICTION(878, setOf(MediaType.MOVIE)),
  SOAP(10766, setOf(MediaType.TV)),
  TALK(10767, setOf(MediaType.TV)),
  THRILLER(53, setOf(MediaType.MOVIE)),
  TV_MOVIE(10770, setOf(MediaType.MOVIE)),
  WAR(10752, setOf(MediaType.MOVIE)),
  WAR_AND_POLITICS(10768, setOf(MediaType.TV)),
  WESTERN(37, setOf(MediaType.MOVIE, MediaType.TV)),
  ;

    companion object {
    private val byId: Map<Int, Genre> = entries.associateBy { it.id }
    fun fromId(id: Int): Genre? = byId[id]

    fun forMediaType(mediaType: MediaType): List<Genre> =
      entries.filter { mediaType in it.mediaTypes }
  }
}
