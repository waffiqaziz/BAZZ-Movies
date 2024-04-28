package com.waffiq.bazz_movies.ui.activity.detail

import android.animation.ArgbEvaluator
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.R.color.gray
import com.waffiq.bazz_movies.R.color.gray_100
import com.waffiq.bazz_movies.R.color.red_matte
import com.waffiq.bazz_movies.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.R.drawable.ic_bookmark_selected
import com.waffiq.bazz_movies.R.drawable.ic_hearth
import com.waffiq.bazz_movies.R.drawable.ic_hearth_selected
import com.waffiq.bazz_movies.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.R.font.nunito_sans_regular
import com.waffiq.bazz_movies.R.id.btn_no
import com.waffiq.bazz_movies.R.id.btn_yes
import com.waffiq.bazz_movies.R.id.rating_bar_action
import com.waffiq.bazz_movies.R.layout.dialog_rating
import com.waffiq.bazz_movies.R.string.added_to_favorite
import com.waffiq.bazz_movies.R.string.added_to_watchlist
import com.waffiq.bazz_movies.R.string.cant_provide_a_score
import com.waffiq.bazz_movies.R.string.deleted_from_favorite
import com.waffiq.bazz_movies.R.string.deleted_from_watchlist
import com.waffiq.bazz_movies.R.string.no_overview
import com.waffiq.bazz_movies.R.string.not_available
import com.waffiq.bazz_movies.R.string.not_available_full
import com.waffiq.bazz_movies.R.string.rating_added_successfully
import com.waffiq.bazz_movies.R.string.status_
import com.waffiq.bazz_movies.R.string.unknown_error
import com.waffiq.bazz_movies.R.string.yt_not_installed
import com.waffiq.bazz_movies.data.remote.Favorite
import com.waffiq.bazz_movies.data.remote.Rate
import com.waffiq.bazz_movies.data.remote.Watchlist
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
import com.waffiq.bazz_movies.utils.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.utils.DataMapper.favFalseWatchlistFalse
import com.waffiq.bazz_movies.utils.DataMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.utils.DataMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.utils.DataMapper.favTrueWatchlistTrue
import com.waffiq.bazz_movies.utils.Event
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.convertRuntime
import com.waffiq.bazz_movies.utils.Helper.dateFormatter
import com.waffiq.bazz_movies.utils.Helper.detailCrew
import com.waffiq.bazz_movies.utils.LocalResult
import com.waffiq.bazz_movies.utils.Status

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var detailViewModel: DetailMovieViewModel
  private lateinit var authViewModel: AuthenticationViewModel

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item favorite or not
  private var isLogin = false // is login as user or not

  private var toast: Toast? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    showLoadingDim(true)

    val factory1 = ViewModelFactory.getInstance(this)
    detailViewModel = ViewModelProvider(this, factory1)[DetailMovieViewModel::class.java]
    detailViewModel.loadingState.observe(this) { showLoadingDim(it) }

    val factory2 = ViewModelUserFactory.getInstance(dataStore)
    authViewModel = ViewModelProvider(this, factory2)[AuthenticationViewModel::class.java]

    scrollActionBarBehavior()
    checkUser()
    getDataExtra()
    showDetailData()
    btnListener()
  }

  private fun scrollActionBarBehavior() {
    val fromColor = ContextCompat.getColor(this, android.R.color.transparent)
    val toColor = ContextCompat.getColor(this, gray)
    binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
      val maxScroll =
        binding.nestedScrollView.getChildAt(0).height - binding.nestedScrollView.height
      animateColorChange(
        binding.appBarLayout,
        fromColor,
        toColor,
        percentage = scrollY.toFloat() / maxScroll.toFloat()
      )
    })
  }

  private fun animateColorChange(
    appBarLayout: AppBarLayout, fromColor: Int, toColor: Int, percentage: Float
  ) {
    // Calculate the adjusted progress based on the percentage scrolled
    val adjustedProgress = percentage.coerceIn(0f, 1f) // Ensure the progress is between 0 and 1

    // Calculate the interpolated color based on the adjusted progress
    val interpolatedColor = ArgbEvaluator().evaluate(adjustedProgress, fromColor, toColor) as Int

    // Set the interpolated color as the background color of the AppBarLayout
    appBarLayout.setBackgroundColor(interpolatedColor)
  }

  private fun checkUser() {
    authViewModel.getUserPref().observe(this) {
      isLogin = it.token != "NaN"

      // rate handling, add favorite and watchlist for user login
      if (isLogin) {
        detailViewModel.rateState.observe(this) { eventResult ->
          eventResult.getContentIfNotHandled()?.let { isRateSuccessful ->
            if (isRateSuccessful) showToast(getString(rating_added_successfully))
          }
        }
        detailViewModel.postModelState.observe(this) { eventResult ->
          eventResult.getContentIfNotHandled()?.let { postModelState ->

            if (postModelState.isSuccess) {
              if (!postModelState.isDelete) { // adding handle
                if (postModelState.isWatchlist) showToastAddedWatchlist()
                else showToastAddedFavorite()
              } else { // remove handle
                if (postModelState.isFavorite) showToastRemoveFromFavorite()
                else showToastRemoveFromWatchlist()
              }

              authViewModel.getUserPref().observe(this) { user ->
                getStated(user.token)
              }
            }
          }
        }
      }

      // shor or hide user score
      binding.yourScoreViewGroup.isGone = !isLogin

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
    // error handling
    detailViewModel.errorState.observe(this) { showSnackBarWarning(it) }

    // shows backdrop
    Glide.with(binding.ivPicture).load(
      if (dataExtra.backdropPath.isNullOrEmpty()) TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
      else TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
    ).placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error_filled)
      .into(binding.ivPicture)

    //shows poster
    Glide.with(binding.ivPoster)
      .load(TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath) // URL movie poster
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_poster_error)
      .into(binding.ivPoster)
    if (dataExtra.posterPath.isNullOrEmpty()) binding.tvBackdropNotFound.visibility = View.VISIBLE
    else binding.tvBackdropNotFound.visibility = View.GONE

    // show data(year, overview, title)
    binding.apply {
      dataExtra.apply {
        val year = dateFormatter((firstAirDate ?: releaseDate ?: "")) ?: ""
        if (year.isEmpty()) {
          detailViewModel.detailMovie.observe(this@DetailMovieActivity) {
            tvYearReleased.text = it.status ?: getString(not_available)
          }
        }

        tvMediaType.text = mediaType?.uppercase()
        if (overview != null) {
          if (overview.isEmpty() || overview.isBlank()) tvOverview.text = getString(no_overview)
          else tvOverview.text = overview
        } else tvOverview.text = getString(no_overview)
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
      }
    }

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

      // shows crew and cast
      dataExtra.id?.let { detailViewModel.getMovieCredits(it) }
      detailViewModel.movieTvCreditsResult.observe(this) {
        createTable(detailCrew(it.crew))
        adapterCast.setCast(it.cast)
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
      detailViewModel.detailMovie.observe(this) { movie ->

        // show genre
        val temp = movie.genres?.map { it?.name }
        val tempID = movie.genres?.map { it?.id ?: 0 }
        if (tempID != null) dataExtra = dataExtra.copy(genreIds = tempID)
        if (temp != null) binding.tvGenre.text = temp.joinToString(separator = ", ")

        // show runtime
        binding.tvDuration.text = movie.runtime?.let { convertRuntime(it) }

        // show TMDb score
        binding.tvScoreTmdb.text =
          if (movie.voteAverage == 0.0
            || movie.voteAverage == null
            || movie.voteAverage.toString().isEmpty()
            || movie.voteAverage.toString().isBlank()
          ) getString(not_available)
          else movie.voteAverage.toString()

        // show OMDb detail (score)
        if (movie.imdbId != null) {
          detailViewModel.getScoreOMDb(movie.imdbId)
          detailViewModel.omdbResult.observe(this) {
            showDetailOMDb(it)
          }
        } else showLoadingDim(false)

        // trailer
        movie.id?.let { detailViewModel.getLinkMovie(it) }
        detailViewModel.linkVideo.observe(this) {
          if (it.isNullOrEmpty() || it.isBlank()) hideTrailer(true)
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
        detailViewModel.ageRating.observe(this) {
          binding.tvAgeRating.text =
            if (it.isNotEmpty() && it.isNotBlank()) it else getString(not_available)
        }
      }


    } else if (dataExtra.mediaType == "tv") {

      // show crew & cast
      dataExtra.id?.let { detailViewModel.getTvCredits(it) }
      detailViewModel.movieTvCreditsResult.observe(this) {
        createTable(detailCrew(it.crew))
        adapterCast.setCast(it.cast)
        if (adapterCast.itemCount <= 0) {
          binding.rvCast.isVisible = false
          binding.tvCastHeader.isVisible = false
        } else {
          binding.rvCast.isVisible = true
          binding.tvCastHeader.isVisible = true
        }
      }

      dataExtra.id?.let { detailViewModel.externalId(it) }
      detailViewModel.externalTvId.observe(this) { response ->
        when (response.status) {
          Status.SUCCESS -> {
            if (response.data?.imdbId != null) {
              detailViewModel.getScoreOMDb(response.data.imdbId)
              detailViewModel.omdbResult.observe(this) {
                showDetailOMDb(it)
              }
            } else showLoadingDim(false)
          }

          Status.ERROR -> {
            showSnackBarWarning(Event(response.message ?: getString(unknown_error)))
            binding.tvScoreImdb.text = getString(not_available)
            binding.tvScoreMetascore.text = getString(not_available)
          }

          Status.LOADING -> {}
        }

        // trailer
        dataExtra.id?.let { detailViewModel.getLinkTv(it) }
        detailViewModel.linkVideo.observe(this) {
          if (it.isNullOrEmpty() || it.isBlank()) hideTrailer(true)
          else {
            hideTrailer(false)
            btnTrailer(it)
          }
        }
      }

      // recommendation tv
      dataExtra.id?.let { mediaId ->
        detailViewModel.getRecommendationTv(mediaId).observe(this) {
          adapterRecommendation.submitData(lifecycle, it)
        }
      }

      // show genres & age rate
      dataExtra.id?.let { detailViewModel.detailTv(it) }
      detailViewModel.detailTv.observe(this) { tv ->
        val temp = tv.genres?.map { it?.name }
        val tempID = tv.genres?.map { it?.id ?: 0 }
        if (tempID != null) dataExtra = dataExtra.copy(genreIds = tempID)
        if (!temp.isNullOrEmpty()) binding.tvGenre.text = temp.joinToString(separator = ", ")
        else binding.tvGenre.text = getString(not_available)

        // tmdb score
        binding.tvScoreTmdb.text =
          if (tv.voteAverage == 0.0 || tv.voteAverage == null) getString(not_available)
          else tv.voteAverage.toString()

        // show runtime
        binding.tvDuration.text = getString(status_, tv.status)

        detailViewModel.ageRating.observe(this) {
          binding.tvAgeRating.text =
            if (it.isNotEmpty() && it.isNotBlank()) it else getString(not_available)
        }
      }
    }

    // show production country
    detailViewModel.productionCountry.observe(this)
    {
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
        intent.data = Uri.parse("$YOUTUBE_LINK_VIDEO$link")
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
    insertDBObserver()
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
          if (favorite) postDataToTMDB(isModeFavorite = true, state = true)
          else postDataToTMDB(isModeFavorite = true, state = false)
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
          if (watchlist) postDataToTMDB(isModeFavorite = false, state = true)
          else postDataToTMDB(isModeFavorite = false, state = false)
        }
      }

      yourScoreViewGroup.setOnClickListener { showDialogRate() }

      imdbViewGroup.setOnClickListener { if (!tvScoreImdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      tmdbViewGroup.setOnClickListener { if (!tvScoreTmdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      metascoreViewGroup.setOnClickListener { if (!tvScoreMetascore.text.contains("[0-9]".toRegex())) showDialogNotRated() }
    }
  }

  private fun insertDBObserver() {
    detailViewModel.localResult.observe(this) {
      it.getContentIfNotHandled()?.let { result ->
        when (result) {
          is LocalResult.Error -> showToast(result.message)
          else -> {}
        }
      }
    }
  }

  private fun postDataToTMDB(isModeFavorite: Boolean, state: Boolean) {
    if (isModeFavorite) { // for favorite
      favorite = !state
      val fav = Favorite(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postFavorite(user.token, fav, user.userId)
      }

    } else { // for watchlist
      watchlist = !state
      val wtc = Watchlist(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      authViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postWatchlist(user.token, wtc, user.userId)
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
      authViewModel.getUserPref().observe(this) { user ->
        getStated(user.token)
        detailViewModel.stated.observe(this) {
          if (it != null) {
            favorite = it.favorite
            watchlist = it.watchlist
            showRatingUserLogin(it)
          }
          changeBtnFavoriteBG(it.favorite)
          changeBtnWatchlistBG(it.watchlist)
        }
      }
    } else { //guest user
      dataExtra.id?.let { detailViewModel.isFavoriteDB(it, dataExtra.mediaType.toString()) }
      detailViewModel.isFavorite.observe(this) {
        changeBtnFavoriteBG(it)
        favorite = it
      }
      dataExtra.id?.let { detailViewModel.isWatchlistDB(it, dataExtra.mediaType.toString()) }
      detailViewModel.isWatchlist.observe(this) {
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
    showToast(
      "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        added_to_favorite
      )
    )
  }

  private fun showToastAddedWatchlist() {
    showToast(
      "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        added_to_watchlist
      )
    )
  }

  private fun showToastRemoveFromFavorite() {
    showToast(
      "<b>${dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.name}</b> " + getString(
        deleted_from_favorite
      )
    )
  }

  private fun showToastRemoveFromWatchlist() {
    showToast(
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

    authViewModel.getUserPref().observe(this) { user ->
      getStated(user.token)
      detailViewModel.stated.observe(this) {
        if (it != null) {
          ratingBar.rating = it.rated
            .toString()
            .replace("{value=", "").replace("}", "")
            .toFloat() / 2
        }
      }
    }

    val buttonYesAlert = dialogView.findViewById(btn_yes) as Button
    buttonYesAlert.setOnClickListener {
      val rate = Rate(value = ratingBar.rating * 2)
      authViewModel.getUserPref().observe(this) { user ->
        if (dataExtra.mediaType.equals("movie"))
          dataExtra.id?.let { it1 -> detailViewModel.postMovieRate(user.token, rate, it1) }
        else dataExtra.id?.let { it1 -> detailViewModel.postTvRate(user.token, rate, it1) }
      }

      // change rating
      detailViewModel.rateState.observe(this) { eventResult ->
        eventResult.peekContent().let { isRateSuccessful ->
          if (isRateSuccessful) binding.tvScoreYourScore.text = rate.value.toString()
        }
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

  private fun showLoadingDim(isLoading: Boolean) {
    if (isLoading) {
      binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.appBarLayout.visibility = View.VISIBLE
      animFadeOut()
    }
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
    textView.typeface = ResourcesCompat.getFont(this, nunito_sans_regular)
    textView.gravity = Gravity.START
    textView.textSize = 14F
    textView.setPadding(0, 7, 24, 7)
    textView.setTextColor(ActivityCompat.getColor(this, gray_100))
    return textView
  }

  private fun showToast(text: String) {
    // cancel toast is there's available
    toast?.cancel()

    toast = Toast.makeText(
      this, HtmlCompat.fromHtml(
        text, HtmlCompat.FROM_HTML_MODE_LEGACY
      ), Toast.LENGTH_SHORT
    )
    toast?.show()
  }

  override fun onDestroy() {
    super.onDestroy()
    toast?.cancel()
  }

  companion object {
    const val EXTRA_MOVIE = "MOVIE"
    const val DELAY_TIME = 600L
  }
}