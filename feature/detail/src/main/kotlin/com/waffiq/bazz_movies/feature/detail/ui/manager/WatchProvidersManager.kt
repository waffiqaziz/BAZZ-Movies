package com.waffiq.bazz_movies.feature.detail.ui.manager

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.waffiq.bazz_movies.core.common.utils.Constants.JUSTWATCH_LINK_MAIN
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_LINK_MAIN
import com.waffiq.bazz_movies.core.designsystem.R.string.where_to_watch_down
import com.waffiq.bazz_movies.core.designsystem.R.string.where_to_watch_up
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityMediaDetailBinding
import com.waffiq.bazz_movies.feature.detail.ui.adapter.WatchProvidersAdapter
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState

/**
 * Manages the UI and interactions related to watch providers on the detail movie screen.
 *
 * This includes:
 * - Initializing RecyclerViews and their adapters for each provider type (Buy, Free, Rent, Stream)
 * - Handling expand/collapse behavior for provider sections
 * - Observing and displaying the watch provider state from the ViewModel
 * - Opening external links to TMDB or JustWatch
 *
 * @param binding The view binding for the detail screen layout.
 * @param context A [Context] used for launching intents and accessing resources.
 * @param dataExtra The movie or TV show data being displayed.
 */
class WatchProvidersManager(
  private val binding: ActivityMediaDetailBinding,
  private val context: Context,
  private val dataExtra: MediaItem,
) {
  private lateinit var adapterAds: WatchProvidersAdapter
  private lateinit var adapterBuy: WatchProvidersAdapter
  private lateinit var adapterFree: WatchProvidersAdapter
  private lateinit var adapterRent: WatchProvidersAdapter
  private lateinit var adapterStreaming: WatchProvidersAdapter

  private var isExpanded = false

  init {
    setupWatchProvidersUI()
    setupClickListeners()
  }

  /**
   * Sets up the RecyclerViews and their adapters for each watch provider category.
   */
  private fun setupWatchProvidersUI() {
    setupRecyclerViewsWithSnap(
      listOf(
        binding.rvAds,
        binding.rvBuy,
        binding.rvFree,
        binding.rvRent,
        binding.rvStreaming,
      ),
    )

    val clickListener = { openTMDBWatchPage() }

    adapterAds = WatchProvidersAdapter(clickListener)
    adapterBuy = WatchProvidersAdapter(clickListener)
    adapterFree = WatchProvidersAdapter(clickListener)
    adapterRent = WatchProvidersAdapter(clickListener)
    adapterStreaming = WatchProvidersAdapter(clickListener)

    setupRecyclerView(binding.rvAds, adapterAds)
    setupRecyclerView(binding.rvBuy, adapterBuy)
    setupRecyclerView(binding.rvFree, adapterFree)
    setupRecyclerView(binding.rvRent, adapterRent)
    setupRecyclerView(binding.rvStreaming, adapterStreaming)
  }

  private fun setupRecyclerView(recyclerView: RecyclerView, adapter: WatchProvidersAdapter) {
    recyclerView.apply {
      itemAnimator = DefaultItemAnimator()
      this.adapter = adapter
    }
  }

  /**
   * Sets up click listeners for toggling provider visibility and external links.
   */
  private fun setupClickListeners() {
    binding.tvToggleWatchProviders.setOnClickListener {
      toggleWatchProvidersVisibility()
    }

    binding.btnJustwatch.setOnClickListener {
      openJustWatch()
    }
  }

  /**
   * Toggles the visibility of the full watch provider section.
   */
  private fun toggleWatchProvidersVisibility() {
    isExpanded = !isExpanded

    TransitionManager.beginDelayedTransition(binding.watchProviderSection, AutoTransition())
    binding.groupWatchProviders.visibility = if (isExpanded) View.VISIBLE else View.GONE

    binding.tvToggleWatchProviders.text = if (isExpanded) {
      context.getString(where_to_watch_up)
    } else {
      context.getString(where_to_watch_down)
    }
  }

  /**
   * Launches the browser with the TMDB "watch" page for the current media item.
   */
  private fun openTMDBWatchPage() {
    context.startActivity(
      Intent(
        Intent.ACTION_VIEW,
        "$TMDB_LINK_MAIN/${dataExtra.mediaType}/${dataExtra.id}/watch".toUri(),
      ),
    )
  }

  /**
   * Opens the JustWatch website in the browser.
   */
  private fun openJustWatch() {
    context.startActivity(Intent(Intent.ACTION_VIEW, JUSTWATCH_LINK_MAIN.toUri()))
  }

  /**
   * Observes the [WatchProvidersUiState] from ViewModel and updates the UI accordingly.
   *
   * @param watchProvidersUiState The LiveData to observe.
   * @param lifecycleOwner The lifecycle owner for binding observation.
   */
  fun observeWatchProviders(
    watchProvidersUiState: LiveData<WatchProvidersUiState>,
    lifecycleOwner: LifecycleOwner,
  ) {
    watchProvidersUiState.observe(lifecycleOwner) { state ->
      handleWatchProvidersState(state)
    }
  }

  /**
   * Handles UI updates based on the current state of watch providers.
   *
   * @param state The state representing loading, success, or error.
   */
  private fun handleWatchProvidersState(state: WatchProvidersUiState) {
    when (state) {
      is WatchProvidersUiState.Loading -> {
        showLoading(true)
        hideError()
      }

      is WatchProvidersUiState.Success -> {
        showLoading(false)
        hideError()
        showSuccessState(state)
      }

      is WatchProvidersUiState.Error -> {
        showLoading(false)
        showError(state.message)
      }
    }
  }

  /**
   * Displays or hides the loading indicator.
   *
   * @param isLoading Whether to show the progress bar.
   */
  private fun showLoading(isLoading: Boolean) {
    binding.progressBar.isVisible = isLoading
  }

  /**
   * Hides the error message and shows the JustWatch layout.
   */
  private fun hideError() {
    binding.tvWatchProvidersMessage.isVisible = false
    binding.layoutJustwatch.isVisible = true
  }

  /**
   * Displays an error message and hides all watch provider sections.
   *
   * @param message The error message to display.
   */
  private fun showError(message: String) {
    binding.tvWatchProvidersMessage.apply {
      text = message
      isVisible = true
    }
    binding.layoutJustwatch.isVisible = false

    // hide all provider sections
    binding.layoutAds.isVisible = false
    binding.layoutBuy.isVisible = false
    binding.layoutFree.isVisible = false
    binding.layoutRent.isVisible = false
    binding.layoutStreaming.isVisible = false
  }

  /**
   * Updates the UI to show watch providers based on available data.
   *
   * @param state The successful state containing categorized provider lists.
   */
  private fun showSuccessState(state: WatchProvidersUiState.Success) {
    adapterAds.setProviders(state.ads)
    adapterBuy.setProviders(state.buy)
    adapterFree.setProviders(state.free)
    adapterRent.setProviders(state.rent)
    adapterStreaming.setProviders(state.flatrate)

    // update provider visibility
    binding.layoutAds.isVisible = state.ads.isNotEmpty()
    binding.layoutBuy.isVisible = state.buy.isNotEmpty()
    binding.layoutFree.isVisible = state.free.isNotEmpty()
    binding.layoutRent.isVisible = state.rent.isNotEmpty()
    binding.layoutStreaming.isVisible = state.flatrate.isNotEmpty()
  }
}
