package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class Keyword(val id: String) {

  COSTUME_DRAMA("195013"),
  DONGHUA("315535"),
  ROMANCE("9840"),

  // strictly keywords
  BISEXUAL_MAN("168812"),
  BOYS_LOVE("289844"),
  ECCHI("195669"),
  EROTIC("256466"),
  GAY_RELATIONSHIP("265777"),
  GAY_ROMANCE("240305"),
  GIRLS_LOVE("280003"),
  HENTAI("198385"),
  LESBIAN("264386"),
  LESBIAN_RELATIONSHIP("9833"),
  SOFTCORE("155477"),

  // could be added, but many movie/series have this theme but not the main focus,
  // so we choose exclude it
  LGBT("158718"),
  ;

    companion object {
    fun List<Keyword>.toKeywordQuery() = joinToString("|") { it.id }

    val STRICT_KEYWORDS = listOf(
      BISEXUAL_MAN,
      BOYS_LOVE,
      ECCHI,
      EROTIC,
      GAY_RELATIONSHIP,
      GAY_ROMANCE,
      GIRLS_LOVE,
      HENTAI,
      LESBIAN,
      LESBIAN_RELATIONSHIP,
      SOFTCORE,
    )
  }
}
