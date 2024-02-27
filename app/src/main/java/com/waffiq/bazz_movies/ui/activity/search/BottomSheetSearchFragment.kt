package com.waffiq.bazz_movies.ui.activity.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.waffiq.bazz_movies.R.color.cream_alpha
import com.waffiq.bazz_movies.R.color.bg_chip
import com.waffiq.bazz_movies.R.color.gray_200
import com.waffiq.bazz_movies.R.string.maximum_selected_genres_reached
import com.waffiq.bazz_movies.databinding.FragmentBottomSheetSearchBinding
import com.waffiq.bazz_movies.utils.Helper.showToastShort

class BottomSheetSearchFragment : BottomSheetDialogFragment() {

  private var _binding: FragmentBottomSheetSearchBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: BottomSheetSearchViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentBottomSheetSearchBinding.inflate(inflater, container, false)
    val root: View = binding.root

    btnListener()
    chipGroupListener()
    chipListener()

    // Fixes bottom sheet not fully expanded inside its parent view
    requireDialog().setOnShowListener {
      val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
      bottomSheetBehavior.isHideable = false
      val bottomSheetParent = binding.bottomSheetParent
      BottomSheetBehavior.from(bottomSheetParent.parent as View).peekHeight =
        bottomSheetParent.height
      bottomSheetBehavior.peekHeight = bottomSheetParent.height
      bottomSheetParent.parent.requestLayout()
    }

    return root
  }

  private fun btnListener() {
    binding.btnApply.setOnClickListener {
      val selectedChip = binding.cgGenre.children
        .filter { (it as Chip).isChecked }
        .map { (it as Chip).text }.toString()

      showToastShort(requireActivity(),selectedChip)
      dismiss()
    }
  }

  private fun chipListener() {
    binding.apply {
      multiTypes.isChecked = true
      cgGenre.children.forEach { view ->
        if (view is Chip) {
          view.chipBackgroundColor = requireActivity().getColorStateList(cream_alpha)
          view.isClickable = false
        }
      }

      movieTypes.setOnClickListener {
        cgGenre.children.forEach { view ->
          if (view is Chip) {
            view.chipBackgroundColor = requireActivity().getColorStateList(bg_chip)
            view.isClickable = true
          }
        }
      }
      tvTypes.setOnClickListener {
        cgGenre.children.forEach { view ->
          if (view is Chip) {
            view.chipBackgroundColor = requireActivity().getColorStateList(bg_chip)
            view.isClickable = true
          }
        }
      }
      multiTypes.setOnClickListener {
        cgGenre.children.forEach { view ->
          if (view is Chip) {
            view.chipBackgroundColor = requireActivity().getColorStateList(cream_alpha)
            view.isClickable = false
          }
        }
      }
      personTypes.setOnClickListener {
        cgGenre.children.forEach { view ->
          if (view is Chip) {
            view.chipBackgroundColor = requireActivity().getColorStateList(gray_200)
            view.isClickable = false
          }
        }
      }
    }
  }

  private fun chipGroupListener() {
    var selectedChipCount = 0

    binding.cgGenre.children.forEach { view ->
      if (view is Chip) {
        view.setOnCheckedChangeListener { chip, isChecked ->
          if (isChecked) {
            if (selectedChipCount >= MAX_SELECTED_CHIPS) {
              // If maximum selected chips reached, uncheck the chip and show toast
              chip.isChecked = false
              showToastShort(
                requireActivity(),
                getString(maximum_selected_genres_reached)
              )
            } else selectedChipCount++
          } else selectedChipCount--
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    const val MAX_SELECTED_CHIPS = 3
  }

}