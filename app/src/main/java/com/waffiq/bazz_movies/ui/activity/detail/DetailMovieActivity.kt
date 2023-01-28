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
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntities
import com.waffiq.bazz_movies.utils.Helper.showToastLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailMovieActivity : AppCompatActivity() {

  lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var viewModel: DetailUserViewModel

  private var favorited = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory = ViewModelFactory.getInstance(this)
    viewModel = ViewModelProvider(this, factory)[DetailUserViewModel::class.java]

    getDataExtra()
    setData()
    btnListener()
    isFavorite()
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }

    binding.btnBookmarks.setOnClickListener {
      if (!favorited) {
        favorited = true
        viewModel.insertToFavorite(mapResponsesToEntities(dataExtra))
        showToastLong(
          this, this.getString(
            R.string.added,
            dataExtra.name ?: dataExtra.originalTitle ?: dataExtra.title
          )
        )
        binding.btnBookmarks.setImageResource(R.drawable.ic_bookmark_selected)
      } else {
        favorited = false
        viewModel.removeFromFavorite(mapResponsesToEntities(dataExtra))
        binding.btnBookmarks.setImageResource(R.drawable.ic_bookmark)
        showToastLong(this, this.getString(R.string.deleted))
      }

    }
  }

  private fun getDataExtra() {
    dataExtra = intent.getParcelableExtra(EXTRA_MOVIE)!!
  }

  private fun setData() {
    Glide.with(binding.ivPoster)
      .load("http://image.tmdb.org/t/p/w500/" + dataExtra.posterPath) // URL movie poster
      .placeholder(R.drawable.ic_bazz_logo)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivPoster)

    binding.apply {
      dataExtra.apply {
        tvYearReleased.text = firstAirDate ?: releaseDate
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
        tvGenre.text = genreIds?.let { iterateGenre(it) }
      }
    }

    if (dataExtra.mediaType == "movie") {
      //shows directors
      viewModel.getAllCreditMovies(dataExtra.id!!)
      viewModel.getCreditDirectorMovies().observe(this) { crew ->
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
      binding.rvCast.layoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
      val adapter = CastAdapter()
      binding.rvCast.adapter = adapter

      viewModel.getCreditsCastMovies().observe(this) {
        adapter.setCast(it)
      }

    } else if (dataExtra.mediaType == "tv") {

      //shows directors
      viewModel.getAllCreditTv(dataExtra.id!!)
      viewModel.getCreditDirectorTv().observe(this) { crew ->
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
      binding.rvCast.layoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
      val adapter = CastAdapter()
      binding.rvCast.adapter = adapter

      viewModel.getCreditsCastTv().observe(this) {
        adapter.setCast(it)
      }
    }
  }

  private fun isFavorite() {
    CoroutineScope(Dispatchers.Default).launch {
      val result = viewModel.checkIsFavorite(dataExtra.id!!)
      withContext(Dispatchers.Main) {
        if (result) {
          favorited = true
          binding.btnBookmarks.setImageResource(R.drawable.ic_bookmark_selected)
        } else {
          favorited = false
          binding.btnBookmarks.setImageResource(R.drawable.ic_bookmark)
        }
      }
    }
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}

