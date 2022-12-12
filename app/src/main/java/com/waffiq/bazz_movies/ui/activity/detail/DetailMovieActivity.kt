package com.waffiq.bazz_movies.ui.activity.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {

  lateinit var binding: ActivityDetailMovieBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)


  }
}