package com.waffiq.bazz_movies.ui.activity.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
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
import android.view.animation.AccelerateDecelerateInterpolator
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
import com.waffiq.bazz_movies.R.id.btn_cancel
import com.waffiq.bazz_movies.R.id.btn_submit
import com.waffiq.bazz_movies.R.id.rating_bar_action
import com.waffiq.bazz_movies.R.layout.dialog_rating
import com.waffiq.bazz_movies.R.string.cant_provide_a_score
import com.waffiq.bazz_movies.R.string.item_added_to_favorite
import com.waffiq.bazz_movies.R.string.item_added_to_watchlist
import com.waffiq.bazz_movies.R.string.item_deleted_from_favorite
import com.waffiq.bazz_movies.R.string.item_deleted_from_watchlist
import com.waffiq.bazz_movies.R.string.no_overview
import com.waffiq.bazz_movies.R.string.not_available
import com.waffiq.bazz_movies.R.string.not_available_full
import com.waffiq.bazz_movies.R.string.rating_added_successfully
import com.waffiq.bazz_movies.R.string.status_
import com.waffiq.bazz_movies.R.string.yt_not_installed
import com.waffiq.bazz_movies.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.domain.model.ResultItem
import com.waffiq.bazz_movies.domain.model.Stated
import com.waffiq.bazz_movies.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.ui.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelFactory
import com.waffiq.bazz_movies.ui.viewmodelfactory.ViewModelUserFactory
import com.waffiq.bazz_movies.utils.Helper.animFadeOutLong
import com.waffiq.bazz_movies.utils.Helper.dateFormatterStandard
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.utils.common.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.utils.common.Event
import com.waffiq.bazz_movies.utils.helpers.DetailPageHelper.detailCrew
import com.waffiq.bazz_movies.utils.mappers.DatabaseMapper.favFalseWatchlistTrue
import com.waffiq.bazz_movies.utils.mappers.DatabaseMapper.favTrueWatchlistFalse
import com.waffiq.bazz_movies.utils.mappers.DatabaseMapper.favTrueWatchlistTrue

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class DetailMovieActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private lateinit var detailViewModel: DetailMovieViewModel
  private lateinit var userPreferenceViewModel: UserPreferenceViewModel

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item watchlist or not
  private var isLogin = false // is login as user or not

  private var toast: Toast? = null
  private var isBGShowed: Boolean = false // flag to show backgroundDim

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    showLoadingDim(true)

    val factory1 = ViewModelFactory.getInstance(this)
    detailViewModel = ViewModelProvider(this, factory1)[DetailMovieViewModel::class.java]
    detailViewModel.loadingState.observe(this) {
      if (it) isBGShowed = true
      showLoadingDim(it)
    }
    errorStateObserver()

    val factory2 = ViewModelUserFactory.getInstance(dataStore)
    userPreferenceViewModel = ViewModelProvider(this, factory2)[UserPreferenceViewModel::class.java]

    scrollActionBarBehavior()
    loadData()
    favWatchlistHandler()
    viewListener()
  }

  // region SCROLL BEHAVIOR
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
  // endregion SCROLL BEHAVIOR

  private fun favWatchlistHandler() {
    detailViewModel.postModelState.observe(this) { eventResult ->
      eventResult.getContentIfNotHandled()?.let { postModelState ->
        if (postModelState.isSuccess) {
          if (!postModelState.isDelete) {
            if (!postModelState.isFavorite) showToastAddedWatchlist()
            else showToastAddedFavorite()
          } else {
            if (postModelState.isFavorite) showToastRemoveFromFavorite()
            else showToastRemoveFromWatchlist()
          }
        }
      }
    }


  }

  private fun loadData() {
    checkUser()
    getDataExtra()
    showDetailData()
  }

  private fun checkUser() {
    userPreferenceViewModel.getUserPref().observe(this) {
      isLogin = it.token != "NaN"

      // handler for rating, add favorite and watchlist for user login
      if (isLogin) {
        detailViewModel.rateState.observe(this) { eventResult ->
          eventResult.getContentIfNotHandled()?.let { isRateSuccessful ->
            if (isRateSuccessful) showToast(getString(rating_added_successfully))
          }
        }

        userPreferenceViewModel.getUserPref().observe(this) { user ->
          getStated(user.token)
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
        intent.getParcelableExtra(EXTRA_MOVIE, ResultItem::class.java)
          ?: error("No DataExtra")
      } else {
        @Suppress("DEPRECATION")
        intent.getParcelableExtra(EXTRA_MOVIE) ?: error("No DataExtra")
      }
    } else finish()
  }

  private fun showDetailData() {
    // shows backdrop
    Glide.with(binding.ivPictureBackdrop)
      .load(
        if (!dataExtra.backdropPath.isNullOrEmpty()) TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
        else if (!dataExtra.posterPath.isNullOrEmpty()) TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
        else ic_backdrop_error_filled,
      ).placeholder(ic_bazz_placeholder_search)
      .error(ic_backdrop_error_filled)
      .into(binding.ivPictureBackdrop)

    // shows poster
    Glide.with(binding.ivPoster)
      .load(
        if (dataExtra.posterPath != null) TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
        else ic_poster_error
      )
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_poster_error)
      .into(binding.ivPoster)

    if (dataExtra.posterPath.isNullOrEmpty()) binding.tvBackdropNotFound.visibility = View.VISIBLE
    else binding.tvBackdropNotFound.visibility = View.GONE

    // show data (title, released year, media type, and overview)
    binding.apply {
      dataExtra.apply {
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
        tvMediaType.text = mediaType.uppercase()
        tvYearReleased.text =
          dateFormatterStandard(releaseDate.toString().ifEmpty { firstAirDate.toString() })
        tvOverview.text =
          if (!overview.isNullOrEmpty() && overview.isNotBlank()) overview
          else getString(no_overview)
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
      detailViewModel.getMovieCredits(dataExtra.id)
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

      // get detail movie
      userPreferenceViewModel.getUserPref().observe(this) { user ->
        detailViewModel.detailMovie(dataExtra.id, user.region)
      }

      // show TMDb score
      detailViewModel.tmdbScore.observe(this) {
        binding.tvScoreTmdb.text = it.ifEmpty { getString(not_available) }
      }

      detailViewModel.detailMovie.observe(this) { movie ->
        // copy genre id
        dataExtra = dataExtra.copy(listGenreIds = movie.genreId)

        // show genre, duration, age rating, region release
        binding.tvGenre.text = movie.genre
        binding.tvDuration.text = movie.duration
        binding.tvAgeRating.text = movie.ageRating
        if (movie.releaseDateRegion.regionRelease.isNotEmpty())
          binding.tvRegionRelease.text = movie.releaseDateRegion.regionRelease
        else binding.tvRegionRelease.visibility = View.GONE

        // set date release based on user region
        if (movie.releaseDateRegion.releaseDate.isNotEmpty())
          binding.tvYearReleased.text = movie.releaseDateRegion.releaseDate
        else binding.tvYearReleased.visibility = View.GONE

        // show OMDb detail (score)
        if (movie.imdbId != null) {
          detailViewModel.getScoreOMDb(movie.imdbId)
          detailViewModel.omdbResult.observe(this) {
            showDetailOMDb(it)
          }
        } else showLoadingDim(false)

        // trailer
        detailViewModel.getLinkVideoMovie(movie.id)
        detailViewModel.linkVideo.observe(this) {
          if (it.isNullOrEmpty() || it.isBlank()) hideTrailer(true)
          else {
            hideTrailer(false)
            btnTrailer(it)
          }
        }

        // recommendation movie
        detailViewModel.getRecommendationMovie(movie.id).observe(this) {
          adapterRecommendation.submitData(lifecycle, it)
        }
      }


    } else if (dataExtra.mediaType == "tv") {

      // show crew & cast
      detailViewModel.getTvCredits(dataExtra.id)
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

      detailViewModel.externalId(dataExtra.id)
      detailViewModel.tvImdbID.observe(this) { imdb ->
        if (imdb != null && imdb.isNotBlank() && imdb.isNotEmpty()) {
          detailViewModel.getScoreOMDb(imdb)
          detailViewModel.omdbResult.observe(this) {
            showDetailOMDb(it)
          }
        } else {
          binding.tvScoreImdb.text = getString(not_available)
          binding.tvScoreMetascore.text = getString(not_available)
        }

        // trailer
        detailViewModel.getLinkTv(dataExtra.id)
        detailViewModel.linkVideo.observe(this) {
          if (it.isNullOrEmpty() || it.isBlank()) hideTrailer(true)
          else {
            hideTrailer(false)
            btnTrailer(it)
          }
        }
      }

      // recommendation tv
      detailViewModel.getRecommendationTv(dataExtra.id).observe(this) {
        adapterRecommendation.submitData(lifecycle, it)
      }


      // show genres & age rate
      detailViewModel.detailTv(dataExtra.id)
      detailViewModel.detailTv.observe(this) { tv ->
        val temp = tv.listGenres?.map { it?.name }
        val tempID = tv.listGenres?.map { it?.id ?: 0 }
        if (tempID != null) dataExtra = dataExtra.copy(listGenreIds = tempID)
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

    // show region release
    detailViewModel.productionCountry.observe(this) {
      if (it != null) binding.tvRegionRelease.text = it else binding.tvRegionRelease.visibility =
        View.GONE
    }
  }

  private fun getStated(token: String) {
    if (dataExtra.mediaType == "movie") detailViewModel.getStatedMovie(token, dataExtra.id)
    else detailViewModel.getStatedTv(token, dataExtra.id)
  }

  private fun showRatingUserLogin(state: Stated) {
    binding.tvScoreYourScore.text = if (state.rated.toString() == "false") getString(not_available)
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

  private fun viewListener() {
    binding.apply {
      // handle swipe refresh to reload data
      swipeRefresh.setOnRefreshListener {
        loadData()
        swipeRefresh.isRefreshing = false
      }

      btnBack.setOnClickListener { finish() }

      btnFavorite.setOnClickListener {
        if (!isLogin) { // Guest user
          when {
            // if watchlist, then edit is_favorite = true
            !favorite && watchlist ->
              detailViewModel.updateToFavoriteDB(favTrueWatchlistTrue(dataExtra))

            // if not in favorite and watchlist insert into room dataset, then set is_favorite = true
            !favorite && !watchlist ->
              detailViewModel.insertToDB(favTrueWatchlistFalse(dataExtra))

            // if favorite and watchlist, then edit is_favorite to false
            favorite && watchlist ->
              detailViewModel.updateToRemoveFromFavoriteDB(favFalseWatchlistTrue(dataExtra))

            // remove from room database, cuz not in favorite
            else -> detailViewModel.delFromFavoriteDB(favTrueWatchlistFalse(dataExtra))
          }
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = true, state = favorite)
        }
      }

      btnWatchlist.setOnClickListener {
        if (!isLogin) { // Guest user
          when {
            // if on favorite, then update is_watchlist = true
            !watchlist && favorite ->
              detailViewModel.updateToWatchlistDB(favTrueWatchlistTrue(dataExtra))

            // if not in favorite and watchlist, insert item into room database and set is_watchlist = true
            !watchlist && !favorite ->
              detailViewModel.insertToDB(favFalseWatchlistTrue(dataExtra))

            // if watchlist and favorite, then update is_watchlist to false
            watchlist && favorite ->
              detailViewModel.updateToRemoveFromWatchlistDB(favTrueWatchlistFalse(dataExtra))

            // remove from room database, cuz is not favorite
            else -> detailViewModel.delFromFavoriteDB(favFalseWatchlistTrue(dataExtra))
          }
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = false, state = watchlist)
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
      val fav = FavoritePostModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      userPreferenceViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postFavorite(user.token, fav, user.userId)
      }

    } else { // for watchlist
      watchlist = !state
      val wtc = WatchlistPostModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      userPreferenceViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postWatchlist(user.token, wtc, user.userId)
      }
    }
  }


  // show score from OMDb API
  private fun showDetailOMDb(data: OMDbDetails) {
    binding.apply {
      tvScoreImdb.text =
        if (data.imdbRating.isNullOrEmpty() || data.imdbRating.isBlank()) getString(not_available) else data.imdbRating
      tvScoreMetascore.text =
        if (data.metascore.isNullOrEmpty() || data.metascore.isBlank()) getString(not_available) else data.metascore
    }
  }


  // check if favorite or watchlist
  private fun isFavoriteWatchlist(isLogin: Boolean) {
    if (isLogin) { // user
      userPreferenceViewModel.getUserPref().observe(this) { user ->
        getStated(user.token)
        detailViewModel.itemState.observe(this) {
          if (it != null) {
            favorite = it.favorite
            watchlist = it.watchlist
            showRatingUserLogin(it)
            changeBtnFavoriteBG(it.favorite)
            changeBtnWatchlistBG(it.watchlist)
          }
        }
      }
    } else { //guest user
      detailViewModel.isFavoriteDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isFavorite.observe(this) {
        changeBtnFavoriteBG(it)
        favorite = it
      }
      detailViewModel.isWatchlistDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isWatchlist.observe(this) {
        changeBtnWatchlistBG(it)
        watchlist = it
      }
    }
  }


  // favorite & watchlist button transform
  private fun changeBtnWatchlistBG(isActivated: Boolean) {
    if (isActivated && binding.btnWatchlist.drawable.constantState ==
      ActivityCompat.getDrawable(this, ic_bookmark_selected)?.constantState
    ) {
      return
    }

    if (!isActivated && binding.btnWatchlist.drawable.constantState ==
      ActivityCompat.getDrawable(this, ic_bookmark)?.constantState
    ) {
      return
    }

    val toRes = if (isActivated) ic_bookmark_selected else ic_bookmark
    val scaleXAnimator =
      ObjectAnimator.ofFloat(binding.btnWatchlist, View.SCALE_X, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val scaleYAnimator =
      ObjectAnimator.ofFloat(binding.btnWatchlist, View.SCALE_Y, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val alphaAnimator = ObjectAnimator.ofFloat(binding.btnWatchlist, View.ALPHA, 1f, 0f).apply {
      duration = 150
      startDelay = 150 // Delay the alpha animation for smooth transition
      interpolator = AccelerateDecelerateInterpolator()
    }

    val set = AnimatorSet().apply {
      playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    }

    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        binding.btnWatchlist.setImageResource(toRes)
        ObjectAnimator.ofFloat(binding.btnWatchlist, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }

  private fun changeBtnFavoriteBG(isActivated: Boolean) {
    if (isActivated && binding.btnFavorite.drawable.constantState ==
      ActivityCompat.getDrawable(this, ic_hearth_selected)?.constantState
    ) {
      return
    }

    if (!isActivated && binding.btnFavorite.drawable.constantState ==
      ActivityCompat.getDrawable(this, ic_hearth)?.constantState
    ) {
      return
    }

    val toRes = if (isActivated) ic_hearth_selected else ic_hearth
    val scaleXAnimator =
      ObjectAnimator.ofFloat(binding.btnFavorite, View.SCALE_X, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val scaleYAnimator =
      ObjectAnimator.ofFloat(binding.btnFavorite, View.SCALE_Y, 1.0f, 1.2f, 1.0f).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
      }

    val alphaAnimator = ObjectAnimator.ofFloat(binding.btnFavorite, View.ALPHA, 1f, 0f).apply {
      duration = 150
      startDelay = 150 // Delay the alpha animation for smooth transition
      interpolator = AccelerateDecelerateInterpolator()
    }

    val set = AnimatorSet().apply {
      playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    }

    set.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        binding.btnFavorite.setImageResource(toRes)
        ObjectAnimator.ofFloat(binding.btnFavorite, View.ALPHA, 0f, 1f).apply {
          duration = 150
          interpolator = AccelerateDecelerateInterpolator()
        }.start()
      }
    })

    set.start()
  }


  // toast, snackbar, dialog, trailer
  private fun showToastAddedFavorite() {
    showToast(getString(item_added_to_favorite))
  }

  private fun showToastAddedWatchlist() {
    showToast(getString(item_added_to_watchlist))
  }

  private fun showToastRemoveFromFavorite() {
    showToast(getString(item_deleted_from_favorite))
  }

  private fun showToastRemoveFromWatchlist() {
    showToast(getString(item_deleted_from_watchlist))
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
    detailViewModel.itemState.observe(this) {
      if (it?.rated != null && it.rated != false) {
        ratingBar.rating = it.rated
          .toString()
          .replace("{value=", "").replace("}", "")
          .toFloat() / 2
      }
    }

    val btnSubmit = dialogView.findViewById(btn_submit) as Button
    btnSubmit.setOnClickListener {
      val ratePostModel = RatePostModel(value = ratingBar.rating * 2)
      userPreferenceViewModel.getUserPref().observe(this) { user ->
        if (dataExtra.mediaType == "movie")
          detailViewModel.postMovieRate(user.token, ratePostModel, dataExtra.id)
        else detailViewModel.postTvRate(user.token, ratePostModel, dataExtra.id)
      }

      // change rating
      detailViewModel.rateState.observe(this) { eventResult ->
        eventResult.peekContent().let { isRateSuccessful ->
          if (isRateSuccessful) binding.tvScoreYourScore.text = ratePostModel.value.toString()
        }
      }
      dialog.dismiss()
    }

    val btnCancel = dialogView.findViewById(btn_cancel) as Button
    btnCancel.setOnClickListener { dialog.dismiss() }
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
    if (!isBGShowed) binding.backgroundDimMovie.startAnimation(animation)
    binding.progressBar.startAnimation(animation)

    Handler(Looper.getMainLooper()).postDelayed({
      binding.backgroundDimMovie.visibility = View.GONE
      binding.progressBar.visibility = View.GONE
    }, DELAY_TIME)
  }

  private fun showLoadingDim(isLoading: Boolean) {
    if (isLoading) {
      if (!isBGShowed) binding.backgroundDimMovie.visibility = View.VISIBLE
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.appBarLayout.visibility = View.VISIBLE
      animFadeOut()
    }
  }

  private fun errorStateObserver() {
    detailViewModel.errorState.observe(this) { showSnackBarWarning(it) }
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