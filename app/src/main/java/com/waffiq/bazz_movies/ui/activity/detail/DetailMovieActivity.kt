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
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.drawable.ic_bazz_logo
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_selected
import com.waffiq.bazz_movies.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.R.drawable.ic_hearth_selected
import com.waffiq.bazz_movies.R.drawable.ic_hearth
import com.waffiq.bazz_movies.R.string.not_available
import com.waffiq.bazz_movies.R.string.yt_not_installed
import com.waffiq.bazz_movies.R.string.added_to_watchlist
import com.waffiq.bazz_movies.R.string.added_to_favorite
import com.waffiq.bazz_movies.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.R.string.deleted_from_watchlist
import com.waffiq.bazz_movies.R.string.not_available_full
import com.waffiq.bazz_movies.R.string.cant_provide_a_score
import com.waffiq.bazz_movies.R.string.status_
import com.waffiq.bazz_movies.R.id.rating_bar_action
import com.waffiq.bazz_movies.R.id.btn_yes
import com.waffiq.bazz_movies.R.id.btn_no
import com.waffiq.bazz_movies.R.layout.dialog_rating
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.font.gothic
import com.waffiq.bazz_movies.R.color.gray_100
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.data.local.model.Rate
import com.waffiq.bazz_movies.data.local.model.Watchlist
import com.waffiq.bazz_movies.data.remote.response.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.data.remote.response.tmdb.StatedResponse
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
import com.waffiq.bazz_movies.utils.Helper.dateFormatter
import com.waffiq.bazz_movies.utils.Helper.detailCrew
import com.waffiq.bazz_movies.utils.Helper.favFalseWatchlistFalse
import com.waffiq.bazz_movies.utils.Helper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.utils.Helper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.utils.Helper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.utils.Helper.showToastShort

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var detailViewModel: DetailMovieViewModel
  private lateinit var authViewModel: AuthenticationViewModel

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item favorite or not
  private var isLogin = false // is login as user or not

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val factory1 = ViewModelFactory.getInstance(this)
    detailViewModel = ViewModelProvider(this, factory1)[DetailMovieViewModel::class.java]

    val factory2 = ViewModelUserFactory.getInstance(dataStore)
    authViewModel = ViewModelProvider(this, factory2)[AuthenticationViewModel::class.java]

    checkUser()
    getDataExtra()
    showDetailData()
    btnListener()
  }


  private fun checkUser() {
    authViewModel.getUser().observe(this) {
      isLogin = it.token != "NaN"

      // hide user score if login as guest
      if (isLogin) { // show all score if login
        binding.yourScoreViewGroup.isGone = false
        binding.spaceRight.isGone = false
      } else { // hide it if guest user
        binding.yourScoreViewGroup.isGone = true
        binding.spaceRight.isGone = true
      }

      // check movie or tv is favorite watchlist
      isFavoriteWatchlist(isLogin)
    }
  }

  private fun getDataExtra() {
    // check if intent hasExtra
    if (intent.hasExtra(EXTRA_MOVIE)) {
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_MOVIE, ResultItem::class.java) ?: error("No DataExtra")
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_MOVIE) ?: error("No DataExtra")
      }
    } else finish()

    binding.swipeRefresh.setOnRefreshListener {
      val i = Intent(this, DetailMovieActivity::class.java)
      i.putExtra(EXTRA_MOVIE, dataExtra)
      activityTransition()
      binding.swipeRefresh.isRefreshing = false
      startActivity(i)
      activityTransition()
      finish()
    }
  }

  private fun activityTransition() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      overrideActivityTransition(
        OVERRIDE_TRANSITION_OPEN,
        android.R.anim.fade_in,
        android.R.anim.fade_out
      )
    } else {
      @Suppress("DEPRECATION")
      overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
  }

  private fun showDetailData() {
    detailViewModel.getLoading().observe(this) { showLoading(it) }
    detailViewModel.getSnackBarText().observe(this) { showSnackBarWarning(it) }

    // shows backdrop
    Glide.with(binding.ivPicture).load(
      if (dataExtra.backdropPath.isNullOrEmpty()) TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
      else TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
    ).placeholder(ic_bazz_logo).error(ic_broken_image).into(binding.ivPicture)

    //shows poster
    Glide.with(binding.ivPoster)
      .load(TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath) // URL movie poster
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_broken_image)
      .into(binding.ivPoster)

    // show data(year, overview, title)
    binding.apply {
      dataExtra.apply {
        val year = dateFormatter((firstAirDate ?: releaseDate ?: "")) ?: ""
        if (year.isEmpty()) tvYearReleased.text = getString(not_available)
        else tvYearReleased.text = year

        tvMediaType.text = mediaType?.uppercase()
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }

    // show tmdb scor e
    binding.tvScoreTmdb.text = if (dataExtra.voteAverage == 0.0F) getString(not_available)
    else dataExtra.voteAverage.toString()

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
      footer = LoadingStateAdapter { adapterRecommendation.retry() }
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

    //show more detail data based media type
    if (dataExtra.mediaType == "movie") {

      // shows directors
      dataExtra.id?.let { detailViewModel.getAllCreditMovies(it) }
      detailViewModel.getCreditDirectorMovies().observe(this) { crew ->
        createTable(detailCrew(crew))
      }

      // show or hide cast
      detailViewModel.getCreditsCastMovies().observe(this) {
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
      dataExtra.id?.let { detailViewModel.detailMovie(it) }
      detailViewModel.detailMovie().observe(this) { movie ->

        // show genre
        val temp = movie.genres?.map { it?.name }
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")

        // show runtime
        binding.tvDuration.text = movie.runtime?.let { convertRuntime(it) }

        // show OMDb detail (score)
        movie.imdbId?.let { imdbId -> detailViewModel.getScoreOMDb(imdbId) }
        detailViewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

        // trailer
        movie.id?.let { detailViewModel.getLinkMovie(it) }
        detailViewModel.getLinkMovie().observe(this) {
          if (it.isNullOrEmpty()) hideTrailer(true)
          else {
            hideTrailer(false)
            btnTrailer(it)
          }
        }

        // recommendation movie
        movie.id?.let { movieID ->
          detailViewModel.getRecommendationMovie(movieID).observe(this) {
            adapterRecommendation.submitData(lifecycle, it)
          }
        }

        // age rate
        detailViewModel.ageRatingMovie().observe(this) {
          binding.tvAgeRating.text =
            if (it.isNotEmpty() && it.isNotBlank()) it else getString(not_available)
        }
      }


    } else if (dataExtra.mediaType == "tv") {

      // show directors
      dataExtra.id?.let { detailViewModel.getAllCreditTv(it) }
      detailViewModel.getCreditDirectorTv().observe(this) { createTable(detailCrew(it)) }

      // show or hide cast
      detailViewModel.getCreditsCastTv().observe(this) {
        adapterCast.setCast(it)
        if (adapterCast.itemCount <= 0) {
          binding.rvCast.isVisible = false
          binding.tvCastHeader.isVisible = false
        } else {
          binding.rvCast.isVisible = true
          binding.tvCastHeader.isVisible = true
        }
      }

      // show score
      dataExtra.id?.let { detailViewModel.externalId(it) }
      detailViewModel.externalId().observe(this) { externalId ->

        if (externalId.imdbId.isNullOrEmpty()) {
          binding.tvScoreImdb.text = getString(not_available)
          binding.tvScoreMetascore.text = getString(not_available)
        } else {
          detailViewModel.getScoreOMDb(externalId.imdbId)
          detailViewModel.detailOMDb().observe(this) { showDetailOMDb(it) }

          //trailer
          dataExtra.id?.let { detailViewModel.getLinkTv(it) }
          detailViewModel.getLinkTv().observe(this) {
            if (it.isNullOrEmpty()) hideTrailer(true)
            else {
              hideTrailer(false)
              btnTrailer(it)
            }
          }
        }
      }

      // recommendation tv
      dataExtra.id?.let { mediaId ->
        detailViewModel.getRecommendationTv(mediaId).observe(this) {
          adapterRecommendation.submitData(lifecycle, it)
        }
      }

      //show genres & age rate
      dataExtra.id?.let { detailViewModel.detailTv(it) }
      detailViewModel.detailTv().observe(this) { tv ->
        val temp = tv.genres?.map { it?.name }
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")

        // show runtime
        binding.tvDuration.text = getString(status_, tv.status)


        detailViewModel.ageRatingTv().observe(this) {
          binding.tvAgeRating.text = it ?: getString(not_available)
        }
      }
    }

    // show production country
    detailViewModel.getProductionCountry().observe(this) {
      binding.tvYearReleased.append(" ($it)")
    }
  }

  private fun getStated(token: String) {
    if (dataExtra.mediaType == "movie") dataExtra.id?.let {
      detailViewModel.getStatedMovie(token, it)
    }
    else dataExtra.id?.let { detailViewModel.getStatedTv(token, it) }
  }

  private fun showRatingUserLogin(state: StatedResponse) {
    binding.tvScoreYourScore.text = if (state.rated == false) getString(not_available)
    else state.rated.toString().replace("{value=", "").replace("}", "")
  }

  private fun btnTrailer(link: String) {
    binding.ibPlay.setOnClickListener {
      try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("$YOUTUBE_LINK_TRAILER$link")
        startActivity(intent)
      } catch (e: Exception) {
        Snackbar.make(
          binding.constraintLayout,
          yt_not_installed,
          Snackbar.LENGTH_LONG
        ).show()
      }
    }
  }

  private fun btnListener() {
    binding.apply {
      btnBack.setOnClickListener { finish() }

      btnFavorite.setOnClickListener {
        if (!isLogin) { // guest user
          if (!favorite) { // if not yet favorite, then add to favorite
            if (watchlist) { // if movies is on watchlist, then edit is_favorite = true
              detailViewModel.updateToFavoriteDB(favTrueWatchlistTrue(dataExtra))
            } else { // insert movie into room dataset, then set is_favorite = true
              detailViewModel.insertToDB(favTrueWatchlistFalse(dataExtra))
            }
            favorite = true
            showToastAddedFavorite()
          } else { // if already favorite, then remove from favorite list
            if (watchlist) { // if movies is on watchlist, then edit is_favorite to false
              detailViewModel.updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(dataExtra))
            } else { // remove movie from room database, cuz not in favorite
              detailViewModel.delFromFavoriteDB(favFalseWatchlistFalse(dataExtra))
            }
            favorite = false
            showToastRemoveFromFavorite()
          }
          changeBtnFavoriteBG(favorite)
        } else { // user login
          if (favorite) {
            postDataToTMDB(isModeFavorite = true, state = true)
            showToastRemoveFromFavorite()
          } else {
            postDataToTMDB(isModeFavorite = true, state = false)
            showToastAddedFavorite()
          }
          changeBtnFavoriteBG(favorite)
        }
      }

      btnWatchlist.setOnClickListener {
        if (!isLogin) { // guest user
          if (!watchlist) { // if not in watchlist, then add to watchlist
            if (favorite) { // if movie is on favorite, then update is_watchlist = true
              detailViewModel.updateToWatchlistDB(favTrueWatchlistTrue(dataExtra))
            } else { // insert movie into room database and set is_watchlist = true
              detailViewModel.insertToDB(favFalseWatchlistTrue(dataExtra))
            }
            watchlist = true
            showToastAddedWatchlist()
          } else { // if in watchlist, then remove from watchlist
            if (favorite) { // if movie is also favorite, then update is_watchlist to false
              detailViewModel.updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(dataExtra))
            } else { // remove movie from room database, cuz is not favorite
              detailViewModel.delFromFavoriteDB(favFalseWatchlistFalse(dataExtra))
            }
            watchlist = false
            showToastRemoveFromWatchlist()
          }
          changeBtnWatchlistBG(watchlist)
        } else { // user login
          if (watchlist) {  // if movies is on favorite, then post watchlist as false (remove from watchlist)
            postDataToTMDB(isModeFavorite = false, state = true)
            showToastRemoveFromWatchlist()
          } else {
            postDataToTMDB(isModeFavorite = false, state = false)
            showToastAddedWatchlist()
          }
          changeBtnWatchlistBG(watchlist)
        }
      }

      yourScoreViewGroup.setOnClickListener { showDialogRate() }

      imdbViewGroup.setOnClickListener { if (!tvScoreImdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      tmdbViewGroup.setOnClickListener { if (!tvScoreTmdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      metascoreViewGroup.setOnClickListener { if (!tvScoreMetascore.text.contains("[0-9]".toRegex())) showDialogNotRated() }

    }
  }

  private fun postDataToTMDB(isModeFavorite: Boolean, state: Boolean) {
    if (isModeFavorite) { // for favorite
      favorite = !state
      val favoriteMode = Favorite(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authViewModel.getUser().observe(this) { user ->
        detailViewModel.postFavorite(user.token, favoriteMode, user.userId)
      }

    } else { // for watchlist
      watchlist = !state
      val unWatchlistModel = Watchlist(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authViewModel.getUser().observe(this) { user ->
        detailViewModel.postWatchlist(user.token, unWatchlistModel, user.userId)
      }
    }
  }


  // show score from OMDb API
  private fun showDetailOMDb(data: OMDbDetailsResponse) {
    binding.apply {
      tvScoreImdb.text =
        if (data.imdbRating.isNullOrEmpty() || data.imdbRating.isBlank()) getString(not_available) else data.imdbRating
      tvScoreMetascore.text =
        if (data.metascore.isNullOrEmpty() || data.metascore.isBlank()) getString(not_available) else data.metascore
    }
  }

  private fun isFavoriteWatchlist(isLogin: Boolean) {
    if (isLogin) { //user
      authViewModel.getUser().observe(this) { user ->
        getStated(user.token)
        detailViewModel.getStated().observe(this) {
          if (it != null) {
            favorite = it.favorite
            watchlist = it.watchlist
            showRatingUserLogin(it)
          }
          changeBtnFavoriteBG(favorite)
          changeBtnWatchlistBG(watchlist)
        }
      }
    } else { //guest user
      dataExtra.id?.let { detailViewModel.isFavoriteDB(it) }
      detailViewModel.isFavoriteDB().observe(this) {
        changeBtnFavoriteBG(it)
        favorite = it
      }
      dataExtra.id?.let { detailViewModel.isWatchlistDB(it) }
      detailViewModel.isWatchlistDB().observe(this) {
        changeBtnWatchlistBG(it)
        watchlist = it
      }
    }
  }

  private fun changeBtnWatchlistBG(boolean: Boolean) {
    // if watchlist
    if (boolean) binding.btnWatchlist.setImageResource(ic_bookmark_selected)

    //if not watchlist
    else binding.btnWatchlist.setImageResource(ic_bookmark)
  }

  private fun changeBtnFavoriteBG(boolean: Boolean) {
    // if favorite
    if (boolean) binding.btnFavorite.setImageResource(ic_hearth_selected)

    //if not favorite
    else binding.btnFavorite.setImageResource(ic_hearth)
  }


  // toast, snackbar, dialog
  private fun showToastAddedFavorite() {
    showToastShort(
      this, "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        added_to_favorite
      )
    )
  }

  private fun showToastAddedWatchlist() {
    showToastShort(
      this, "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        added_to_watchlist
      )
    )
  }

  private fun showToastRemoveFromFavorite() {
    showToastShort(
      this,
      "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        deleted_from_favorite
      )
    )
  }

  private fun showToastRemoveFromWatchlist() {
    showToastShort(
      this,
      "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        deleted_from_watchlist
      )
    )
  }

  private fun showDialogNotRated() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder
      .setTitle(getString(not_available_full))
      .setMessage(getString(cant_provide_a_score))

    val dialog: AlertDialog = builder.create()
    dialog.show()
  }

  private fun showDialogRate() {
    val dialog = Dialog(this)
    val dialogView = View.inflate(this, dialog_rating, null)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(dialogView)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val ratingBar = dialogView.findViewById<RatingBar>(rating_bar_action)

    val buttonYesAlert = dialogView.findViewById(btn_yes) as Button
    buttonYesAlert.setOnClickListener {
      showToastShort(this, "Rating: ${ratingBar.rating * 2}")
      val rate = Rate(value = ratingBar.rating * 2)
      authViewModel.getUser().observe(this) { user ->
        if (dataExtra.mediaType.equals("movie"))
          dataExtra.id?.let { it1 -> detailViewModel.postMovieRate(user.token, rate, it1) }
        else dataExtra.id?.let { it1 -> detailViewModel.postTvRate(user.token, rate, it1) }

        // change rating
        binding.tvScoreYourScore.text = rate.value.toString()
      }
      dialog.dismiss()
    }

    val buttonNoAlert = dialogView.findViewById(btn_no) as Button
    buttonNoAlert.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }

  private fun showSnackBarWarning(eventMessage: Event<String>) {
    val message = eventMessage.getContentIfNotHandled() ?: return
    val snackBar = Snackbar.make(
      binding.constraintLayout,
      message,
      Snackbar.LENGTH_SHORT
    )

    val snackbarView = snackBar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(this, red_matte))
    snackBar.show()
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
    }, DELAY_TIME)
  }

  private fun showLoading(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else animFadeOut()
  }


  // credits table
  private fun createTable(pair: Pair<MutableList<String>, MutableList<String>>) {
    val (job, crewName) = pair

    // Create a TableLayout
    val tableLayout = TableLayout(this)
    tableLayout.layoutParams = TableLayout.LayoutParams(
      TableLayout.LayoutParams.MATCH_PARENT,
      TableLayout.LayoutParams.WRAP_CONTENT
    )

    // Create rows
    for (i in 0..<job.size) {
      val tableRow = TableRow(this)
      tableRow.layoutParams = TableRow.LayoutParams(
        TableRow.LayoutParams.MATCH_PARENT,
        TableRow.LayoutParams.WRAP_CONTENT
      )

      val cell1 = createTableCell(job[i])
      val cell2 = createTableCell(": " + crewName[i])

      tableRow.addView(cell1)
      tableRow.addView(cell2)

      tableLayout.addView(tableRow)
    }

    binding.table.addView(tableLayout)
  }

  private fun createTableCell(text: String): TextView {
    val textView = TextView(this)
    textView.text = text
    textView.layoutParams = TableRow.LayoutParams(
      TableRow.LayoutParams.WRAP_CONTENT,
      TableRow.LayoutParams.WRAP_CONTENT
    )
    textView.typeface = ResourcesCompat.getFont(this, gothic)
    textView.gravity = Gravity.START
    textView.textSize = 14F
    textView.setPadding(0, 7, 24, 7)
    textView.setTextColor(ActivityCompat.getColor(this, gray_100))
    return textView
  }


  companion object {
    const val EXTRA_MOVIE = "MOVIE"
    const val DELAY_TIME = 600L
  }
}