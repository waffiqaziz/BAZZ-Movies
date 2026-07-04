package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import java.util.Locale

fun List<PartsItem>.sortedByOption(option: CollectionSortOption): List<PartsItem> =
  when (option) {
    CollectionSortOption.TITLE_AZ -> sortedBy { it.title?.lowercase(Locale.ROOT) }
    CollectionSortOption.TITLE_ZA -> sortedByDescending { it.title?.lowercase(Locale.ROOT) }
    CollectionSortOption.RATING_HIGH_TO_LOW -> sortedByDescending { it.voteAverage }
    CollectionSortOption.RATING_LOW_TO_HIGH -> sortedBy { it.voteAverage }
    CollectionSortOption.POPULARITY_HIGH_TO_LOW -> sortedByDescending { it.popularity }
    CollectionSortOption.POPULARITY_LOW_TO_HIGH -> sortedBy { it.popularity }
    CollectionSortOption.RELEASE_DATE_NEWEST -> sortedByDescending { it.releaseDate }
    CollectionSortOption.RELEASE_DATE_OLDEST -> sortedBy { it.releaseDate }
  }
