package com.waffiq.bazz_movies.feature.about.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    binding.fab.setOnClickListener { finish() }

    // setup tmdb logo
    binding.ivTmdbLogo.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TMDB_LINK_MAIN)))
    }

    binding.btnAboutUs.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(BAZZ_MOVIES_LINK)))
    }

    justifyTextView(binding.tvTmdbAttribute)
    justifyTextView(binding.tvAboutText)
  }
}
