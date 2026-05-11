package com.waffiq.bazz_movies.core.network.data.remote.query

import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.Companion.toGenreQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.Companion.toKeywordQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.Companion.toRegionQuery

fun DiscoverMovieParams.toQueryMap(): Map<String, String> = buildMap {
    put("page", page.toString())
    put("include_adult", includeAdult.toString())
    put("language", language)
    put("sort_by", sortBy)

    genres?.let { put("with_genres", it.toGenreQuery()) }
    genre?.let { put("with_genres", it) }
    keywords?.let { put("with_keywords", it.toKeywordQuery()) }
    keyword?.let { put("with_keywords", it) }

    releaseDateGte?.let { put("release_date.gte", it) }
    releaseDateLte?.let { put("release_date.lte", it) }
    watchRegion?.let { put("watch_region", it) }
  }

fun DiscoverTvParams.toQueryMap(): Map<String, String> = buildMap {
    put("page", page.toString())
    put("include_adult", includeAdult.toString())
    put("language", language)
    put("sort_by", sortBy)

    genres?.let { put("with_genres", it.toGenreQuery()) }
    genre?.let { put("with_genres", it) }
    keywords?.let { put("with_keywords", it.toKeywordQuery()) }
    keyword?.let { put("with_keywords", it) }

    originCountry?.let { put("with_origin_country", it.toRegionQuery()) }
    withoutGenres?.let { put("without_genres", it.toGenreQuery()) }
    withoutKeywords?.let { put("without_keywords", it.toKeywordQuery()) }
    firstAirDateGte?.let { put("first_air_date.gte", it) }
    firstAirDateLte?.let { put("first_air_date.lte", it) }
    watchRegion?.let { put("watch_region", it) }
  }
