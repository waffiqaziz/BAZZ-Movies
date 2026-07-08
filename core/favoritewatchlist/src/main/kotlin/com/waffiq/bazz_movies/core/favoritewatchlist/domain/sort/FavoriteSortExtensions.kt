package com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort

import com.waffiq.bazz_movies.core.models.Favorite
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy.CREATED_AT_ASC
import com.waffiq.bazz_movies.core.network.data.remote.constants.SortBy.CREATED_AT_DESC

fun List<Favorite>.sortedByOption(option: GuestFavoriteSortOption): List<Favorite> =
  when (option) {
    GuestFavoriteSortOption.RECENTLY_ADDED -> sortedByDescending { it.lastUpdated }
    GuestFavoriteSortOption.OLDEST_ADDED -> sortedBy { it.lastUpdated }
    GuestFavoriteSortOption.TITLE_AZ -> sortedBy { it.title.lowercase() }
    GuestFavoriteSortOption.TITLE_ZA -> sortedByDescending { it.title.lowercase() }
    GuestFavoriteSortOption.RATING_HIGH_TO_LOW -> sortedByDescending { it.rating }
    GuestFavoriteSortOption.RATING_LOW_TO_HIGH -> sortedBy { it.rating }
    GuestFavoriteSortOption.POPULARITY_HIGH_TO_LOW -> sortedByDescending { it.popularity }
    GuestFavoriteSortOption.POPULARITY_LOW_TO_HIGH -> sortedBy { it.popularity }
    GuestFavoriteSortOption.RELEASE_DATE_NEWEST -> sortedByDescending { it.releaseDate }
    GuestFavoriteSortOption.RELEASE_DATE_OLDEST -> sortedBy { it.releaseDate }
  }

fun LoggedFavoriteSortOption.toQueryString(): String =
  when (this) {
    LoggedFavoriteSortOption.RECENTLY_ADDED -> CREATED_AT_DESC
    LoggedFavoriteSortOption.OLDEST_ADDED -> CREATED_AT_ASC
  }
