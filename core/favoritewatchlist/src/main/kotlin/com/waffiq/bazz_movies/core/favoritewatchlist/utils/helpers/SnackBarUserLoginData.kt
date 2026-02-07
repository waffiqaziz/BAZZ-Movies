package com.waffiq.bazz_movies.core.favoritewatchlist.utils.helpers

import com.waffiq.bazz_movies.core.domain.FavoriteParams
import com.waffiq.bazz_movies.core.domain.WatchlistParams

/**
 * Data class representing the information required to display a Snackbar
 * related to the user's login state, particularly focusing on their favorites
 * and watchlist.
 *
 * This class is used to encapsulate the outcome of a user login action,
 * including success status, title also as error message, and additional data related
 * to the user's favorite and watchlist models.
 *
 * @param isSuccess Boolean indicating whether the login operation was successful.
 * @param title A message to be displayed in the Snackbar. This can either be an
 *              error message or a success message based on the value of `isSuccess`.
 * @param favoriteModel An optional [FavoriteParams] object containing the user's
 *                      favorite items. This can be null if no favorites are involved.
 * @param watchlistModel An optional [WatchlistParams] object containing the
 *                       user's watchlist items. This can be null if no watchlist
 *                       is involved.
 */
data class SnackBarUserLoginData(
  val isSuccess: Boolean, // indicates whether the login was successful or not
  val title: String, // the title/message to be shown in the Snackbar, could be an error or success message
  val favoriteModel: FavoriteParams?, // optional data related to the user's favorites, may be null if not relevant
  val watchlistModel: WatchlistParams? // optional data related to the user's watchlist, may be null if not relevant
)
