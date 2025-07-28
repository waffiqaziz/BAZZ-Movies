package com.waffiq.bazz_movies.feature.about.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class AboutActivityWithNoToolbar : AboutActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun setSupportActionBar(toolbar: Toolbar?) {
    /* do nothing */
  }
}
