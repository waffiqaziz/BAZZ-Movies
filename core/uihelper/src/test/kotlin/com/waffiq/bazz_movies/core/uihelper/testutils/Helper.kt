package com.waffiq.bazz_movies.core.uihelper.testutils

import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.MaterialShapeDrawable

object Helper {

  fun getAppBarColor(appBarLayout: AppBarLayout): Int? {
    return when (val background = appBarLayout.background) {
      is ColorDrawable -> background.color
      is MaterialShapeDrawable -> background.fillColor?.defaultColor
      is GradientDrawable -> {
        try {
          //  uses reflection because GradientDrawable hides color field
          val field = GradientDrawable::class.java.getDeclaredField("mFillPaint")
          field.isAccessible = true
          val paint = field.get(background) as? Paint
          paint?.color
        } catch (_: Exception) {
          null
        }
      }
      else -> null
    }
  }
}
