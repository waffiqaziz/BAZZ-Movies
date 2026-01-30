package com.waffiq.bazz_movies.feature.detail.ui.manager

import android.content.ActivityNotFoundException
import android.util.Log
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_LONG
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.common.utils.Constants.MOVIE_MEDIA_TYPE
import com.waffiq.bazz_movies.core.common.utils.Constants.NOT_AVAILABLE
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_BACKDROP_ORIGINAL
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_backdrop_error_filled
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_backdrops
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.no_overview
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.R.string.yt_not_installed
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.LoadingStateAdapter
import com.waffiq.bazz_movies.core.uihelper.utils.Animation.fadeOut
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.SnackBarManager.snackBarWarning
import com.waffiq.bazz_movies.core.utils.DateFormatter.dateFormatterStandard
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaDetail
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.domain.model.releasedate.ReleaseDateRegion
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.GenreAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.RecommendationAdapter
import com.waffiq.bazz_movies.feature.detail.ui.launcher.DefaultTrailerLauncher
import com.waffiq.bazz_movies.feature.detail.utils.helpers.CreateTableViewHelper.createTable
import com.waffiq.bazz_movies.feature.detail.utils.helpers.MediaHelper.extractCrewDisplayNames
import com.waffiq.bazz_movies.navigation.INavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

/**
 * Manages the UI presentation logic for the Detail Movie screen.
 *
 * This class is responsible for setting up and updating the UI components such as:
 * - Movie poster and backdrop
 * - Title, overview, genre, duration, and release date
 * - Cast and crew credits
 * - Recommendations and trailers
 * - External ratings from TMDb and OMDb
 * - UI state handling (loading, error messages, etc.)
 *
 * @param binding The binding object for the detail activity layout.
 * @param activity The parent activity for accessing lifecycle, resources, and context.
 * @param navigator A navigation interface used for handling item clicks.
 */
class DetailUIManager(
  private val binding: ActivityDetailMovieBinding,
  private val activity: AppCompatActivity,
  private val navigator: INavigator,
) {
  private lateinit var adapterCast: CastAdapter
  private lateinit var adapterRecommendation: RecommendationAdapter
  private lateinit var adapterGenre: GenreAdapter

  private var mSnackbar: Snackbar? = null
  private var toast: Toast? = null

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  var trailerLauncher = DefaultTrailerLauncher()

  init {
    setupRecyclerViews()
    initializeAdapters()
  }

  /**
   * Sets up horizontal snap scrolling for RecyclerViews list of genre, cast, and recommendation.
   */
  private fun setupRecyclerViews() {
    setupRecyclerViewsWithSnap(
      listOf(
        binding.rvCast,
        binding.rvRecommendation,
      )
    )
    binding.rvGenre.layoutManager = FlexboxLayoutManager(binding.rvGenre.context).apply {
      flexDirection = FlexDirection.ROW
      flexWrap = FlexWrap.WRAP
      justifyContent = JustifyContent.FLEX_START
    }
  }

  /**
   * Initializes adapters for cast and recommendation lists, and sets them to their RecyclerViews.
   */
  private fun initializeAdapters() {
    adapterGenre = GenreAdapter()
    adapterCast = CastAdapter(navigator)
    adapterRecommendation = RecommendationAdapter(navigator)

    binding.rvGenre.apply {
      itemAnimator = DefaultItemAnimator()
      adapter = adapterGenre
    }

    binding.rvCast.apply {
      itemAnimator = DefaultItemAnimator()
      adapter = adapterCast
    }

    binding.rvRecommendation.apply {
      itemAnimator = DefaultItemAnimator()
      adapter = adapterRecommendation.withLoadStateFooter(
        footer = LoadingStateAdapter { adapterRecommendation.retry() }
      )
    }

    setupRecommendationVisibility()
  }

  /**
   * Controls the visibility of recommendation section based on loading state and item count.
   */
  private fun setupRecommendationVisibility() {
    adapterRecommendation.addLoadStateListener { loadState ->
      val isRecommendationEmpty = loadState.source.refresh is LoadState.NotLoading &&
        loadState.append.endOfPaginationReached &&
        adapterRecommendation.itemCount < 1

      binding.tvRecommendationHeader.isVisible = !isRecommendationEmpty
      binding.rvRecommendation.isVisible = !isRecommendationEmpty
    }
  }

  /**
   * Displays general media info like images, title, release year, and overview.
   */
  fun showGeneralInfo(dataExtra: MediaItem) {
    showBackdropAndPoster(dataExtra)
    updateBasicInfo(dataExtra)
  }

  /**
   * Loads and displays poster and backdrop images using Glide.
   */
  private fun showBackdropAndPoster(dataExtra: MediaItem) {
    val backdropUrl = when {
      dataExtra.backdropPath == NOT_AVAILABLE && dataExtra.posterPath == NOT_AVAILABLE ->
        ic_backdrop_error_filled

      !dataExtra.backdropPath.isNullOrEmpty() ->
        TMDB_IMG_LINK_BACKDROP_ORIGINAL + dataExtra.backdropPath

      !dataExtra.posterPath.isNullOrEmpty() ->
        TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath

      else -> ic_backdrop_error_filled
    }

    Glide.with(binding.ivPictureBackdrop)
      .load(backdropUrl)
      .placeholder(ic_bazz_placeholder_backdrops)
      .centerCrop()
      .error(ic_backdrop_error_filled)
      .transition(withCrossFade())
      .into(binding.ivPictureBackdrop)

    binding.tvBackdropNotFound.isVisible =
      dataExtra.backdropPath.isNullOrEmpty() || dataExtra.backdropPath == NOT_AVAILABLE

    val posterUrl = when {
      dataExtra.posterPath == NOT_AVAILABLE -> ic_poster_error
      dataExtra.posterPath != null -> TMDB_IMG_LINK_POSTER_W500 + dataExtra.posterPath
      else -> ic_poster_error
    }

    Glide.with(binding.ivPoster)
      .load(posterUrl)
      .placeholder(ic_bazz_placeholder_poster)
      .error(ic_poster_error)
      .transition(withCrossFade())
      .into(binding.ivPoster)
  }

  /**
   * Populates basic text data such as title, media type, release date, and overview.
   */
  private fun updateBasicInfo(dataExtra: MediaItem) {
    binding.apply {
      tvTitle.text =
        dataExtra.name ?: dataExtra.title ?: dataExtra.originalTitle ?: dataExtra.originalName
      tvMediaType.text = dataExtra.mediaType.uppercase()
      tvYearReleased.text = dateFormatterStandard(
        dataExtra.releaseDate?.takeIf { it.isNotEmpty() }
          ?: dataExtra.firstAirDate?.takeIf { it.isNotEmpty() }.orEmpty()
      )
      tvOverview.text = dataExtra.overview?.takeIf { it.isNotBlank() }
        ?: activity.getString(no_overview)
    }
  }

  /**
   * Updates the UI with detailed metadata like genre, score, runtime, and status.
   */
  fun updateDetailUI(details: MediaDetail, mediaType: String) {
    binding.apply {
      if (details.genreId != null) adapterGenre.setGenre(details.genreId)
      tvScoreTmdb.text = details.tmdbScore ?: activity.getString(not_available)

      // set duration for movie and status for tv-series
      tvDuration.text = when (mediaType) {
        MOVIE_MEDIA_TYPE -> details.duration ?: activity.getString(not_available)
        else -> {
          if (details.duration.isNullOrEmpty()) {
            activity.getString(not_available)
          } else {
            details.duration
          }
        }
      }
    }

    updateAgeRating(details.ageRating)
    updateReleaseInfo(details.releaseDateRegion)
    showLoadingDim(false)
  }

  /**
   * Updates age restriction based on selected region
   */
  private fun updateAgeRating(ageRating: String?) {
    val hasAgeRating = !ageRating.isNullOrEmpty()
    binding.tvAgeRating.isVisible = hasAgeRating
    if (hasAgeRating) binding.tvAgeRating.text = ageRating
  }

  /**
   * Updates release date according to the region.
   */
  private fun updateReleaseInfo(releaseDateRegion: ReleaseDateRegion) {
    // region release
    val hasRegionRelease = releaseDateRegion.regionRelease.isNotEmpty()
    binding.tvRegionRelease.isVisible = hasRegionRelease
    if (hasRegionRelease) {
      binding.tvRegionRelease.text = releaseDateRegion.regionRelease
    }

    // release date
    val hasReleaseDate = releaseDateRegion.releaseDate.isNotEmpty()
    binding.tvYearReleased.isVisible = hasReleaseDate
    if (hasReleaseDate) binding.tvYearReleased.text = releaseDateRegion.releaseDate
  }

  /**
   * Updates the cast and crew credits section.
   */
  fun updateCreditsUI(credits: MediaCredits) {
    createTable(activity, extractCrewDisplayNames(credits.crew), binding.table)
    adapterCast.setCast(credits.cast)

    val hasCast = adapterCast.itemCount > 0
    binding.rvCast.isVisible = hasCast
    binding.tvCastHeader.isVisible = hasCast
  }

  /**
   * Updates the external ratings (IMDb, Metascore, Rotten Tomatoes) from OMDb.
   */
  fun updateOMDbScores(omdbDetails: OMDbDetails) {
    binding.apply {
      tvScoreImdb.text = omdbDetails.imdbRating?.takeIf { it.isNotBlank() }
        ?: activity.getString(not_available)
      tvScoreMetascore.text = omdbDetails.metascore?.takeIf { it.isNotBlank() }
        ?: activity.getString(not_available)
      tvScoreRottenTomatoes.text = omdbDetails.ratings
        ?.firstOrNull { it.source == "Rotten Tomatoes" }?.value
        ?: activity.getString(not_available)
    }
  }

  /**
   * Sets up the trailer button and click handler if a YouTube video link is available.
   */
  fun setupTrailerButton(videoLink: String?) {
    val hasTrailer = !videoLink.isNullOrBlank()
    binding.ibPlay.isVisible = hasTrailer

    if (hasTrailer) {
      binding.ibPlay.setOnClickListener {
        playTrailer(videoLink)
      }
    }
  }

  /**
   * Open trailer in youtube link.
   */
  fun playTrailer(link: String) {
    try {
      trailerLauncher.launch(activity, link)
    } catch (e: ActivityNotFoundException) {
      Log.e(TAG, "YouTube app not installed", e)
      showSnackbarWarning(activity.getString(yt_not_installed))
    }
  }

  /**
   * Updates the recommendation list with paginated data.
   */
  fun updateRecommendations(recommendations: PagingData<MediaItem>, lifecycle: Lifecycle) {
    adapterRecommendation.submitData(lifecycle, recommendations)
  }

  /**
   * Displays or hides a loading indicator with background dimming.
   */
  fun showLoadingDim(isLoading: Boolean) {
    if (isLoading) {
      binding.progressBar.isVisible = true
    } else {
      binding.appBarLayout.isVisible = true
      binding.progressBar.isVisible = false
      fadeOut(binding.backgroundDimMovie, DEBOUNCE_SHORT)
    }
  }

  /**
   * Binds a LiveData<Boolean> to the loading UI.
   */
  fun setupLoadingObserver(loadingState: LiveData<Boolean>) {
    loadingState.observe(activity) { isLoading ->
      showLoadingDim(isLoading)
    }
  }

  /**
   * Observes error messages and shows them using Snackbar.
   */
  fun setupErrorObserver(errorState: Flow<String>) {
    activity.lifecycleScope.launch {
      activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
        errorState
          .debounce(DEBOUNCE_LONG)
          .collect { errorMessage ->
            showSnackbarWarning(errorMessage)
          }
      }
    }
  }

  /**
   * Displays a styled Toast message.
   */
  fun showToast(text: String) {
    toast?.cancel()
    toast = Toast.makeText(
      activity.applicationContext,
      text,
      Toast.LENGTH_SHORT
    )
    toast?.show()
  }

  /**
   * Shows a warning message using a Snackbar.
   */
  private fun showSnackbarWarning(message: String) {
    mSnackbar = snackBarWarning(binding.coordinatorLayout, null, message)
  }

  /**
   * Dismisses the current Snackbar if showing.
   */
  fun dismissSnackbar() {
    mSnackbar?.dismiss()
  }

  /**
   * Clears all transient UI state (snackbars, toasts).
   */
  fun cleanup() {
    mSnackbar?.dismiss()
    mSnackbar = null
    toast?.cancel()
    toast = null
  }

  companion object {
    private const val TAG = "DetailMovieUIManager"
  }
}
