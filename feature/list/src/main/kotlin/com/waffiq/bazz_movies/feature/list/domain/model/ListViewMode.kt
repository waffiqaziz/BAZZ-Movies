package com.waffiq.bazz_movies.feature.list.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_grid
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_grid_2
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_list
import com.waffiq.bazz_movies.core.designsystem.R.string.view_detailed
import com.waffiq.bazz_movies.core.designsystem.R.string.view_detailed_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.view_three_columns
import com.waffiq.bazz_movies.core.designsystem.R.string.view_three_columns_desc
import com.waffiq.bazz_movies.core.designsystem.R.string.view_two_columns
import com.waffiq.bazz_movies.core.designsystem.R.string.view_two_columns_desc

@Suppress("MagicNumber")
enum class ListViewMode(
  @StringRes val title: Int,
  @StringRes val subtitle: Int,
  @DrawableRes val icon: Int,
  val spanCount: Int,
) {
  TWO_COLUMNS(view_two_columns, view_two_columns_desc, ic_grid_2, 2),
  THREE_COLUMNS(view_three_columns, view_three_columns_desc, ic_grid, 3),
  DETAILED(view_detailed, view_detailed_desc, ic_list, 1),
  ;

    val isDetailed get() = this == DETAILED
}
