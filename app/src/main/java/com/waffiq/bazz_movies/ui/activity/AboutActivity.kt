package com.waffiq.bazz_movies.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.R.layout.custom_action_bar_title
import com.waffiq.bazz_movies.R.mipmap.ic_launcher
import com.waffiq.bazz_movies.databinding.ActivityAboutBinding
import com.waffiq.bazz_movies.utils.Constants.BAZZ_MOVIES_LINK
import com.waffiq.bazz_movies.utils.Constants.TMDB_LINK_MAIN

class AboutActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAboutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // setup action bar
    @SuppressLint("InflateParams")
    val actionBarTitleView = layoutInflater.inflate(custom_action_bar_title, null)
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
  }
}