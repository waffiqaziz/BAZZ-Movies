package com.waffiq.bazz_movies.feature.about.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar

class TestableAboutActivity : AboutActivity() {
  var mockSupportActionBar: ActionBar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun getSupportActionBar(): ActionBar? = mockSupportActionBar

  public override fun setupActionBar() {
    val actionBar = supportActionBar
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setDisplayShowHomeEnabled(true)
    }
  }
}
