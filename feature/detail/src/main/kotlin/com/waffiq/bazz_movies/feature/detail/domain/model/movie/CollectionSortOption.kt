package com.waffiq.bazz_movies.feature.detail.domain.model.movie

import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.designsystem.R.string.popularity_asc
import com.waffiq.bazz_movies.core.designsystem.R.string.popularity_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_asc
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.release_date_newest
import com.waffiq.bazz_movies.core.designsystem.R.string.release_date_olders
import com.waffiq.bazz_movies.core.designsystem.R.string.title_az
import com.waffiq.bazz_movies.core.designsystem.R.string.title_za
import com.waffiq.bazz_movies.core.uihelper.model.LabeledOption

enum class CollectionSortOption(@StringRes override val label: Int) : LabeledOption {
  TITLE_AZ(title_az),
  TITLE_ZA(title_za),
  RATING_HIGH_TO_LOW(rating_desc),
  RATING_LOW_TO_HIGH(rating_asc),
  POPULARITY_HIGH_TO_LOW(popularity_desc),
  POPULARITY_LOW_TO_HIGH(popularity_asc),
  RELEASE_DATE_NEWEST(release_date_newest),
  RELEASE_DATE_OLDEST(release_date_olders),
}
