package com.waffiq.bazz_movies.ui.activity.detail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.AuthenticationViewModel
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodel.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.utils.Constants.YOUTUBE_LINK_TRAILER
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.convertRuntime
import com.waffiq.bazz_movies.utils.Helper.dateFormater
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesFavorite
import com.waffiq.bazz_movies.utils.Helper.mapResponsesToEntitiesWatchlist
import com.waffiq.bazz_movies.utils.Helper.showToastLong


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var viewModel: DetailMovieViewModel
  private lateinit var authenticationViewModel: AuthenticationViewModel

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item favorite or not
  private var isLogin = false // is login as user or not

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
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra("EXTRA_MOVIE", ResultItem::class.java)!!
      } else {
        @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_MOVIE)!!
      }
    } else finish()
  }

  private fun showDetailData() {
    viewModel.getLoading().observe(this) { showLoading(it) }

    // shows backdrop
    Glide.with(binding.ivPicture).load(
      if (dataExtra.backdropPath.isNullOrEmpty()) {
        TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
      } else {
        TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
      }
    ).placeholder(R.drawable.ic_bazz_logo).error(R.drawable.ic_broken_image).into(binding.ivPicture)

    //shows poster
    Glide.with(binding.ivPoster)
      .load(TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath) // URL movie poster
      .placeholder(R.drawable.ic_bazz_placeholder_poster)
      .error(R.drawable.ic_broken_image)
      .into(binding.ivPoster)

    // show data(year, overview, title)
    binding.apply {
      dataExtra.apply {
        val year = (firstAirDate ?: releaseDate)?.let { dateFormater(it) }
        tvYearReleased.text = year
        tvMediaType.text = mediaType?.uppercase()
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }

    binding.tvScoreTmdb.text = (dataExtra.voteAverage ?: "N/A").toString()

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
      // shows directors
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

      // show or hide cast
      viewModel.getCreditsCastMovies().observe(this) {
        adapterCast.setCast(it)
        if (adapterCast.itemCount <= 0) {
          binding.rvCast.isVisible = false
          binding.tvCastHeader.isVisible = false
        } else {
          binding.rvCast.isVisible = true
          binding.tvCastHeader.isVisible = true
        }
      }

      // show score, genres, backdrop, trailer
      viewModel.detailMovie(dataExtra.id!!)
      viewModel.detailMovie().observe(this) { movie ->

        //show genre
        val temp = movie.genres?.map { it?.name }
        if (temp != null) {
          binding.tvGenre.text = temp.joinToString(separator = ", ")
        }

        binding.tvDuration.text = convertRuntime(movie.runtime!!)

//        //show score
//        movie.imdbId?.let { imdbId -> viewModel.getScore(API_KEY_IMDB_API_LIB, imdbId) }
//        viewModel.score().observe(this) { showScore(it) }

        // show OMDb detail (score)
        movie.imdbId?.let { imdbId -> viewModel.getScoreOMDb(imdbId) }
        viewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

        // trailer
        movie.id?.let { viewModel.getLinkMovie(it) }
        viewModel.getLinkMovie().observe(this) {
          if (it.isNullOrEmpty()) hideTrailer(true)
          else {
            hideTrailer(false)
            btnTrailer(it)
          }
        }

        // recommendation movie
        viewModel.getRecommendationMovie(movie.id!!).observe(this) {
          adapterRecommendation.submitData(lifecycle, it)
        }

        // age rate
        viewModel.ageRatingMovie().observe(this) {
          binding.tvAgeRating.text = it
        }
      }

    } else if (dataExtra.mediaType == "tv") {
      viewModel.getSnackBarText().observe(this) { showSnackBar(it) }

      // show directors
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

      // show or hide cast
      viewModel.getCreditsCastTv().observe(this) {
        adapterCast.setCast(it)
        if (adapterCast.itemCount <= 0) {
          binding.rvCast.isVisible = false
          binding.tvCastHeader.isVisible = false
        } else {
          binding.rvCast.isVisible = true
          binding.tvCastHeader.isVisible = true
        }
      }

      //show score
      viewModel.externalId(dataExtra.id!!)
      viewModel.externalId().observe(this) { externalId ->
        if (externalId.imdbId.isNullOrEmpty()) {
          binding.tvScoreImdb.text = getString(R.string.not_available)
          binding.tvScoreMetascore.text = getString(R.string.not_available)
        } else {
//        viewModel.getScore(API_KEY_IMDB_API_LIB, externalId.imdbId)
//        viewModel.score().observe(this) { showScore(it) }

          viewModel.getScoreOMDb(externalId.imdbId)
          viewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

          //trailer
          externalId.id?.let { viewModel.getLinkTv(it) }
          viewModel.getLinkTv().observe(this) {
            if (it.isNullOrEmpty()) hideTrailer(true)
            else {
              hideTrailer(false)
              btnTrailer(it)
            }
          }
        }
      }

      // recommendation tv
      viewModel.getRecommendationTv(dataExtra.id!!).observe(this) {
        adapterRecommendation.submitData(lifecycle, it)
      }

      //show genres & age rate
      viewModel.detailTv(dataExtra.id!!)
      viewModel.detailTv().observe(this) { tv ->
        val temp = tv.genres?.map { it?.name }
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")

        viewModel.ageRatingTv().observe(this) {
          binding.tvAgeRating.text = it
        }
      }
    }
  }

  private fun btnTrailer(link: String) {
    binding.ibPlay.setOnClickListener {
      Log.d("KKKKK", link)
      try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("$YOUTUBE_LINK_TRAILER$link")
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
          if (watchlist) { // if movies is on watchlist, then edit is_favorited = true
            viewModel.updateToFavoriteDB(dataExtra.id!!)
          } else { // insert movie into room dataset, then set is_favorited = true
            viewModel.insertToFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
          }
          favorite = true
          showToastAddedFavorite()
        } else {
          if (watchlist) { // if movies is on watchlist, then edit is_favorited to false
            viewModel.updateToRemoveFromFavoriteDB(dataExtra.id!!)
          } else { // remove movie from room database,  cuz not in favorite
            viewModel.removeFromFavoriteDB(mapResponsesToEntitiesFavorite(dataExtra))
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
          if (favorite) { // if movie is on favorite, then update is_watchlist = true
            viewModel.updateToWatchlistDB(dataExtra.id!!)
          } else { // insert movie into room database and set is_watchlist = true
            viewModel.insertToFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
          }
          watchlist = true
          showToastAddedWatchlist()
        } else {
          if (favorite) { // if movie is favorite, then update is_watchlist to false
            viewModel.updateToRemoveFromWatchlistDB(dataExtra.id!!)
          } else { // remove movie from room database, cuz not in watchlist
            viewModel.removeFromFavoriteDB(mapResponsesToEntitiesWatchlist(dataExtra))
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

  private fun showDetailOMDb(data: OMDbDetailsResponse) {
    binding.apply {
      tvScoreImdb.text = if (data.imdbRating == "") "NR" else data.imdbRating
      tvScoreMetascore.text = if (data.metascore == "") "NR" else data.metascore
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
      viewModel.isFavoriteDB(dataExtra.id!!)
      viewModel.isFavoriteDB().observe(this) {
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
      viewModel.isWatchlistDB(dataExtra.id!!)
      viewModel.isWatchlistDB().observe(this) {
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
    buttonYesAlert.setOnClickListener { showToastLong(this, "Yes") }

    val buttonNoAlert = dialog.findViewById(R.id.btn_no) as Button
    buttonNoAlert.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }

  private fun hideTrailer(hide: Boolean) {
    binding.ibPlay.isVisible = !hide
  }

  private fun animFadeOut() {
    val animation = animFadeOutLong(this)
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, 600)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE // blur background when loading
      binding.progressBar.visibility = View.VISIBLE
    } else animFadeOut()
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}