package com.waffiq.bazz_movies.core.network.data.remote.query

import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre
import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.Companion.toGenreQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.Companion.toKeywordQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.Companion.toRegionQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DiscoverParamsExtTest {

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithDefaultValues_expectedDefaultQueryMap() {
    val params = DiscoverMovieParams()

    val result = params.toQueryMap()

    assertEquals("1", result["page"])
    assertEquals("false", result["include_adult"])
    assertEquals("en-US", result["language"])
    assertEquals(SortBy.POPULARITY_DESC, result["sort_by"])
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithGenres_expectedWithGenresQuery() {
    val params = DiscoverMovieParams(
      genres = listOf(
        Genre.ACTION,
        Genre.COMEDY,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Genre.ACTION,
        Genre.COMEDY,
      ).toGenreQuery(),
      result["with_genres"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithGenre_expectedWithGenresQuery() {
    val params = DiscoverMovieParams(
      genre = "28|35",
    )

    val result = params.toQueryMap()

    assertEquals("28|35", result["with_genres"])
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithKeywords_expectedWithKeywordsQuery() {
    val params = DiscoverMovieParams(
      keywords = listOf(
        Keyword.DONGHUA,
        Keyword.ROMANCE,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Keyword.DONGHUA,
        Keyword.ROMANCE,
      ).toKeywordQuery(),
      result["with_keywords"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithKeyword_expectedWithKeywordsQuery() {
    val params = DiscoverMovieParams(
      keyword = "123|456",
    )

    val result = params.toQueryMap()

    assertEquals("123|456", result["with_keywords"])
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithReleaseDates_expectedReleaseDateQueries() {
    val params = DiscoverMovieParams(
      releaseDateGte = "2025-01-01",
      releaseDateLte = "2025-12-31",
    )

    val result = params.toQueryMap()

    assertEquals("2025-01-01", result["release_date.gte"])
    assertEquals("2025-12-31", result["release_date.lte"])
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithWatchRegion_expectedWatchRegionQuery() {
    val params = DiscoverMovieParams(
      watchRegion = "US",
    )

    val result = params.toQueryMap()

    assertEquals("US", result["watch_region"])
  }

  @Test
  fun toQueryMap_whenDiscoverMovieParamsWithoutOptionalValues_expectedOptionalQueriesNotIncluded() {
    val params = DiscoverMovieParams()

    val result = params.toQueryMap()

    assertFalse(result.containsKey("with_genres"))
    assertFalse(result.containsKey("with_keywords"))
    assertFalse(result.containsKey("release_date.gte"))
    assertFalse(result.containsKey("release_date.lte"))
    assertFalse(result.containsKey("watch_region"))
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithDefaultValues_expectedDefaultQueryMap() {
    val params = DiscoverTvParams()

    val result = params.toQueryMap()

    assertEquals("1", result["page"])
    assertEquals("false", result["include_adult"])
    assertEquals("en-US", result["language"])
    assertEquals(SortBy.POPULARITY_DESC, result["sort_by"])
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithGenres_expectedWithGenresQuery() {
    val params = DiscoverTvParams(
      genres = listOf(
        Genre.ANIMATION,
        Genre.DRAMA,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Genre.ANIMATION,
        Genre.DRAMA,
      ).toGenreQuery(),
      result["with_genres"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithOriginCountry_expectedOriginCountryQuery() {
    val params = DiscoverTvParams(
      originCountry = listOf(
        Region.KOREA,
        Region.JAPAN,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Region.KOREA,
        Region.JAPAN,
      ).toRegionQuery(),
      result["with_origin_country"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithWithoutGenres_expectedWithoutGenresQuery() {
    val params = DiscoverTvParams(
      withoutGenres = listOf(
        Genre.HORROR,
        Genre.CRIME,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Genre.HORROR,
        Genre.CRIME,
      ).toGenreQuery(),
      result["without_genres"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithWithoutKeywords_expectedWithoutKeywordsQuery() {
    val params = DiscoverTvParams(
      withoutKeywords = listOf(
        Keyword.ROMANCE,
      ),
    )

    val result = params.toQueryMap()

    assertEquals(
      listOf(
        Keyword.ROMANCE,
      ).toKeywordQuery(),
      result["without_keywords"],
    )
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithFirstAirDates_expectedFirstAirDateQueries() {
    val params = DiscoverTvParams(
      firstAirDateGte = "2025-01-01",
      firstAirDateLte = "2025-12-31",
    )

    val result = params.toQueryMap()

    assertEquals("2025-01-01", result["first_air_date.gte"])
    assertEquals("2025-12-31", result["first_air_date.lte"])
  }

  @Test
  fun toQueryMap_whenDiscoverTvParamsWithoutOptionalValues_expectedOptionalQueriesNotIncluded() {
    val params = DiscoverTvParams()

    val result = params.toQueryMap()

    assertFalse(result.containsKey("with_genres"))
    assertFalse(result.containsKey("with_keywords"))
    assertFalse(result.containsKey("with_origin_country"))
    assertFalse(result.containsKey("without_genres"))
    assertFalse(result.containsKey("without_keywords"))
    assertFalse(result.containsKey("first_air_date.gte"))
    assertFalse(result.containsKey("first_air_date.lte"))
    assertFalse(result.containsKey("watch_region"))
  }
}
