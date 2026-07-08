package com.waffiq.bazz_movies.core.favoritewatchlist.domain.sort

import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.designsystem.R.string.oldest_added
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.uihelper.model.LabeledOption

enum class LoggedFavoriteSortOption(@StringRes override val label: Int) : LabeledOption {
  RECENTLY_ADDED(recently_added),
  OLDEST_ADDED(oldest_added),
}
