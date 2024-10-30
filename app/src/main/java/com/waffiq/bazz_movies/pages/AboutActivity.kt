package com.waffiq.bazz_movies.pages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.R.mipmap.ic_launcher
import com.waffiq.bazz_movies.core.ui.R.id.action_bar_title
import com.waffiq.bazz_movies.core.utils.common.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.justifyTextView
import com.waffiq.bazz_movies.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAboutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // setup action bar
    val actionBarTitleView = findViewById<View>(action_bar_title)
    supportActionBar?.apply {
      setDisplayShowCustomEnabled(true)
      setDisplayShowHomeEnabled(true)
      setIcon(ic_launcher)
      customView = actionBarTitleView
      title = null // Hide the default title
    }

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
