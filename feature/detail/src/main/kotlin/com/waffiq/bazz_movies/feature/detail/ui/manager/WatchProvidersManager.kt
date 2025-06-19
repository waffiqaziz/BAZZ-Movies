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
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.feature.detail.databinding.ActivityDetailMovieBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.ui.adapter.WatchProvidersAdapter
import com.waffiq.bazz_movies.feature.detail.ui.state.WatchProvidersUiState

class WatchProvidersManager(
  private val binding: ActivityDetailMovieBinding,
  private val context: Context,
  private val dataExtra: ResultItem,
) {
  private lateinit var adapterBuy: WatchProvidersAdapter
  private lateinit var adapterFree: WatchProvidersAdapter
  private lateinit var adapterRent: WatchProvidersAdapter
  private lateinit var adapterStreaming: WatchProvidersAdapter

  private var isExpanded = false

  init {
    setupWatchProvidersUI()
    setupClickListeners()
  }

  private fun setupWatchProvidersUI() {
    setupRecyclerViewsWithSnap(
      listOf(
        binding.rvBuy,
        binding.rvFree,
        binding.rvRent,
        binding.rvStreaming,
      )
    )

    val clickListener = { openTMDBWatchPage() }

    // initialize adapters
    adapterBuy = WatchProvidersAdapter(clickListener)
    adapterFree = WatchProvidersAdapter(clickListener)
    adapterRent = WatchProvidersAdapter(clickListener)
    adapterStreaming = WatchProvidersAdapter(clickListener)

    // setup RecyclerViews
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

  private fun setupClickListeners() {
    binding.tvToggleWatchProviders.setOnClickListener {
      toggleWatchProvidersVisibility()
    }

    binding.btnJustwatch.setOnClickListener {
      openJustWatch()
    }
  }

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

  private fun openTMDBWatchPage() {
    context.startActivity(
      Intent(
        Intent.ACTION_VIEW,
        "$TMDB_LINK_MAIN/${dataExtra.mediaType}/${dataExtra.id}/watch".toUri()
      )
    )
  }

  private fun openJustWatch() {
    context.startActivity(Intent(Intent.ACTION_VIEW, JUSTWATCH_LINK_MAIN.toUri()))
  }

  fun observeWatchProviders(
    watchProvidersUiState: LiveData<WatchProvidersUiState>,
    lifecycleOwner: LifecycleOwner,
  ) {
    watchProvidersUiState.observe(lifecycleOwner) { state ->
      handleWatchProvidersState(state)
    }
  }

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

  private fun showLoading(isLoading: Boolean) {
    binding.progressBar.isVisible = isLoading
  }

  private fun hideError() {
    binding.tvWatchProvidersMessage.isVisible = false
    binding.layoutJustwatch.isVisible = true
  }

  private fun showError(message: String) {
    binding.tvWatchProvidersMessage.apply {
      text = message
      isVisible = true
    }
    binding.layoutJustwatch.isVisible = false

    hideAllProviderSections()
  }

  private fun showSuccessState(state: WatchProvidersUiState.Success) {
    // Update adapters
    adapterBuy.setProviders(state.buy)
    adapterFree.setProviders(state.free)
    adapterRent.setProviders(state.rent)
    adapterStreaming.setProviders(state.flatrate)

    // Update visibility for each provider type
    updateProviderVisibility(
      state.buy,
      binding.rvBuy,
      binding.labelBuy,
      binding.layoutBuy
    )

    updateProviderVisibility(
      state.free,
      binding.rvFree,
      binding.labelFree,
      binding.layoutFree
    )

    updateProviderVisibility(
      state.rent,
      binding.rvRent,
      binding.labelRent,
      binding.layoutRent
    )

    updateProviderVisibility(
      state.flatrate,
      binding.rvStreaming,
      binding.labelStreaming,
      binding.layoutStreaming
    )
  }

  private fun updateProviderVisibility(
    providers: List<Provider>,
    recyclerView: RecyclerView,
    label: View,
    layout: View,
  ) {
    val hasProviders = providers.isNotEmpty()
    recyclerView.isVisible = hasProviders
    label.isVisible = hasProviders
    layout.isVisible = hasProviders
  }

  private fun hideAllProviderSections() {
    listOf(
      binding.rvBuy, binding.rvFree, binding.rvRent, binding.rvStreaming,
      binding.labelBuy, binding.labelFree, binding.labelRent, binding.labelStreaming,
      binding.layoutBuy, binding.layoutFree, binding.layoutRent, binding.layoutStreaming
    ).forEach { view ->
      view.isVisible = false
    }
  }
}
