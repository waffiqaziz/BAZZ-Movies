package com.waffiq.bazz_movies.ui.activity.detail

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.ResultItem
import com.waffiq.bazz_movies.data.remote.response.ScoreRatingResponse
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesFavorite
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesWatchlist
import com.waffiq.bazz_movies.utils.Helper.showToastLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var viewModel: DetailMovieViewModel
  private lateinit var authenticationViewModel: AuthenticationViewModel

  private var favorite = false
  private var watchlist = false
  private var isLogin = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory1 = ViewModelFactory.getInstance(this)
    viewModel = ViewModelProvider(this, factory1)[DetailMovieViewModel::class.java]

    val factory2 = ViewModelUserFactory.getInstance(dataStore)
    authenticationViewModel = ViewModelProvider(this, factory2)[AuthenticationViewModel::class.java]

    checkUser()
    getDataExtra()
    showDetailData()
    btnListener()
  }

  private fun checkUser() {
    authenticationViewModel.getUser().observe(this) {
      isLogin = it.token != "NaN"
      isFavorite(isLogin)
      isWatchlist(isLogin)
    }
  }

  private fun getDataExtra() {
    dataExtra = intent.getParcelableExtra(EXTRA_MOVIE)!!
  }

  private fun showDetailData() {
    viewModel.getLoading().observe(this) {
      showLoading(it)
    }
    //shows poster
    Glide.with(binding.ivPicture)
      .load(
        if (dataExtra.backdropPath.isNullOrEmpty()) {
          "http://image.tmdb.org/t/p/w500/" + dataExtra.posterPath
        } else {
          "http://image.tmdb.org/t/p/w780/" + dataExtra.backdropPath
        }
      ) // URL movie poster
      .placeholder(R.drawable.ic_bazz_logo)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivPicture)

    //show data(year, overview, title)
    binding.apply {
      dataExtra.apply {
        tvYearReleased.text = firstAirDate ?: releaseDate
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }
    binding.tvScoreTmdb.text = dataExtra.voteAverage.toString()

    //setup rv cast
    binding.rvCast.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapter = CastAdapter()
    binding.rvCast.adapter = adapter

    //show data based media type
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

      //show cast
      viewModel.getCreditsCastMovies().observe(this) {
        adapter.setCast(it)
      }

      //show score & genres
      viewModel.getDetailMovie(dataExtra.id!!)
      viewModel.detailMovie().observe(this) { movie ->

        //show genre
        val temp = movie.genres?.map { it?.name }
        if (temp != null) {
          binding.tvGenre.text = temp.joinToString(separator = ", ")
        }

        //show score
        movie.imdbId?.let { viewModel.getScore("k_v3ex7lfp", it) }
        viewModel.score().observe(this) {
          showScore(it)
        }
      }

    } else if (dataExtra.mediaType == "tv") {
      viewModel.getSnackBarText().observe(this) {
        showSnackBar(it)
      }

      //show directors
      viewModel.getAllCreditTv(dataExtra.id!!)
      viewModel.getCreditDirectorTv().observe(this) { crew ->
        binding.tvDirector.text = getString(
          R.string.writing,
          crew.map { it }.filter {
            it.job == "Writing"
          }.map { it.name }
            .toString()
            .dropLast(1)
            .substring(1)
        )
      }

      //show cast
      viewModel.getCreditsCastTv().observe(this) {
        adapter.setCast(it)
      }

      //show score
      viewModel.getExternalId(dataExtra.id!!)
      viewModel.extenalId().observe(this) { externalId ->
        if (externalId.imdbId.isNullOrEmpty()) {
          binding.apply {
            tvScoreImdb.text = "NR"
            tvScoreFilmAffinity.text = "NR"
            tvScoreMetascore.text = "NR"
            tvScoreRottenTomatoes.text = "NR"
          }
        } else {
          viewModel.getScore("k_v3ex7lfp", externalId.imdbId)
          viewModel.score().observe(this) {
            showScore(it)
          }
        }
      }

      //show genres
      viewModel.getDetailTv(dataExtra.id!!)
      viewModel.detailTv().observe(this) { tv ->
        val temp = tv.genres?.map { it?.name }
        if (temp != null) {
          binding.tvGenre.text = temp.joinToString(separator = ", ")
        }
      }
    }
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }

    binding.btnFavorite.setOnClickListener {
      if (!isLogin) { //guest user
        if (!favorite) {
          CoroutineScope(Dispatchers.IO).launch{
            if (viewModel.checkIsWatchlistDB(dataExtra.id!!)) { // if movies is on watchlist, then edit is_favorited to true
              viewModel.updateToFavoriteDB(dataExtra.id!!)
            } else { // insert
              viewModel.insertToFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
            }
          }
          favorite = true
          binding.btnFavorite.setImageResource(R.drawable.ic_hearth_selected)
          showToastAddedFavorite()
        } else {
          if (viewModel.checkIsWatchlistDB(dataExtra.id!!)) { // if movies is on watchlist, then edit is_favorited to false
            viewModel.updateToRemoveFromFavoriteDB(dataExtra.id!!)
          } else { // insert
            viewModel.removeFromFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
          }
          favorite = false
          binding.btnFavorite.setImageResource(R.drawable.ic_hearth)
          showToastLong(this, this.getString(R.string.deleted_from_favorite))
        }
      } else { // user login
        if (favorite) {
          favorite = false
          val favoriteMode = Favorite(
            dataExtra.mediaType,
            dataExtra.id,
            false
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postFavorite(user.token, favoriteMode, user.userId)
          }
          showToastLong(this, this.getString(R.string.deleted_from_favorite))
          binding.btnFavorite.setImageResource(R.drawable.ic_hearth)
        } else {
          favorite = true
          val favoriteMode = Favorite(
            dataExtra.mediaType,
            dataExtra.id,
            true
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postFavorite(user.token, favoriteMode, user.userId)
          }
          binding.btnFavorite.setImageResource(R.drawable.ic_hearth_selected)
          showToastAddedFavorite()
        }
      }
    }

    binding.btnWatchlist.setOnClickListener {
      if (!isLogin) { //guest user
        if (!watchlist) {
          CoroutineScope(Dispatchers.IO).launch{
            if (viewModel.checkIsFavoriteDB(dataExtra.id!!)) { // if movies is on favorite, then edit is_watchlist to true
              viewModel.updateToWatchlist(dataExtra.id!!)
            } else { // insert
              viewModel.insertToFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
            }
          }
          watchlist = true
          binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark_selected)
          showToastAddedWatchlist()
        } else {
          if (viewModel.checkIsWatchlistDB(dataExtra.id!!)) { // if movies is on favorite, then edit is_watchlist to false
            viewModel.updateToRemoveFromWatchlistDB(dataExtra.id!!)
          } else { // insert
            viewModel.removeFromFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
          }
          watchlist = false
          binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark)
          showToastLong(this, this.getString(R.string.deleted_from_watchlist))
        }
      } else { // user login
        if (watchlist) {
          watchlist = false
          val watchlistMode = Watchlist(
            dataExtra.mediaType,
            dataExtra.id,
            false
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postWatchlist(user.token, watchlistMode, user.userId)
          }
          showToastLong(this, this.getString(R.string.deleted_from_watchlist))
          binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark)
        } else {
          watchlist = true
          val watchlistMode = Watchlist(
            dataExtra.mediaType,
            dataExtra.id,
            true
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postWatchlist(user.token, watchlistMode, user.userId)
          }
          binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark_selected)
          showToastAddedWatchlist()
        }
      }
    }

    binding.tvScoreYourScore.setOnClickListener { showDialogRate() }
    binding.tvYourScore.setOnClickListener { showDialogRate() }
    binding.tvScoreYourScore.text = getString(R.string.nan)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDim.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.backgroundDim.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun showScore(data: ScoreRatingResponse) {
    binding.apply {
      tvScoreImdb.text = if (data.imDb == "") "NR" else data.imDb
      tvScoreFilmAffinity.text = if (data.filmAffinity == "") "NR" else data.filmAffinity
      tvScoreMetascore.text = if (data.metacritic == "") "NR" else data.metacritic
      tvScoreRottenTomatoes.text = if (data.rottenTomatoes == "") "NR" else data.rottenTomatoes
    }
  }

  private fun showSnackBar(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    Snackbar.make(
      binding.constraintLayout,
      message,
      Snackbar.LENGTH_SHORT
    ).show()
  }

  private fun isFavorite(isLogin: Boolean) {
    if (isLogin) { //user
      authenticationViewModel.getUser().observe(this) { user ->
        viewModel.getStatedMovies(user.token, dataExtra.id!!)
        viewModel.stated().observe(this) {
          favorite = it.favorite!!
          if (favorite) binding.btnFavorite.setImageResource(R.drawable.ic_hearth_selected)
          else binding.btnFavorite.setImageResource(R.drawable.ic_hearth)
        }
      }
    } else { //guest user
      CoroutineScope(Dispatchers.Default).launch {
        val result = viewModel.checkIsFavoriteDB(dataExtra.id!!)
        withContext(Dispatchers.Main) {
          if (result) {
            favorite = true
            binding.btnFavorite.setImageResource(R.drawable.ic_hearth_selected)
          } else {
            favorite = false
            binding.btnFavorite.setImageResource(R.drawable.ic_hearth)
          }
        }
      }
    }
  }

  private fun isWatchlist(isLogin: Boolean) {
    if (isLogin) { //user
      authenticationViewModel.getUser().observe(this) { user ->
        viewModel.getStatedMovies(user.token, dataExtra.id!!)
        viewModel.stated().observe(this) {
          watchlist = it.watchlist!!
          if (watchlist) binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark_selected)
          else binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark)
        }
      }
    } else { //guest user
      CoroutineScope(Dispatchers.Default).launch {
        val result = viewModel.checkIsWatchlistDB(dataExtra.id!!)
        withContext(Dispatchers.Main) {
          if (result) {
            watchlist = true
            binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark_selected)
          } else {
            watchlist = false
            binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark)
          }
        }
      }
    }
  }

  private fun showToastAddedFavorite() {
    showToastLong(
      this, this.getString(
        R.string.added_favorite,
        dataExtra.name ?: dataExtra.originalTitle ?: dataExtra.title
      )
    )
  }

  private fun showToastAddedWatchlist() {
    showToastLong(
      this, this.getString(
        R.string.added_watchlist,
        dataExtra.name ?: dataExtra.originalTitle ?: dataExtra.title
      )
    )
  }

  private fun showDialogRate() {
    val dialog = Dialog(this)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.popup_rating)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val buttonYesAlert = dialog.findViewById(R.id.btn_yes) as Button
    buttonYesAlert.setOnClickListener {
      showToastLong(this, "Yes")
    }

    val buttonNoAlert = dialog.findViewById(R.id.btn_no) as Button
    buttonNoAlert.setOnClickListener {
      dialog.dismiss()
    }
    dialog.show()
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}

