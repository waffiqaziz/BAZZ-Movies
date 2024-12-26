package com.waffiq.bazz_movies.feature.detail.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
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
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_LONG
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NAN
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.core.data.ResultItem
import com.waffiq.bazz_movies.core.data.Stated
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_backdrops
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bookmark
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_hearth
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.cant_provide_a_score
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_added_to_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_favorite
import com.waffiq.bazz_movies.core.designsystem.R.string.item_removed_from_watchlist
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available_full
import com.waffiq.bazz_movies.core.designsystem.R.string.rating_added_successfully
import com.waffiq.bazz_movies.core.designsystem.R.string.status_
import com.waffiq.bazz_movies.core.designsystem.R.string.unknown_error
import com.waffiq.bazz_movies.core.designsystem.R.string.yt_not_installed
import com.waffiq.bazz_movies.core.designsystem.R.style.CustomAlertDialogTheme
import com.waffiq.bazz_movies.core.domain.FavoriteModel
import com.waffiq.bazz_movies.core.domain.WatchlistModel
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.handleOverHeightAppBar
import com.waffiq.bazz_movies.core.uihelper.utils.ActionBarBehavior.transparentStatusBar
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeOut
import com.waffiq.bazz_movies.core.uihelper.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.core.uihelper.utils.GestureHelper.addPaddingWhenNavigationEnable
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.justifyTextView
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.feature.detail.R.id.btn_cancel
import com.waffiq.bazz_movies.feature.detail.R.id.btn_submit
import com.waffiq.bazz_movies.feature.detail.R.id.rating_bar_action
import com.waffiq.bazz_movies.feature.detail.R.layout.dialog_rating
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.DetailMovieTvUsed
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.RecommendationAdapter
import com.waffiq.bazz_movies.feature.detail.utils.helpers.CreateTableViewHelper.createTable
import com.waffiq.bazz_movies.feature.detail.utils.helpers.DetailMovieTvHelper.detailCrew
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnFavoriteBG
import com.waffiq.bazz_movies.feature.detail.utils.uihelpers.ButtonImageChanger.changeBtnWatchlistBG
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.text.isBlank
import kotlin.text.isEmpty

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: ActivityDetailMovieBinding

  private lateinit var dataExtra: ResultItem

  private val detailViewModel: DetailMovieViewModel by viewModels()
  private val prefViewModel: DetailUserPrefViewModel by viewModels()

  private lateinit var adapterCast: CastAdapter
  private lateinit var adapterRecommendation: RecommendationAdapter

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item watchlist or not
  private var isLogin = false // is login as user or not

  private var mSnackbar: Snackbar? = null
  private var toast: Toast? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // action bar behavior
    window.transparentStatusBar()
    binding.appBarLayout.handleOverHeightAppBar()
    scrollActionBarBehavior(window, binding.appBarLayout, binding.nestedScrollView)
    addPaddingWhenNavigationEnable(binding.root)

    justifyTextView(binding.tvOverview as TextView)

    showLoadingDim(true)
    setupRecyclerView()
    stateObserver()
    initTag()

    checkUser()
    getDataExtra()
    showDetailData()
    favWatchlistHandler()
    viewListener()
  }

  private fun showLoadingDim(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.isVisible = true
    } else {
      binding.appBarLayout.isVisible = true
      binding.progressBar.isVisible = false
      fadeOut(binding.backgroundDimMovie, DEBOUNCE_SHORT)
    }
  }

  private fun setupRecyclerView() {
    setupRecyclerViewsWithSnap(
      listOf(binding.rvCast, binding.rvRecommendation)
    )

    // setup adapter
    adapterCast = CastAdapter(navigator)
    adapterRecommendation = RecommendationAdapter(navigator)

    // setup rv cast
    binding.rvCast.itemAnimator = DefaultItemAnimator()
    binding.rvCast.adapter = adapterCast

    // setup rv recommendation
    binding.rvRecommendation.itemAnimator = DefaultItemAnimator()
    binding.rvRecommendation.adapter = adapterRecommendation.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterRecommendation.retry() }
    )
  }

  private fun initTag() {
    binding.btnFavorite.tag = ic_hearth
    binding.btnWatchlist.tag = ic_bookmark
  }

  private fun favWatchlistHandler() {
    detailViewModel.postModelState.observe(this) { eventResult ->
      eventResult.getContentIfNotHandled()?.let { postModelState ->
        if (!postModelState.isSuccess) return@let

        val messageResId = when {
          postModelState.isDelete && postModelState.isFavorite -> item_removed_from_favorite
          postModelState.isDelete -> item_removed_from_watchlist
          postModelState.isFavorite -> item_added_to_favorite
          else -> item_added_to_watchlist
        }

        showToast(getString(messageResId))
      }
    }
  }

  private fun checkUser() {
    prefViewModel.getUserToken().observe(this) { token ->
      isLogin = token != NAN && token.isNotEmpty()

      // handler for rating, add favorite and watchlist for user login
      if (isLogin) {
        detailViewModel.rateState.observe(this) { eventResult ->
          eventResult.getContentIfNotHandled()?.let { isRateSuccessful ->
            if (isRateSuccessful) showToast(getString(rating_added_successfully))
          }
        }
        getStated(token)
        binding.yourScoreViewGroup.setOnClickListener { showDialogRate() }
      }

      // shor or hide user score
      binding.yourScoreViewGroup.isVisible = isLogin

      // check movie or tv is favorite watchlist
      isFavoriteWatchlist(isLogin)
    }
  }

  private fun getDataExtra() {
    if (!intent.hasExtra(EXTRA_MOVIE)) { // check if intent hasExtra for early return
      finish()
      return
    }

    dataExtra = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      intent.getParcelableExtra(EXTRA_MOVIE, ResultItem::class.java)
    } else {
      @Suppress("DEPRECATION")
      intent.getParcelableExtra(EXTRA_MOVIE)
    } ?: error("No DataExtra")
  }

  private fun showDetailData() {
    showGeneralInfo()
    showRecommendation()
    getDetailBasedMediaType()

    // show detail data based media type
    if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) observeDetailMovie()
    else if (dataExtra.mediaType == TV_MEDIA_TYPE) observeDetailTv()
  }

  private fun getDetailBasedMediaType() {
    if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) getDetailMovie()
    else if (dataExtra.mediaType == TV_MEDIA_TYPE) getDetailTv()
  }

  // show data (backdrop, poster, title, released year, media type, and overview) based @params DATA_EXTRA
  private fun showGeneralInfo() {
    showBackdropAndPoster()
    binding.apply {
      dataExtra.apply {
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
        tvMediaType.text = mediaType.uppercase()
        tvYearReleased.text =
          dateFormatterStandard(releaseDate.toString().ifEmpty { firstAirDate.toString() })
        tvOverview.text = overview?.takeIf { it.isNotBlank() } ?: getString(no_overview)
      }
    }
  }

  private fun showBackdropAndPoster() {
    Glide.with(binding.ivPictureBackdrop)
      .load(
        if (dataExtra.backdropPath == NOT_AVAILABLE || dataExtra.posterPath == NOT_AVAILABLE) {
          ic_backdrop_error_filled
        } else if (!dataExtra.backdropPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_BACKDROP_W780 + dataExtra.backdropPath
        } else if (!dataExtra.posterPath.isNullOrEmpty()) {
          TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
        } else {
          ic_backdrop_error_filled
        }
      )
      .placeholder(ic_bazz_placeholder_backdrops)
      .centerCrop()
      .error(ic_backdrop_error_filled)
      .transition(withCrossFade())
      .into(binding.ivPictureBackdrop)

    binding.tvBackdropNotFound.isVisible =
      dataExtra.backdropPath.isNullOrEmpty() || dataExtra.backdropPath == NOT_AVAILABLE

    Glide.with(binding.ivPoster)
      .load(
        if (dataExtra.posterPath == NOT_AVAILABLE) {
          ic_poster_error
        } else if (dataExtra.posterPath != null) {
          TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
        } else {
          ic_poster_error
        }
      )
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_poster_error)
      .transition(withCrossFade())
      .into(binding.ivPoster)
  }

  // region MOVIE
  private fun getDetailMovie() {
    // shows crew and cast
    detailViewModel.getMovieCredits(dataExtra.id)

    // get detail movie
    prefViewModel.getUserRegion().observe(this) { region ->
      detailViewModel.detailMovie(dataExtra.id, region)
    }

    detailViewModel.detailMovieTv.observe(this) { movie ->
      // copy genre id
      dataExtra = dataExtra.copy(listGenreIds = movie.genreId)

      // update UI for movie details
      updateMovieDetailsUI(movie)

      // Trigger fetching of other data
      movie.imdbId?.let {
        if (it.isNotEmpty()) {
          detailViewModel.getScoreOMDb(it)
        }
      }.run {
        showLoadingDim(false)
      }
      detailViewModel.getLinkVideoMovie(movie.id)
      detailViewModel.getRecommendationMovie(movie.id)
    }
  }

  private fun updateMovieDetailsUI(movie: DetailMovieTvUsed) {
    // show genre, duration, tmdb score
    binding.tvGenre.text = movie.genre ?: getString(not_available)
    binding.tvDuration.text = movie.duration ?: getString(not_available)
    binding.tvScoreTmdb.text = movie.tmdbScore ?: getString(not_available)

    // set age rating
    if (!movie.ageRating.isNullOrEmpty()) {
      showViewAgeRating(true)
      binding.tvAgeRating.text = movie.ageRating
    } else {
      showViewAgeRating(false)
    }

    // set region release
    if (movie.releaseDateRegion.regionRelease.isNotEmpty()) {
      showViewRegionReleaseDate(true)
      binding.tvRegionRelease.text = movie.releaseDateRegion.regionRelease
    } else {
      showViewRegionReleaseDate(false)
    }

    // set date release based on user region
    if (movie.releaseDateRegion.releaseDate.isNotEmpty()) {
      showViewReleaseDate(true)
      binding.tvYearReleased.text = movie.releaseDateRegion.releaseDate
    } else {
      showViewReleaseDate(false)
    }
  }

  private fun observeDetailMovie() {
    detailViewModel.movieTvCreditsResult.observe(this) {
      createTable(this, detailCrew(it.crew), binding.table)
      adapterCast.setCast(it.cast)
      if (adapterCast.itemCount <= 0) {
        binding.rvCast.isVisible = false
        binding.tvCastHeader.isVisible = false
      } else {
        binding.rvCast.isVisible = true
        binding.tvCastHeader.isVisible = true
      }
    }
    detailViewModel.omdbResult.observe(this) { omdbScore ->
      showDetailOMDb(omdbScore)
    }

    detailViewModel.linkVideo.observe(this) { videoLink ->
      if (videoLink.isNullOrEmpty() || videoLink.isBlank()) {
        hideTrailer(true)
      } else {
        hideTrailer(false)
        btnTrailer(videoLink)
      }
    }

    detailViewModel.recommendation.observe(this) { recommendations ->
      adapterRecommendation.submitData(lifecycle, recommendations)
    }
  }
  // endregion MOVIE

  // region TV
  private fun getDetailTv() {
    // show crew & cast
    detailViewModel.getTvCredits(dataExtra.id)

    // video
    detailViewModel.getImdbVideoTv(dataExtra.id)

    // recommendation tv
    detailViewModel.getRecommendationTv(dataExtra.id).observe(this) {
      adapterRecommendation.submitData(lifecycle, it)
    }

    // show genres & age rate
    prefViewModel.getUserRegion().observe(this) { region ->
      detailViewModel.detailTv(dataExtra.id, region)
    }
    detailViewModel.detailMovieTv.observe(this) { tv ->

      // genre and tmdb score
      binding.tvGenre.text = tv.genre ?: getString(not_available)
      binding.tvScoreTmdb.text = tv.tmdbScore ?: getString(not_available)

      // status
      binding.tvDuration.text =
        if (tv.duration.isNullOrEmpty()) {
          getString(not_available)
        } else {
          getString(status_, tv.duration)
        }

      // age rating
      if (!tv.ageRating.isNullOrEmpty()) {
        showViewAgeRating(true)
        binding.tvAgeRating.text = tv.ageRating
      } else {
        showViewAgeRating(false)
      }

      // set origin country
      if (tv.releaseDateRegion.regionRelease.isNotEmpty()) {
        showViewRegionReleaseDate(true)
        binding.tvRegionRelease.text = tv.releaseDateRegion.regionRelease
      } else {
        showViewRegionReleaseDate(false)
      }

      // release date
      if (tv.releaseDateRegion.releaseDate.isNotEmpty()) {
        showViewReleaseDate(true)
        binding.tvYearReleased.text = tv.releaseDateRegion.releaseDate
      } else {
        showViewReleaseDate(false)
      }
    }
  }

  private fun observeDetailTv() {
    detailViewModel.movieTvCreditsResult.observe(this) {
      createTable(this, detailCrew(it.crew), binding.table)
      adapterCast.setCast(it.cast)
      if (adapterCast.itemCount <= 0) {
        binding.rvCast.isVisible = false
        binding.tvCastHeader.isVisible = false
      } else {
        binding.rvCast.isVisible = true
        binding.tvCastHeader.isVisible = true
      }
    }

    detailViewModel.tvImdbID.observe(this) { imdb ->
      if (imdb == null || imdb.isBlank() || imdb.isEmpty()) {
        showLoadingDim(false)
        binding.tvScoreImdb.text = getString(not_available)
        binding.tvScoreMetascore.text = getString(not_available)
      }
    }

    detailViewModel.omdbResult.observe(this) {
      showDetailOMDb(it)
    }

    detailViewModel.linkVideo.observe(this) {
      if (it.isNullOrEmpty() || it.isBlank()) {
        hideTrailer(true)
      } else {
        hideTrailer(false)
        btnTrailer(it)
      }
    }
  }
  // endregion TV

  private fun getStated(token: String) {
    if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) {
      detailViewModel.getStatedMovie(token, dataExtra.id)
    } else {
      detailViewModel.getStatedTv(token, dataExtra.id)
    }
  }

  private fun showRatingUserLogin(state: Stated) {
    binding.tvScoreYourScore.text = if (state.rated.toString() == "false") {
      getString(not_available)
    } else {
      state.rated.toString().replace("{value=", "").replace("}", "")
    }
  }

  private fun btnTrailer(link: String) {
    binding.ibPlay.setOnClickListener {
      try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("$YOUTUBE_LINK_VIDEO$link")
        startActivity(intent)
      } catch (e: ActivityNotFoundException) {
        Log.e(TAG, "YouTube app not installed", e)
        snackBarWarning(
          binding.coordinatorLayout,
          null,
          getString(yt_not_installed)
        )
      } catch (e: Exception) {
        Log.e(TAG, "Unknown error occurred while trying to play video", e)
        snackBarWarning(
          binding.coordinatorLayout,
          null,
          getString(unknown_error)
        )
      }
    }
  }

  private fun viewListener() {
    binding.apply {
      // handle swipe refresh to reload data
      swipeRefresh.setOnRefreshListener {
        checkUser()
        getDetailBasedMediaType()
        swipeRefresh.isRefreshing = false
      }

      btnBack.setOnClickListener { finish() }

      btnFavorite.setOnClickListener {
        mSnackbar?.dismiss()
        if (!isLogin) { // Guest user
          detailViewModel.handleBtnFavorite(favorite, watchlist, dataExtra)
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = true, state = favorite)
        }
      }

      btnWatchlist.setOnClickListener {
        mSnackbar?.dismiss()
        if (!isLogin) { // Guest user
          detailViewModel.handleBtnWatchlist(favorite, watchlist, dataExtra)
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = false, state = watchlist)
        }
      }

      imdbViewGroup.setOnClickListener { showDialogIfNotRated(tvScoreImdb.text.toString()) }
      tmdbViewGroup.setOnClickListener { showDialogIfNotRated(tvScoreTmdb.text.toString()) }
      metascoreViewGroup.setOnClickListener { showDialogIfNotRated(tvScoreMetascore.text.toString()) }
    }
  }

  private val showDialogIfNotRated: (String) -> Unit = { scoreText ->
    if (!scoreText.contains("[0-9]".toRegex())) showDialogNotRated()
  }

  private fun postDataToTMDB(isModeFavorite: Boolean, state: Boolean) {
    if (isModeFavorite) { // for favorite
      favorite = !state
      val fav = FavoriteModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      prefViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postFavorite(user.token, fav, user.userId)
      }
    } else { // for watchlist
      watchlist = !state
      val wtc = WatchlistModel(
        dataExtra.mediaType,
        dataExtra.id,
        !state
      )
      prefViewModel.getUserPref().observe(this) { user ->
        detailViewModel.postWatchlist(user.token, wtc, user.userId)
      }
    }
  }

  // show score from OMDb API
  private fun showDetailOMDb(data: OMDbDetails) {
    binding.apply {
      tvScoreImdb.text = data.imdbRating?.takeIf { it.isNotBlank() } ?: getString(not_available)
      tvScoreMetascore.text = data.metascore?.takeIf { it.isNotBlank() } ?: getString(not_available)
    }
  }

  // check if favorite or watchlist
  private fun isFavoriteWatchlist(isLogin: Boolean) {
    if (isLogin) { // user
      prefViewModel.getUserToken().observe(this) { token ->
        getStated(token)
      }
      detailViewModel.itemState.observe(this) {
        if (it != null) {
          favorite = it.favorite
          watchlist = it.watchlist
          showRatingUserLogin(it)
          changeBtnFavoriteBG(binding.btnFavorite, it.favorite)
          changeBtnWatchlistBG(binding.btnWatchlist, it.watchlist)
        }
      }
    } else { // guest user
      detailViewModel.isFavoriteDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isFavorite.observe(this) {
        changeBtnFavoriteBG(binding.btnFavorite, it)
        favorite = it
      }
      detailViewModel.isWatchlistDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isWatchlist.observe(this) {
        changeBtnWatchlistBG(binding.btnWatchlist, it)
        watchlist = it
      }
    }
  }

  // region toast, snackbar, dialog, states
  private fun showDialogNotRated() {
    MaterialAlertDialogBuilder(this, CustomAlertDialogTheme).apply {
      setTitle(resources.getString(not_available_full))
      setMessage(resources.getString(cant_provide_a_score))
    }.show().also { dialog ->
      // Ensure dialog is shown if the activity is not finishing
      if (this.isFinishing) {
        dialog.cancel()
      }
    }
  }

  private fun showDialogRate() {
    val dialog = Dialog(this)
    val dialogView = View.inflate(this, dialog_rating, null)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(dialogView)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val ratingBar = dialogView.findViewById<RatingBar>(rating_bar_action)
    ratingBar.rating = binding.tvScoreYourScore.text.toString().toFloat() / 2

    val btnSubmit: Button = dialogView.findViewById(btn_submit)
    btnSubmit.setOnClickListener {
      val rating: Float = ratingBar.rating * 2
      prefViewModel.getUserToken().observe(this) { token ->
        if (dataExtra.mediaType == MOVIE_MEDIA_TYPE) {
          detailViewModel.postMovieRate(token, rating, dataExtra.id)
        } else {
          detailViewModel.postTvRate(token, rating, dataExtra.id)
        }
      }

      // change rating
      detailViewModel.rateState.observe(this) { eventResult ->
        eventResult.peekContent().let { isRateSuccessful ->
          if (isRateSuccessful) {
            binding.tvScoreYourScore.text = String.format(Locale.getDefault(), "%.1f", rating)
          }
        }
      }
      dialog.dismiss()
    }

    val btnCancel: Button = dialogView.findViewById(btn_cancel)
    btnCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }

  @OptIn(FlowPreview::class) // Allows using the debounce function
  private fun stateObserver() {
    detailViewModel.loadingState.observe(this) { showLoadingDim(it) }
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        detailViewModel.errorState.debounce(DEBOUNCE_LONG) // Prevent multiple emissions within 500ms
          .collect { errorMessage ->
            mSnackbar =
              snackBarWarning(binding.coordinatorLayout, null, errorMessage)
          }
      }
    }
  }

  private fun showToast(text: String) {
    // cancel toast is there's available
    toast?.cancel()

    toast = Toast.makeText(
      applicationContext,
      HtmlCompat.fromHtml(
        text, HtmlCompat.FROM_HTML_MODE_LEGACY
      ),
      Toast.LENGTH_SHORT
    )
    toast?.show()
  }
  // endregion toast, snackbar, dialog, states

  // region VIEW HANDLER
  private fun showRecommendation() {
    // show or hide recommendation if null
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
  }

  private fun hideTrailer(hide: Boolean) {
    binding.ibPlay.isVisible = !hide
  }

  private fun showViewAgeRating(isShow: Boolean) {
    binding.tvAgeRating.isVisible = isShow
    binding.divider2.isVisible = isShow
  }

  private fun showViewRegionReleaseDate(isShow: Boolean) {
    binding.tvRegionRelease.isVisible = isShow
  }

  private fun showViewReleaseDate(isShow: Boolean) {
    binding.tvYearReleased.isVisible = isShow
    binding.divider1.isVisible = isShow
  }
  // endregion VIEW HANDLER

  override fun onDestroy() {
    super.onDestroy()
    mSnackbar?.dismiss()
    mSnackbar = null
    toast?.cancel()
    toast = null
  }

  companion object {
    const val TAG = "DETAIL MOVIE ACTIVITY"
    const val EXTRA_MOVIE = "MOVIE"
  }
}
