package com.waffiq.bazz_movies.core.network.data.remote.query

import com.waffiq.bazz_movies.core.network.data.remote.constants.Genre.Companion.toGenreQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.Keyword.Companion.toKeywordQuery
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.FIRST_AIR_DATE_GTE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.FIRST_AIR_DATE_LTE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.INCLUDE_ADULT
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.LANGUAGE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.ORIGIN_COUNTRY
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.PAGE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.RELEASE_DATE_GTE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.RELEASE_DATE_LTE
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.SORT_BY
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WATCH_REGION
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WITHOUT_GENRES
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WITHOUT_KEYWORDS
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WITH_GENRES
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WITH_KEYWORDS
import com.waffiq.bazz_movies.core.network.data.remote.constants.QueryParams.WITH_TYPE
import com.waffiq.bazz_movies.core.network.data.remote.constants.Region.Companion.toRegionQuery

fun DiscoverMovieParams.toQueryMap(): Map<String, String> =
  buildMap {
    put(PAGE, page.toString())
    put(INCLUDE_ADULT, includeAdult.toString())
    put(LANGUAGE, language)
    put(SORT_BY, sortBy)

    genres?.let { put(WITH_GENRES, it.toGenreQuery()) }
    genre?.let { put(WITH_GENRES, it) }
    keywords?.let { put(WITH_KEYWORDS, it.toKeywordQuery()) }
    keyword?.let { put(WITH_KEYWORDS, it) }

    releaseDateGte?.let { put(RELEASE_DATE_GTE, it) }
    releaseDateLte?.let { put(RELEASE_DATE_LTE, it) }
    watchRegion?.let { put(WATCH_REGION, it) }
  }

fun DiscoverTvParams.toQueryMap(): Map<String, String> =
  buildMap {
    put(PAGE, page.toString())
    put(INCLUDE_ADULT, includeAdult.toString())
    put(LANGUAGE, language)
    put(SORT_BY, sortBy)

    genres?.let { put(WITH_GENRES, it.toGenreQuery()) }
    genre?.let { put(WITH_GENRES, it) }
    keywords?.let { put(WITH_KEYWORDS, it.toKeywordQuery()) }
    keyword?.let { put(WITH_KEYWORDS, it) }
    type?.let { put(WITH_TYPE, it) }

    originCountry?.let { put(ORIGIN_COUNTRY, it.toRegionQuery()) }
    withoutGenres?.let { put(WITHOUT_GENRES, it.toGenreQuery()) }
    withoutKeywords?.let { put(WITHOUT_KEYWORDS, it.toKeywordQuery()) }
    firstAirDateGte?.let { put(FIRST_AIR_DATE_GTE, it) }
    firstAirDateLte?.let { put(FIRST_AIR_DATE_LTE, it) }
    watchRegion?.let { put(WATCH_REGION, it) }
  }
