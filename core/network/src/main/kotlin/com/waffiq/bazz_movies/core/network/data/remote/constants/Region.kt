package com.waffiq.bazz_movies.core.network.data.remote.constants

enum class Region(val codeCountry: String) {
  CHINA("CN"),
  INDONESIA("ID"),
  JAPAN("JP"),
  KOREA("KR"),
  MALAYSIA("MY"),
  TAIWAN("TW"),
  THAILAND("TH"),
  ;

    companion object {
    fun List<Region>.toRegionQuery() = joinToString("|") { it.codeCountry }
  }
}
