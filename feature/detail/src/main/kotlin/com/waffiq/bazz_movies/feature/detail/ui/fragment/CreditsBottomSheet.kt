package com.waffiq.bazz_movies.feature.detail.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.waffiq.bazz_movies.core.designsystem.R.string.cast
import com.waffiq.bazz_movies.core.designsystem.R.string.crew
import com.waffiq.bazz_movies.feature.detail.databinding.BottomSheetCreditsBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CrewAdapter
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditsBottomSheet : BottomSheetDialogFragment() {

  @Inject
  lateinit var navigator: INavigator

  private lateinit var binding: BottomSheetCreditsBinding
  private lateinit var castAdapter: CastAdapter
  private lateinit var crewAdapter: CrewAdapter

  private val viewModel: MediaDetailViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = BottomSheetCreditsBinding.inflate(inflater, null, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupBottomSheetBehavior()

    val credits = viewModel.uiState.value.credits ?: return
    setupRecyclerView(credits)
    setupTabs(credits)
  }

  private fun setupBottomSheetBehavior() {
    (dialog as BottomSheetDialog).behavior.apply {
      state = BottomSheetBehavior.STATE_HALF_EXPANDED
      skipCollapsed = false
    }
  }

  private fun setupRecyclerView(credits: MediaCredits) {
    castAdapter = CastAdapter(navigator)
    crewAdapter = CrewAdapter()

    castAdapter.setVerticalMode(true)

    binding.rvCredits.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = castAdapter
    }

    // load initial cast list
    castAdapter.submitList(credits.cast)
  }

  private fun setupTabs(credits:  MediaCredits) {
    binding.tabLayout.apply {
      addTab(newTab().setText(getString(cast) + " (${credits.cast.size})"))
      addTab(newTab().setText(getString(crew) + " (${credits.crew.size})"))
    }

    binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        binding.rvCredits.adapter = when (tab.position) {
          0 -> castAdapter.also { it.submitList(credits.cast) }
          else -> crewAdapter.also { it.submitList(credits.crew) }
        }
      }

      override fun onTabUnselected(tab: TabLayout.Tab) = Unit
      override fun onTabReselected(tab: TabLayout.Tab) = Unit
    })
  }
}
