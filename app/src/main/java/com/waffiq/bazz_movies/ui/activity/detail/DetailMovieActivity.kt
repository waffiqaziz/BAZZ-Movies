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
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
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

      // hide user score if login as guest
      if (isLogin) { // show all score if login
        binding.yourScoreViewGroup.isGone = false
        binding.spaceRight.isGone = false
      } else { // hide if guest
        binding.yourScoreViewGroup.isGone = true
        binding.spaceRight.isGone = true
      }

      isFavorite(isLogin)
      isWatchlist(isLogin)
    }
  }

  private fun getDataExtra() {
    // check if intent hasExtra
    if (intent.hasExtra(EXTRA_MOVIE)) {
      dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_MOVIE, ResultItem::class.java)!!
      } else {
        @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_MOVIE)!!
      }
    } else finish()

    binding.swipeRefresh.setOnRefreshListener {
      val i = Intent(this, DetailMovieActivity::class.java)
      i.putExtra(EXTRA_MOVIE, dataExtra)
      overridePendingTransition(0, 0)
      binding.swipeRefresh.isRefreshing = false
      startActivity(i)
      overridePendingTransition(0, 0)
      finish()
    }
  }

  private fun showDetailData() {
    viewModel.getLoading().observe(this) { showLoading(it) }
    viewModel.getSnackBarText().observe(this) { showSnackBarWarning(it) }

    // shows backdrop
    Glide.with(binding.ivPicture).load(
      if (dataExtra.backdropPath.isNullOrEmpty()) TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
      else TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
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
        val year = dateFormater((firstAirDate ?: releaseDate ?: ""))!!
        if (year.isEmpty()) {
          tvYearReleased.text = getString(R.string.not_available)
        } else tvYearReleased.text = year

        tvMediaType.text = mediaType?.uppercase()
        tvOverview.text = overview
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }

    binding.tvScoreTmdb.text = (if (dataExtra.voteAverage == 0.0F) {
      getString(R.string.not_available)
    } else dataExtra.voteAverage).toString()

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
      viewModel.getAllCreditMovies(dataExtra.id!!)
      viewModel.getCreditDirectorMovies().observe(this) { crew ->
//        binding.tvDirector.text = detailCrew(this, crew)
        createTable(detailCrew(crew))
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
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")

        binding.tvDuration.text = convertRuntime(movie.runtime!!)

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
        viewModel.ageRatingMovie().observe(this) { binding.tvAgeRating.text = it }
      }


    } else if (dataExtra.mediaType == "tv") {

      // show directors
      viewModel.getAllCreditTv(dataExtra.id!!)
      viewModel.getCreditDirectorTv().observe(this) {

        // binding.tvDirector.text = detailCrew(this, crew)
        createTable(detailCrew(it))
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
          binding.tvAgeRating.text = it ?: getString(R.string.not_available)
        }
      }
    }

    // show production country
    viewModel.getProductionCountry().observe(this) {
      binding.tvYearReleased.append(" ($it)")
    }
  }

  private fun btnTrailer(link: String) {
    binding.ibPlay.setOnClickListener {
      Log.d("KKKKK", link)
      try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("$YOUTUBE_LINK_TRAILER$link")
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
    binding.apply {
      btnBack.setOnClickListener { finish() }

      btnFavorite.setOnClickListener {
        if (!isLogin) { // guest user
          if (!favorite) { // if not yet favorite, then add to favorite
            if (watchlist) { // if movies is on watchlist, then edit is_favorite = true
              viewModel.updateToFavoriteDB(favTrueWatchlistTrue(dataExtra))
            } else { // insert movie into room dataset, then set is_favorite = true
              viewModel.insertToDB(favTrueWatchlistFalse(dataExtra))
            }
            favorite = true
            showToastAddedFavorite()
          } else { // if already favorite, then remove from favorite list
            if (watchlist) { // if movies is on watchlist, then edit is_favorite to false
              viewModel.updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(dataExtra))
            } else { // remove movie from room database, cuz not in favorite
              viewModel.delFromFavoriteDB(favFalseWatchlistFalse(dataExtra))
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
              viewModel.updateToWatchlistDB(favTrueWatchlistTrue(dataExtra))
            } else { // insert movie into room database and set is_watchlist = true
              viewModel.insertToDB(favFalseWatchlistTrue(dataExtra))
            }
            watchlist = true
            showToastAddedWatchlist()
          } else { // if in watchlist, then remove from watchlist
            if (favorite) { // if movie is also favorite, then update is_watchlist to false
              viewModel.updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(dataExtra))
            } else { // remove movie from room database, cuz is not favorite
              viewModel.delFromFavoriteDB(favFalseWatchlistFalse(dataExtra))
            }
            watchlist = false
            showToastShort(
              this@DetailMovieActivity,
              getString(R.string.deleted_from_watchlist, dataExtra.title)
            )
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

  private fun showDialogNotRated() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder
      .setTitle(getString(R.string.not_available_full))
      .setMessage(getString(R.string.cant_provide_a_score))

    val dialog: AlertDialog = builder.create()
    dialog.show()
  }

  private fun postDataToTMDB(isModeFavorite: Boolean, state: Boolean) {
    if (isModeFavorite) { // for favorite
      favorite = !state
      val favoriteMode = Favorite(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authenticationViewModel.getUser().observe(this) { user ->
        viewModel.postFavorite(user.token, favoriteMode, user.userId)
      }

    } else { // for watchlist
      watchlist = !state
      val unWatchlistModel = Watchlist(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authenticationViewModel.getUser().observe(this) { user ->
        viewModel.postWatchlist(user.token, unWatchlistModel, user.userId)
      }
    }
  }


  // show score from OMDb API
  private fun showDetailOMDb(data: OMDbDetailsResponse) {
    binding.apply {
      tvScoreImdb.text =
        if (data.imdbRating == "") getString(R.string.not_available) else data.imdbRating
      tvScoreMetascore.text =
        if (data.metascore == "") getString(R.string.not_available) else data.metascore
    }
  }

  private fun isFavorite(isLogin: Boolean) {
    if (isLogin) { //user
      authenticationViewModel.getUser().observe(this) { user ->
        getStated(user.token)
        viewModel.stated().observe(this) {
          favorite = it.favorite!!
          binding.tvScoreYourScore.text = if (it.rated == false) getString(R.string.not_available)
          else it.rated.toString().replace("{value=", "").replace("}", "")
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
          binding.tvScoreYourScore.text = if (it.rated == false) getString(R.string.not_available)
          else it.rated.toString().replace("{value=", "").replace("}", "")
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


  // show toast and snackbar
  private fun showToastAddedFavorite() {
    showToastShort(
      this, this.getString(
        R.string.added_to_favorite,
        dataExtra.name ?: dataExtra.originalTitle ?: dataExtra.title
      )
    )
  }

  private fun showToastAddedWatchlist() {
    showToastShort(
      this, this.getString(
        R.string.added_to_watchlist,
        dataExtra.name ?: dataExtra.originalTitle ?: dataExtra.title
      )
    )
  }

  private fun showToastRemoveFromFavorite() {
    showToastShort(this, this.getString(R.string.deleted_from_favorite, dataExtra.title))
  }

  private fun showToastRemoveFromWatchlist() {
    showToastShort(this, this.getString(R.string.deleted_from_watchlist, dataExtra.title))
  }

  private fun showDialogRate() {
    val dialog = Dialog(this)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.popup_rating)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val buttonYesAlert = dialog.findViewById(R.id.btn_yes) as Button
    buttonYesAlert.setOnClickListener { showToastShort(this, "Yes") }

    val buttonNoAlert = dialog.findViewById(R.id.btn_no) as Button
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
    snackbarView.setBackgroundColor(
      ContextCompat.getColor(
        this,
        R.color.red_matte
      )
    )
    snackBar.show()
  }


  private fun hideTrailer(hide: Boolean) {
    binding.ibPlay.isVisible = !hide
  }

  private fun animFadeOut() {
    val animation = animFadeOutLong(this)
    binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, 600)
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
    textView.typeface = ResourcesCompat.getFont(this, R.font.gothic)
    textView.gravity = Gravity.START
    textView.textSize = 14F
    textView.setPadding(0, 7, 24, 7)
    textView.setTextColor(ActivityCompat.getColor(this, R.color.grey_100))
    return textView
  }


  companion object {
    const val EXTRA_MOVIE = "MOVIE"
  }
}