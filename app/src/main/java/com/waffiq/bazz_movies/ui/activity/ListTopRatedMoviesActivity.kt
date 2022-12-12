package com.waffiq.bazz_movies.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.waffiq.bazz_movies.databinding.ActivityListTopRatedMoviesBinding
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.MovieAdapter
import com.waffiq.bazz_movies.ui.viewmodel.ListTopRatedMoviesViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory

class ListTopRatedMoviesActivity : AppCompatActivity() {
  private lateinit var binding: ActivityListTopRatedMoviesBinding
  private val viewModel: ListTopRatedMoviesViewModel by viewModels {
    ViewModelFactory(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityListTopRatedMoviesBinding.inflate(layoutInflater)
    setContentView(binding.root)

//    setupToolbar()
    setListStory()
  }

  private fun setupToolbar(){
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)
  }

  private fun setListStory() {
    binding.rvMovie.layoutManager = LinearLayoutManager(this)
    val adapter = MovieAdapter()

    binding.rvMovie.adapter = adapter.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapter.retry()
      }
    )

    viewModel.getTopRatedMovies().observe(this) {
      adapter.submitData(lifecycle,it)
    }
//    viewModel.getMoviesGenres().observe(this){
//      binding.tvInfo.text = it.toString()
//    }
  }

}