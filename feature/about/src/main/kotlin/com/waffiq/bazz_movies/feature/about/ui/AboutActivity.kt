package com.waffiq.bazz_movies.feature.about.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.waffiq.bazz_movies.core.common.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.feature.about.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAboutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.toolbarLayout.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)

    // setup tmdb logo
    binding.ivTmdbLogo.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, TMDB_LINK_MAIN.toUri()))
    }

    binding.btnAboutUs.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, BAZZ_MOVIES_LINK.toUri()))
    }

    justifyTextView(binding.tvTmdbAttribute)
    justifyTextView(binding.tvAboutText)
  }

  override fun onSupportNavigateUp(): Boolean {
    finish()
    return true
  }
}
