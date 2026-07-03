package com.waffiq.bazz_movies.core.uihelper.testutils

import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.designsystem.R.string.title_az
import com.waffiq.bazz_movies.core.designsystem.R.string.title_za
import com.waffiq.bazz_movies.core.uihelper.model.LabeledOption

enum class SortOption(@StringRes override val label: Int) : LabeledOption {
  TITLE_AZ(title_az),
  TITLE_ZA(title_za),
}
