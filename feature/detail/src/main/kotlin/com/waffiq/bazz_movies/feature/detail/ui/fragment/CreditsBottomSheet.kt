package com.waffiq.bazz_movies.feature.detail.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.waffiq.bazz_movies.core.designsystem.R.string.cast
import com.waffiq.bazz_movies.core.designsystem.R.string.crew
import com.waffiq.bazz_movies.core.designsystem.R.style.ThemeOverlay_App_BottomSheetDialog_Credits
import com.waffiq.bazz_movies.feature.detail.R.layout.bottom_sheet_credits
import com.waffiq.bazz_movies.feature.detail.databinding.BottomSheetCreditsBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCredits
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CastAdapter
import com.waffiq.bazz_movies.feature.detail.ui.adapter.CrewAdapter
import com.waffiq.bazz_movies.feature.detail.ui.viewmodel.MediaDetailViewModel
import com.waffiq.bazz_movies.navigation.INavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreditsBottomSheet : BottomSheetDialogFragment(bottom_sheet_credits) {

  @Inject
  lateinit var navigator: INavigator

  private val viewModel: MediaDetailViewModel by activityViewModels()

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
    BottomSheetDialog(requireContext(), ThemeOverlay_App_BottomSheetDialog_Credits)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupBottomSheetBehavior()

    val credits = viewModel.uiState.value.detail?.credits ?: return
    val binding = BottomSheetCreditsBinding.bind(view)

    val castAdapter = CastAdapter(navigator).apply { setVerticalMode(true) }
    val crewAdapter = CrewAdapter()

    setupRecyclerView(binding, castAdapter, credits)
    setupTabs(binding, castAdapter, crewAdapter, credits)
  }

  private fun setupBottomSheetBehavior() {
    (dialog as BottomSheetDialog).behavior.apply {
      isFitToContents = true
      skipCollapsed = true
    }
  }

  private fun setupRecyclerView(
    binding: BottomSheetCreditsBinding,
    castAdapter: CastAdapter,
    credits: MediaCredits,
  ) {
    binding.rvCredits.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = castAdapter
    }
    castAdapter.submitList(credits.cast)
  }

  private fun setupTabs(
    binding: BottomSheetCreditsBinding,
    castAdapter: CastAdapter,
    crewAdapter: CrewAdapter,
    credits: MediaCredits,
  ) {
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
