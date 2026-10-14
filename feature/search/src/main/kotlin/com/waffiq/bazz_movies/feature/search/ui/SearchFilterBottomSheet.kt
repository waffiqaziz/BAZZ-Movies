@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.search.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_alpha_20
import com.waffiq.bazz_movies.core.designsystem.R.dimen.stroke
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_tick
import com.waffiq.bazz_movies.core.designsystem.R.font.nunito_sans_bold
import com.waffiq.bazz_movies.core.model.media.MediaType
import com.waffiq.bazz_movies.feature.search.R.color.selector_chip_background
import com.waffiq.bazz_movies.feature.search.R.color.selector_chip_stroke
import com.waffiq.bazz_movies.feature.search.R.color.selector_chip_text
import com.waffiq.bazz_movies.feature.search.R.layout.fragment_search_filter
import com.waffiq.bazz_movies.feature.search.databinding.FragmentSearchFilterBinding
import com.waffiq.bazz_movies.feature.search.utils.labelRes
import com.waffiq.bazz_movies.feature.search.utils.toMediaTypeSet

class SearchFilterBottomSheet : BottomSheetDialogFragment(fragment_search_filter) {

  override fun onStart() {
    super.onStart()
    dialog?.window?.let { enableEdgeToEdge(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = FragmentSearchFilterBinding.bind(view)
    val preselected = arguments?.getStringArrayList(ARG_SELECTED).toMediaTypeSet()
    val chipIdToType = mutableMapOf<Int, MediaType>()

    SELECTABLE_TYPES.forEach { filterType ->
      val chip = createFilterChip(filterType, preselected)
      chipIdToType[chip.id] = filterType
      binding.chipGroupFilters.addView(chip)
    }

    binding.btnApply.setOnClickListener {
      val selected = binding.chipGroupFilters.checkedChipIds
        .mapNotNull { chipIdToType[it] }
        .toSet()
        .ifEmpty { setOf(MediaType.MULTI) }

      setFragmentResult(
        REQUEST_FILTER_RESULT,
        Bundle().apply { putStringArrayList(RESULT_SELECTED, ArrayList(selected.map { it.name })) },
      )
      dismiss()
    }
  }

  private fun createFilterChip(filterType: MediaType, preselected: Set<MediaType>) =
    Chip(requireContext()).apply {
      id = View.generateViewId()
      text = getString(filterType.labelRes())

      isCheckable = true
      isCheckedIconVisible = true
      isChecked = filterType in preselected

      chipBackgroundColor = getColor(selector_chip_background)
      chipStrokeColor = getColor(selector_chip_stroke)
      chipStrokeWidth = resources.getDimension(stroke)
      rippleColor = getColor(yellow_alpha_20)

      checkedIcon = ContextCompat.getDrawable(requireContext(), ic_tick)

      setTextColor(getColor(selector_chip_text))
      typeface = ResourcesCompat.getFont(requireContext(), nunito_sans_bold)
      textSize = TEXT_SIZE
    }

  private fun getColor(@ColorRes id: Int) = ContextCompat.getColorStateList(requireContext(), id)

  companion object {
    const val REQUEST_FILTER_RESULT = "search_filter_result"
    const val RESULT_SELECTED = "result_selected"
    private const val ARG_SELECTED = "arg_selected"

    private val SELECTABLE_TYPES = listOf(MediaType.PERSON, MediaType.MOVIE, MediaType.TV)
    private const val TEXT_SIZE = 12f

    fun newInstance(selected: Set<MediaType>): SearchFilterBottomSheet =
      SearchFilterBottomSheet().apply {
        arguments = Bundle().apply {
          putStringArrayList(ARG_SELECTED, ArrayList(selected.map { it.name }))
        }
      }
  }
}
