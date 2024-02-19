package com.waffiq.bazz_movies.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.databinding.ActivityAboutBinding
import com.waffiq.bazz_movies.utils.Constants.TMDB_LINK_MAIN

class AboutActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAboutBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAboutBinding.inflate(layoutInflater)
    setContentView(binding.root)

    supportActionBar?.setDisplayShowHomeEnabled(true)
    supportActionBar?.setIcon(R.mipmap.ic_launcher)

    binding.fab.setOnClickListener{
      finish()
    }

    binding.ivTmdbLogo.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TMDB_LINK_MAIN)))
    }
  }
}