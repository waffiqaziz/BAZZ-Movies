package com.waffiq.bazz_movies.core.uihelper.testutils

import android.graphics.drawable.ColorDrawable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable

object Helper {

  fun getAppBarColor(appBarLayout: AppBarLayout): Int? {
    return when (val background = appBarLayout.background) {
      is ColorDrawable -> background.color
      is MaterialShapeDrawable -> background.fillColor?.defaultColor
      else -> null
    }
  }
}
