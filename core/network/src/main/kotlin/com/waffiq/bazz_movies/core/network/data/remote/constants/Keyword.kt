package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class Keyword(val id: String) {

  COSTUME_DRAMA("195013"),
  DONGHUA("315535"),
  ROMANCE("9840"),

  // used for without keyboard to get romance drama, strictly
  BISEXUAL_MAN("168812"),
  GAY_ROMANCE("240305"),
  GAY_RELATIONSHIP("265777"),
  BOYS_LOVE("289844"),
  GIRLS_LOVE("280003"),
  LESBIAN("264386"),
  LESBIAN_RELATIONSHIP("9833"),

  // could be added, but many series have this theme
  // but not the main focus, so we choose exclude it
  LGBT("158718"),

  // used for without keyboard to get anime, strictly
  ECCHI("195669"),
  EROTIC("256466"),
  HENTAI("198385"),
  SOFTCORE("155477"),
  ;

    companion object {
    fun List<Keyword>.toKeywordQuery() = joinToString("|") { it.id }
  }
}
