@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.waffiq.bazz_movies.core.common.utils.Constants.TV_MEDIA_TYPE
import com.waffiq.bazz_movies.core.designsystem.R.string.binding_error
import com.waffiq.bazz_movies.core.uihelper.snackbar.ISnackbar
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnap
import com.waffiq.bazz_movies.core.uihelper.utils.Helpers.setupRecyclerViewsWithSnapGridLayout
import com.waffiq.bazz_movies.core.utils.FlowUtils.collectAndSubmitData
import com.waffiq.bazz_movies.feature.home.databinding.FragmentAsianBinding
import com.waffiq.bazz_movies.feature.home.ui.adapter.ItemWIdeAdapter
import com.waffiq.bazz_movies.feature.home.ui.adapter.MediaAdapter
import com.waffiq.bazz_movies.feature.home.ui.domain.AnimePeriod
import com.waffiq.bazz_movies.feature.home.ui.viewmodel.AsianViewModel
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.detachRecyclerView
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.observeLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupLoadState
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRecyclerWideItem
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupRetryButton
import com.waffiq.bazz_movies.feature.home.utils.helpers.HomeFragmentHelper.setupSwipeRefresh
import com.waffiq.bazz_movies.navigation.INavigator
import com.waffiq.bazz_movies.navigation.ListArgs
import com.waffiq.bazz_movies.navigation.ListType
import com.waffiq.bazz_movies.navigation.MediaSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AsianFragment : Fragment() {

  @Inject
  lateinit var navigator: INavigator

  @Inject
  lateinit var snackbar: ISnackbar

  private lateinit var animeAdapter: ItemWIdeAdapter
  private lateinit var costumeDramaAdapter: MediaAdapter
  private lateinit var asianRomanceAdapter: MediaAdapter
  private lateinit var donghuaAdapter: MediaAdapter

  private var _binding: FragmentAsianBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  private val asianViewModel: AsianViewModel by viewModels()

  private var mSnackbar: Snackbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    animeAdapter = ItemWIdeAdapter(navigator)
    costumeDramaAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
    asianRomanceAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
    donghuaAdapter = MediaAdapter(navigator, MediaSource.Typed(TV_MEDIA_TYPE))
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = FragmentAsianBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    showShimmer(true)
    setData()
    setupAdapter()
    refreshHandle()
    moreButtonAction()
  }

  private fun showShimmer(isVisible: Boolean) {
    binding.shimmerLayoutAsian.shimmerAsian.isVisible = isVisible
  }

  private fun setupAdapter() {
    // Set up RecyclerViews
    setupRecyclerWideItem(binding.rvAnime)
    setupRecyclerViewsWithSnap(listOf(binding.rvCostumeDrama, binding.rvDonghua))
    setupRecyclerViewsWithSnapGridLayout(recyclerViews = listOf(binding.rvRomanceDrama))

    binding.apply {
      rvAnime.setupLoadState(animeAdapter)
      rvCostumeDrama.setupLoadState(costumeDramaAdapter)
      rvRomanceDrama.setupLoadState(asianRomanceAdapter)
      rvDonghua.setupLoadState(donghuaAdapter)
    }
  }

  private fun setData() {
    viewLifecycleOwner.observeLoadState(
      loadStateFlow = donghuaAdapter.loadStateFlow,
      onLoading = { showShimmer(true) },
      onSuccess = {
        binding.illustrationErrorAsian.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showShimmer(false)
        showView(true)
      },
      onError = { error ->
        binding.illustrationErrorAsian.apply {
          progressCircular.isVisible = false
          btnTryAgain.isVisible = true
        }
        showShimmer(false)
        showView(donghuaAdapter.itemCount > 0)
        mSnackbar = snackbar.showSnackbarWarning(error)
      },
    )

    // Observe ViewModel data and submit to adapters
    observeAnime()
    collectAndSubmitData(this, { asianViewModel.getCostumeDrama() }, costumeDramaAdapter)
    collectAndSubmitData(this, { asianViewModel.getAsianRomance() }, asianRomanceAdapter)
    collectAndSubmitData(this, { asianViewModel.getDonghua() }, donghuaAdapter)
  }

  private fun refreshHandle() {
    // Set up swipe-to-refresh
    setupSwipeRefresh(
      binding.swipeRefreshAsian,
      animeAdapter,
      donghuaAdapter,
      costumeDramaAdapter,
      asianRomanceAdapter,
    )

    // Set up retry button
    setupRetryButton(
      binding.illustrationErrorAsian,
      animeAdapter,
      donghuaAdapter,
      costumeDramaAdapter,
      asianRomanceAdapter,
    )
  }

  private fun showView(isVisible: Boolean) {
    // Toggle visibility based on the flag
    binding.apply {
      layoutHeaderAnime.isVisible = isVisible
      rvAnime.isVisible = isVisible
      layoutHeaderCostumeDrama.isVisible = isVisible
      rvCostumeDrama.isVisible = isVisible
      layoutHeaderRomanceDrama.isVisible = isVisible
      rvRomanceDrama.isVisible = isVisible
      layoutHeaderDonghua.isVisible = isVisible
      rvDonghua.isVisible = isVisible
      illustrationErrorAsian.root.isVisible = !isVisible
    }
  }

  private fun moreButtonAction() {
    binding.btnMoreAnime.button.setOnClickListener {
      openList(
        listType =
        if (asianViewModel.animePeriod.value == AnimePeriod.THIS_SEASON) {
          ListType.ANIME_THIS_SEASON
        } else {
          ListType.ANIME_ALL_TIME
        },
      )
    }
    binding.btnMoreCostumeDrama.button.setOnClickListener {
      openList(ListType.COSTUME_DRAMA)
    }
    binding.btnMoreRomanceDrama.button.setOnClickListener {
      openList(ListType.ROMANCE_DRAMA)
    }
    binding.btnMoreDonghua.button.setOnClickListener {
      openList(ListType.DONGHUA)
    }
  }

  private fun openList(listType: ListType) {
    navigator.openList(
      requireContext(),
      ListArgs(
        listType = listType,
        mediaType = MediaSource.Typed(TV_MEDIA_TYPE),
        title = "",
      ),
    )
  }

  private fun observeAnime() {
    collectAndSubmitData(this, { asianViewModel.anime }, animeAdapter)

    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        asianViewModel.animePeriod.collect { period ->
          binding.btnAnimeAllTime.isChecked = period == AnimePeriod.ALL_TIME
          binding.btnAnimeThisSeason.isChecked = period == AnimePeriod.THIS_SEASON
        }
      }
    }

    binding.btnAnimeAllTime.setOnClickListener {
      asianViewModel.setAnimePeriod(AnimePeriod.ALL_TIME)
    }
    binding.btnAnimeThisSeason.setOnClickListener {
      asianViewModel.setAnimePeriod(AnimePeriod.THIS_SEASON)
    }
  }

  override fun onPause() {
    super.onPause()
    mSnackbar?.dismiss()
  }

  override fun onStop() {
    super.onStop()
    mSnackbar?.dismiss()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    animeAdapter.removeLoadStateListener { }
    costumeDramaAdapter.removeLoadStateListener { }
    asianRomanceAdapter.removeLoadStateListener { }
    donghuaAdapter.removeLoadStateListener { }

    // Detach RecyclerViews programmatically
    binding.apply {
      rvAnime.detachRecyclerView()
      rvCostumeDrama.detachRecyclerView()
      rvRomanceDrama.detachRecyclerView()
      rvDonghua.detachRecyclerView()
    }

    mSnackbar = null
    _binding = null
  }
}
