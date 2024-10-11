package com.waffiq.bazz_movies.pages.detail

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waffiq.bazz_movies.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_backdrops
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_poster_error
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
import com.waffiq.bazz_movies.R.string.unknown_error
import com.waffiq.bazz_movies.R.string.yt_not_installed
import com.waffiq.bazz_movies.core.data.remote.post_body.FavoritePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.RatePostModel
import com.waffiq.bazz_movies.core.data.remote.post_body.WatchlistPostModel
import com.waffiq.bazz_movies.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.domain.model.Stated
import com.waffiq.bazz_movies.core.domain.model.detail.DetailMovieTvUsed
import com.waffiq.bazz_movies.core.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.core.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.core.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.ui.adapter.TrendingAdapter
import com.waffiq.bazz_movies.viewmodel.UserPreferenceViewModel
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.dateFormatterStandard
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.justifyTextView
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.scrollActionBarBehavior
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.transparentStatusBar
import com.waffiq.bazz_movies.core.utils.common.Constants.DEBOUNCE_LONG
import com.waffiq.bazz_movies.core.utils.common.Constants.NAN
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W780
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.utils.common.Constants.YOUTUBE_LINK_VIDEO
import com.waffiq.bazz_movies.core.utils.helpers.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.utils.helpers.details.CreateTableViewHelper.createTable
import com.waffiq.bazz_movies.core.utils.helpers.details.DetailMovieTvHelper.detailCrew
import com.waffiq.bazz_movies.core.utils.uihelpers.Animation.fadeOut
import com.waffiq.bazz_movies.core.utils.uihelpers.ButtonImageChanger.changeBtnFavoriteBG
import com.waffiq.bazz_movies.core.utils.uihelpers.ButtonImageChanger.changeBtnWatchlistBG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovieActivity : AppCompatActivity() {

  private lateinit var binding: ActivityDetailMovieBinding
  private lateinit var dataExtra: ResultItem
  private val detailViewModel: DetailMovieViewModel by viewModels()
  private val userPreferenceViewModel: UserPreferenceViewModel by viewModels()

  private val adapterCast = CastAdapter()
  private val adapterRecommendation = TrendingAdapter()

  private var favorite = false // is item favorite or not
  private var watchlist = false // is item watchlist or not
  private var isLogin = false // is login as user or not

  private var toast: Toast? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityDetailMovieBinding.inflate(layoutInflater)
    setContentView(binding.root)

    showLoadingDim(true)

    detailViewModel.loadingState.observe(this) { showLoadingDim(it) }
    errorStateObserver()

    transparentStatusBar(window)
    scrollActionBarBehavior(binding.appBarLayout, binding.nestedScrollView)
    justifyTextView(binding.tvOverview)
    loadData()
    favWatchlistHandler()
    viewListener()
  }

  private fun favWatchlistHandler() {
    detailViewModel.postModelState.observe(this) { eventResult ->
      eventResult.getContentIfNotHandled()?.let { postModelState ->
        if (postModelState.isSuccess) {
          if (!postModelState.isDelete) {
            if (!postModelState.isFavorite) showToastAddedWatchlist() else showToastAddedFavorite()
          } else {
            if (postModelState.isFavorite) showToastRemoveFromFavorite() else showToastRemoveFromWatchlist()
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
      isLogin = it.token != NAN

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
    setupRecyclerView()
    showRecommendation()
    showGeneralInfo()
    getDetailBasedMediaType()

    // show detail data based media type
    if (dataExtra.mediaType == "movie") {
      observeDetailMovie()
    } else if (dataExtra.mediaType == "tv") {
      observeDetailTv()
    }
  }

  private fun getDetailBasedMediaType() {
    showBackdrop()
    showPoster()
    if (dataExtra.mediaType == "movie") {
      getDetailMovie()
    } else if (dataExtra.mediaType == "tv") {
      getDetailTv()
    }
  }

  private fun setupRecyclerView() {
    // setup rv cast
    binding.rvCast.itemAnimator = DefaultItemAnimator()
    binding.rvCast.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.rvCast.adapter = adapterCast

    // setup rv recommendation
    binding.rvRecommendation.itemAnimator = DefaultItemAnimator()
    binding.rvRecommendation.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    binding.rvRecommendation.adapter = adapterRecommendation.withLoadStateFooter(
      footer = LoadingStateAdapter { adapterRecommendation.retry() }
    )
  }

  private fun showBackdrop() {
    Glide.with(binding.ivPictureBackdrop)
      .load(
        if (dataExtra.backdropPath == "N/A" || dataExtra.posterPath == "N/A") {
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
      dataExtra.backdropPath.isNullOrEmpty() || dataExtra.backdropPath == "N/A"
  }

  private fun showPoster() {
    Glide.with(binding.ivPoster)
      .load(
        if (dataExtra.posterPath == "N/A") {
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

  private fun showGeneralInfo() {
    // show data (title, released year, media type, and overview) based DATA_EXTRA
    binding.apply {
      dataExtra.apply {
        tvTitle.text = name ?: title ?: originalTitle ?: originalName
        tvMediaType.text = mediaType.uppercase()
        tvYearReleased.text =
          dateFormatterStandard(releaseDate.toString().ifEmpty { firstAirDate.toString() })
        tvOverview.text =
          if (!overview.isNullOrEmpty() && overview.isNotBlank()) {
            overview
          } else {
            getString(no_overview)
          }
      }
    }
  }

  private fun getDetailMovie() {
    // shows crew and cast
    detailViewModel.getMovieCredits(dataExtra.id)

    // get detail movie
    userPreferenceViewModel.getUserPref().observe(this) { user ->
      detailViewModel.detailMovie(dataExtra.id, user.region)
    }

    detailViewModel.detailMovieTv.observe(this) { movie ->
      // copy genre id
      dataExtra = dataExtra.copy(listGenreIds = movie.genreId)

      // update UI for movie details
      updateMovieDetailsUI(movie)

      // Trigger fetching of other data
      if (!movie.imdbId.isNullOrEmpty()) {
        detailViewModel.getScoreOMDb(movie.imdbId)
      } else {
        showLoadingDim(false)
      }
      detailViewModel.getLinkVideoMovie(movie.id)
      detailViewModel.getRecommendationMovie(movie.id)
    }
  }

  private fun updateMovieDetailsUI(data: DetailMovieTvUsed) {
    // show genre, duration, tmdb score
    binding.tvGenre.text = data.genre ?: getString(not_available)
    binding.tvDuration.text = data.duration ?: getString(not_available)
    binding.tvScoreTmdb.text = data.tmdbScore ?: getString(not_available)

    // set age rating
    if (!data.ageRating.isNullOrEmpty()) {
      showViewAgeRating(true)
      binding.tvAgeRating.text = data.ageRating
    } else {
      showViewAgeRating(false)
    }

    // set region release
    if (data.releaseDateRegion.regionRelease.isNotEmpty()) {
      showViewRegionReleaseDate(true)
      binding.tvRegionRelease.text = data.releaseDateRegion.regionRelease
    } else {
      showViewRegionReleaseDate(false)
    }

    // set date release based on user region
    if (data.releaseDateRegion.releaseDate.isNotEmpty()) {
      showViewReleaseDate(true)
      binding.tvYearReleased.text = data.releaseDateRegion.releaseDate
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
    userPreferenceViewModel.getUserPref().observe(this) {
      detailViewModel.detailTv(dataExtra.id, it.region)
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

      // release date
      if (tv.releaseDateRegion.regionRelease.isNotEmpty()) {
        showViewReleaseDate(true)
        binding.tvYearReleased.text = tv.releaseDateRegion.regionRelease
      } else {
        showViewReleaseDate(false)
      }

      showViewRegionReleaseDate(false)
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

  private fun getStated(token: String) {
    if (dataExtra.mediaType == "movie") {
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
        if (!isLogin) { // Guest user
          detailViewModel.handleBtnFavorite(favorite, watchlist, dataExtra)
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = true, state = favorite)
        }
      }

      btnWatchlist.setOnClickListener {
        if (!isLogin) { // Guest user
          detailViewModel.handleBtnWatchlist(favorite, watchlist, dataExtra)
        } else { // Logged-in user
          postDataToTMDB(isModeFavorite = false, state = watchlist)
        }
      }

      yourScoreViewGroup.setOnClickListener { showDialogRate() }

      imdbViewGroup.setOnClickListener { if (!tvScoreImdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      tmdbViewGroup.setOnClickListener { if (!tvScoreTmdb.text.contains("[0-9]".toRegex())) showDialogNotRated() }
      metascoreViewGroup.setOnClickListener {
        if (!tvScoreMetascore.text.contains("[0-9]".toRegex())) {
          showDialogNotRated()
        }
      }
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
      }
      detailViewModel.itemState.observe(this) {
        if (it != null) {
          favorite = it.favorite
          watchlist = it.watchlist
          showRatingUserLogin(it)
          changeBtnFavoriteBG(this, binding.btnFavorite, it.favorite)
          changeBtnWatchlistBG(this, binding.btnWatchlist, it.watchlist)
        }
      }
    } else { // guest user
      detailViewModel.isFavoriteDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isFavorite.observe(this) {
        changeBtnFavoriteBG(this, binding.btnFavorite, it)
        favorite = it
      }
      detailViewModel.isWatchlistDB(dataExtra.id, dataExtra.mediaType)
      detailViewModel.isWatchlist.observe(this) {
        changeBtnWatchlistBG(this, binding.btnWatchlist, it)
        watchlist = it
      }
    }
  }

  // region toast, snackbar, dialog
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
    MaterialAlertDialogBuilder(this).apply {
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
        if (dataExtra.mediaType == "movie") {
          detailViewModel.postMovieRate(user.token, ratePostModel, dataExtra.id)
        } else {
          detailViewModel.postTvRate(user.token, ratePostModel, dataExtra.id)
        }
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

  @OptIn(FlowPreview::class) // Allows using the debounce function
  private fun errorStateObserver() {
    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        detailViewModel.errorState.debounce(DEBOUNCE_LONG) // Prevent multiple emissions within 500ms
          .collect { errorMessage ->
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
  // endregion toast, snackbar, dialog

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

  private fun showLoadingDim(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.isVisible = true
    } else {
      binding.appBarLayout.isVisible = true
      binding.progressBar.isVisible = false
      fadeOut(binding.backgroundDimMovie, 200L)
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
    toast?.cancel()
    toast = null
  }

  companion object {
    const val TAG = "DETAIL MOVIE ACTIVITY"
    const val EXTRA_MOVIE = "MOVIE"
  }
}
