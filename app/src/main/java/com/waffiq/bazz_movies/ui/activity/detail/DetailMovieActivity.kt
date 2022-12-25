package com.waffiq.bazz_movies.ui.activity.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.ResultItem
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.utils.Helper.iterateGenre

class DetailMovieActivity : AppCompatActivity() {

  lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var viewModel: DetailUserViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelFactory.getInstance(this)
    viewModel = ViewModelProvider(this, factory)[DetailUserViewModel::class.java]

    binding.btnBack.setOnClickListener { finish() }
    setData()
  }

  private fun setData() {
    dataExtra =
      intent.getParcelableExtra(EXTRA_MOVIE)!!

    Glide.with(binding.ivPoster)
      .load("http://image.tmdb.org/t/p/w500/" + dataExtra.posterPath) // URL movie poster
      .placeholder(R.drawable.ic_bazz_logo)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivPoster)

    binding.apply {
      dataExtra.apply {
        tvYearReleased.text = firstAirDate ?: releaseDate
        tvOverview.text = overview
        tvTitle.text = title ?: originalTitle ?: originalName
        tvGenre.text = genreIds?.let { iterateGenre(it) }
      }
    }

    //shows directors
    viewModel.getAllCredits(dataExtra.id!!)
    viewModel.getCreditsDirector().observe(this) { crew ->
      binding.tvDirector.text = getString(
        R.string.director,
        crew.map { it }.filter {
          it.job == "Director"
        }.map { it.name }
          .toString()
          .dropLast(1)
          .substring(1)
      )
    }

    //shows picture cast
    binding.rvCast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapter = CastAdapter()
    binding.rvCast.adapter = adapter

    viewModel.getCreditsCast().observe(this) {
      adapter.setCast(it)
    }
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}