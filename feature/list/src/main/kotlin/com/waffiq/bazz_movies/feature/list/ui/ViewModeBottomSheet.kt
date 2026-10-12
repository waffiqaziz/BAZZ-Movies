@file:Suppress("BackingPropertyNaming")

package com.waffiq.bazz_movies.feature.list.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_200
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_300
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_800
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_900
import com.waffiq.bazz_movies.core.designsystem.R.color.white
import com.waffiq.bazz_movies.feature.list.databinding.BottomSheetViewModeBinding
import com.waffiq.bazz_movies.feature.list.databinding.ItemViewModeOptionBinding
import com.waffiq.bazz_movies.feature.list.domain.model.ListViewMode

class ViewModeBottomSheet : BottomSheetDialogFragment() {

  private var _binding: BottomSheetViewModeBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    _binding = BottomSheetViewModeBinding.inflate(inflater, null, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val current = ListViewMode.valueOf(requireArguments().getString(ARG_MODE)!!)

    val options = mapOf(
      ListViewMode.TWO_COLUMNS to binding.optionTwoColumns,
      ListViewMode.THREE_COLUMNS to binding.optionThreeColumns,
      ListViewMode.DETAILED to binding.optionDetailed,
    )

    options.forEach { (mode, row) -> bindOption(row, mode, selected = mode == current) }
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.let { enableEdgeToEdge(it) }
  }

  private fun bindOption(
    row: ItemViewModeOptionBinding,
    mode: ListViewMode,
    selected: Boolean,
  ) {
    row.icon.setImageResource(mode.icon)
    row.title.setText(mode.title)
    row.subtitle.setText(mode.subtitle)

    // change color based on selected
    if (selected) {
      ImageViewCompat.setImageTintList(
        row.icon,
        ColorStateList.valueOf(resources.getColor(gray_900, null)),
      )
      row.title.setTextColor(resources.getColor(gray_900, null))
      row.subtitle.setTextColor(resources.getColor(gray_800, null))
    } else {
      ImageViewCompat.setImageTintList(
        row.icon,
        ColorStateList.valueOf(resources.getColor(gray_200, null)),
      )
      row.title.setTextColor(resources.getColor(gray_200, null))
      row.subtitle.setTextColor(resources.getColor(gray_300, null))
    }

    row.check.isVisible = selected
    row.card.setCardBackgroundColor(
      if (selected) {
        resources.getColor(white, null)
      } else {
        Color.TRANSPARENT
      },
    )
    row.card.setOnClickListener {
      parentFragmentManager.setFragmentResult(
        REQUEST_KEY,
        Bundle().apply { putString(RESULT_MODE, mode.name) },
      )
      dismiss()
    }
  }

  override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
  }

  companion object {
    const val TAG = "ViewModeBottomSheet"
    const val REQUEST_KEY = "view_mode_request"
    const val RESULT_MODE = "view_mode_result"
    private const val ARG_MODE = "arg_mode"

    fun newInstance(current: ListViewMode) =
      ViewModeBottomSheet().apply {
        arguments = Bundle().apply {
          putString(ARG_MODE, current.name)
        }
      }
  }
}
