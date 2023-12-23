package com.waffiq.bazz_movies.ui.activity.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.ScoreRatingResponse
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesFavorite
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesWatchlist
import com.waffiq.bazz_movies.utils.Helper.showToastLong

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var viewModel: DetailMovieViewModel
  private lateinit var authenticationViewModel: AuthenticationViewModel

  private var favorite = false // is movie/tv series favorite/not
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
    // check if intent hasExtra
    if (intent.hasExtra(EXTRA_MOVIE)) {
//      dataExtra = intent.getParcelableExtra(EXTRA_MOVIE)!!
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra("EXTRA_MOVIE", ResultItem::class.java)!!
      } else {
        @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_MOVIE)!!
      }
    } else finish()
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

    // show data(year, overview, title)
    binding.apply {
      dataExtra.apply {
        val year = firstAirDate ?: releaseDate
        "${mediaType?.uppercase()} | $year".also { tvYearReleased.text = it }
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }

    // show backdrop
    Glide.with(binding.ivYoutubeVideo)
      .load(
        if (dataExtra.backdropPath.isNullOrEmpty()) {
          "https://image.tmdb.org/t/p/w1280/" + dataExtra.posterPath
        } else {
          "https://image.tmdb.org/t/p/w1280/" + dataExtra.backdropPath
        }
      ) // URL movie poster
      .placeholder(R.drawable.ic_bazz_logo)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivYoutubeVideo)

    binding.tvScoreTmdb.text = dataExtra.voteAverage.toString()

    // setup rv cast
    binding.rvCast.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterCast = CastAdapter()
    binding.rvCast.adapter = adapterCast

    // setup rv recommendation
    binding.rvRecommendation.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    val adapterRecommendation = TrendingAdapter()
    binding.rvRecommendation.adapter = adapterRecommendation.withLoadStateFooter(
      footer = LoadingStateAdapter {
        adapterRecommendation.retry()
      }
    )

    // hide recommendation if null
    adapterRecommendation.addLoadStateListener { loadState ->
      if (loadState.source.refresh is LoadState.NotLoading &&
        loadState.append.endOfPaginationReached &&
        adapterRecommendation.itemCount < 1
      ) {
        binding.tvRecommendationHeader.isVisible = false
        binding.rvRecommendation.isVisible = false
      } else {
        binding.tvRecommendationHeader.isVisible = true
        binding.rvRecommendation.isVisible = true
      }
    }

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
        adapterCast.setCast(it)
      }

      //show score, genres, backdrop, trailer
      viewModel.getDetailMovie(dataExtra.id!!)
      viewModel.detailMovie().observe(this) { movie ->

        //show genre
        val temp = movie.genres?.map { it?.name }
        if (temp != null) {
          binding.tvGenre.text = temp.joinToString(separator = ", ")
        }

//        //show score
//        movie.imdbId?.let { imdbId -> viewModel.getScore(API_KEY_IMDB_API_LIB, imdbId) }
//        viewModel.score().observe(this) { showScore(it) }

        // show OMDb detail (score)
        movie.imdbId?.let { imdbId -> viewModel.getScore(imdbId) }
        viewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

        // trailer
        movie.id?.let { viewModel.getLinkMovie(it) }
        viewModel.getLinkMovie().observe(this) {
          if (it.isNullOrEmpty()) hideTrailer(true)
          else btnTrailer(it)
        }

        // recommendation movie
        viewModel.getRecommendationMovie(movie.id!!).observe(this) {
          adapterRecommendation.submitData(lifecycle, it)
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
        adapterCast.setCast(it)
      }

      //show score
      viewModel.getExternalId(dataExtra.id!!)
      viewModel.externalId().observe(this) { externalId ->
        if (externalId.imdbId.isNullOrEmpty()) {
          binding.apply {
            tvScoreImdb.text = getString(R.string.not_available)
            tvScoreFilmAffinity.text = getString(R.string.not_available)
            tvScoreMetascore.text = getString(R.string.not_available)
            tvScoreRottenTomatoes.text = getString(R.string.not_available)
          }
        } else {
//        viewModel.getScore(API_KEY_IMDB_API_LIB, externalId.imdbId)
//        viewModel.score().observe(this) { showScore(it) }

          viewModel.getScore(externalId.imdbId)
          viewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

          //trailer
          externalId.id?.let { viewModel.getLinkTv(it) }
          viewModel.getLinkTv().observe(this) {
            if (it.isNullOrEmpty()) hideTrailer(true)
            else btnTrailer(it)
          }
        }
      }

      // recommendation tv
      viewModel.getRecommendationTv(dataExtra.id!!).observe(this) {
        adapterRecommendation.submitData(lifecycle, it)
      }

      //show genres
      viewModel.getDetailTv(dataExtra.id!!)
      viewModel.detailTv().observe(this) { tv ->
        val temp = tv.genres?.map { it?.name }
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")
      }
    }
  }

  private fun hideTrailer(hide: Boolean) {
    binding.ivYoutubeVideo.isVisible = !hide
    binding.ibPlay.isVisible = !hide
    binding.tvWatchTrailer.isVisible = !hide
  }

  private fun btnTrailer(link: String) {
    binding.ibPlay.setOnClickListener {
      Log.d("KKKKK", link)
      try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://www.youtube.com/watch?v=$link")
//        intent.setPackage("com.google.android.youtube")
        startActivity(intent)
      } catch (e: Exception) {
        Snackbar.make(
          binding.constraintLayout,
          R.string.yt_not_installed,
          Snackbar.LENGTH_LONG
        ).show()
      }
    }
  }

  private fun btnListener() {
    binding.btnBack.setOnClickListener { finish() }

    binding.btnFavorite.setOnClickListener {
      if (!isLogin) { //guest user
        if (!favorite) {
          // get movie is watchlist or not
          viewModel.getIsWatchlist().observe(this) {
            if (it) { // if movies is on watchlist, then edit is_favorited = true
              viewModel.updateToFavoriteDB(dataExtra.id!!)
            } else { // insert movie into room dataset, then set is_favorited = true
              viewModel.insertToFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
            }
          }
          favorite = true
          showToastAddedFavorite()
        } else {
          // get movie is watchlist or not
          viewModel.getIsWatchlist().observe(this) {
            if (it) { // if movies is on watchlist, then edit is_favorited to false
              viewModel.updateToRemoveFromFavoriteDB(dataExtra.id!!)
            } else { // remove movie from room database,  cuz not in favorite
              viewModel.removeFromFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
            }
          }
          favorite = false
          showToastRemoveFromFavorite()
        }
        changeBtnFavoriteBG(favorite)
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
          showToastRemoveFromFavorite()
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
          showToastAddedFavorite()
        }
        changeBtnFavoriteBG(favorite)
      }
    }

    binding.btnWatchlist.setOnClickListener {
      if (!isLogin) { // guest user
        if (!watchlist) { // if not in watchlist, then add to watchlist
          // get movie is favorite or not
          viewModel.getIsFavorite().observe(this) {
            if (it) { // if movie is on favorite, then update is_watchlist = true
              viewModel.updateToWatchlist(dataExtra.id!!)
            } else { // insert movie into room database and set is_watchlist = true
              viewModel.insertToFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
            }
          }
          watchlist = true
          showToastAddedWatchlist()
        } else {
          // get movie is favorite or not
          viewModel.getIsFavorite().observe(this) {
            if (it) { // if movie is favorite, then update is_watchlist to false
              viewModel.updateToRemoveFromWatchlistDB(dataExtra.id!!)
            } else { // remove movie from room database, cuz not in watchlist
              viewModel.removeFromFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
            }
          }
          watchlist = false
          showToastLong(this, this.getString(R.string.deleted_from_watchlist))
        }
        changeBtnWatchlistBG(watchlist)
      } else { // user login
        if (watchlist) {  // if movies is on favorite, then post watchlist as false (remove from watchlist)
          watchlist = false
          val unWatchlistModel = Watchlist(
            dataExtra.mediaType,
            dataExtra.id,
            false
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postWatchlist(user.token, unWatchlistModel, user.userId)
          }
          showToastRemoveFromWatchlist()
        } else {
          watchlist = true
          val watchlistModel = Watchlist(
            dataExtra.mediaType,
            dataExtra.id,
            true
          )
          authenticationViewModel.getUser().observe(this) { user ->
            viewModel.postWatchlist(user.token, watchlistModel, user.userId)
          }
          showToastAddedWatchlist()
        }
        changeBtnWatchlistBG(watchlist)
      }
    }

    binding.tvScoreYourScore.setOnClickListener { showDialogRate() }
    binding.tvYourScore.setOnClickListener { showDialogRate() }
    binding.tvScoreYourScore.text = getString(R.string.nan)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDim.visibility = View.VISIBLE // blur background when loading
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.backgroundDim.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun showScoreIMDBLib(data: ScoreRatingResponse) {
    binding.apply {
      tvScoreImdb.text = if (data.imDb == "") "NR" else data.imDb
      tvScoreFilmAffinity.text = if (data.filmAffinity == "") "NR" else data.filmAffinity
      tvScoreMetascore.text = if (data.metacritic == "") "NR" else data.metacritic
      tvScoreRottenTomatoes.text = if (data.rottenTomatoes == "") "NR" else data.rottenTomatoes
    }
  }

  private fun showDetailOMDb(data: OMDbDetailsResponse) {
    binding.apply {
      tvScoreImdb.text = if (data.imdbRating == "") "NR" else data.imdbRating
      tvScoreFilmAffinity.text = "N/A"
      tvScoreMetascore.text = if (data.metascore == "") "NR" else data.metascore

      tvScoreRottenTomatoes.text = "N/A"
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
        getStated(user.token)
        viewModel.stated().observe(this) {
          favorite = it.favorite!!
          changeBtnFavoriteBG(favorite)
        }
      }
    } else { //guest user
      viewModel.checkIsFavoriteDB(dataExtra.id!!)
      viewModel.getIsFavorite().observe(this) {
        changeBtnFavoriteBG(it)
        favorite = it
      }
    }
  }

  private fun isWatchlist(isLogin: Boolean) {
    if (isLogin) { //user
      authenticationViewModel.getUser().observe(this) { user ->
        getStated(user.token)
        viewModel.stated().observe(this) {
          watchlist = it.watchlist!!
          changeBtnWatchlistBG(watchlist)
        }
      }
    } else { //guest user
      viewModel.checkIsWatchlistDB(dataExtra.id!!)
      viewModel.getIsWatchlist().observe(this) {
        changeBtnWatchlistBG(it)
        watchlist = it
      }

    }

  }

  private fun changeBtnWatchlistBG(boolean: Boolean) {
    // if watchlist
    if (boolean) binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark_selected)

    //if not watchlist
    else binding.btnWatchlist.setImageResource(R.drawable.ic_bookmark)
  }

  private fun changeBtnFavoriteBG(boolean: Boolean) {
    // if favorite
    if (boolean) binding.btnFavorite.setImageResource(R.drawable.ic_hearth_selected)

    //if not favorite
    else binding.btnFavorite.setImageResource(R.drawable.ic_hearth)
  }

  private fun getStated(token: String) {
    if (dataExtra.mediaType == "movie") viewModel.getStatedMovie(token, dataExtra.id!!)
    else viewModel.getStatedTv(token, dataExtra.id!!)
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

  private fun showToastRemoveFromFavorite() {
    showToastLong(this, this.getString(R.string.deleted_from_favorite))
  }

  private fun showToastRemoveFromWatchlist() {
    showToastLong(this, this.getString(R.string.deleted_from_watchlist))
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

