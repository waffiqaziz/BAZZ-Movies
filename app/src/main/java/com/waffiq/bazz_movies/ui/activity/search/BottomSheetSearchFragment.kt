package com.waffiq.bazz_movies.ui.activity.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.waffiq.bazz_movies.R.string.maximum_selected_genres_reached
import com.waffiq.bazz_movies.R.string.binding_error
import com.waffiq.bazz_movies.data.local.model.FilterSearch
import com.waffiq.bazz_movies.databinding.FragmentBottomSheetSearchBinding
import com.waffiq.bazz_movies.utils.Helper.iterateGenreToInt
import com.waffiq.bazz_movies.utils.Helper.showToastShort

class BottomSheetSearchFragment(private val inputListener: ListenerFilter) :
  BottomSheetDialogFragment() {

  private var _binding: FragmentBottomSheetSearchBinding? = null
  private val binding get() = _binding ?: error(getString(binding_error))

  //private lateinit var viewModel: BottomSheetSearchViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentBottomSheetSearchBinding.inflate(inflater, container, false)
    val root: View = binding.root

    btnListener()
    chipGroupListener()
    selectedChipGroup()

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
      val selectedChipValues = mutableListOf<String>()
      lateinit var filterSearch: FilterSearch

      if (binding.movieTypes.isChecked) {
        for (i in 0 until binding.cgMovieGenre.childCount) {
          val chip = binding.cgMovieGenre.getChildAt(i) as Chip
          if (chip.isChecked) {
            val chipText = chip.text.toString()
            selectedChipValues.add(chipText)
          }
        }
      } else if (binding.tvTypes.isChecked) {
        for (i in 0 until binding.cgTvGenre.childCount) {
          val chip = binding.cgTvGenre.getChildAt(i) as Chip
          if (chip.isChecked) {
            val chipText = chip.text.toString()
            selectedChipValues.add(chipText)
          }
        }
      } else if (binding.multiTypes.isChecked) {
        filterSearch = FilterSearch(1, null, null)
      }

      inputListener.passData("BISMILLAH")
      showToastShort(requireActivity(), filterSearch.toString())
      showToastShort(requireActivity(), iterateGenreToInt(selectedChipValues))
      dismiss()
    }
  }

  private fun selectedChipGroup() {
    binding.multiTypes.isChecked = true
    binding.containerGenre.visibility = View.GONE

    // hide genre chips available for multi and person search
    binding.multiTypes.setOnClickListener {
      binding.containerGenre.visibility = View.GONE
    }
    binding.personTypes.setOnClickListener {
      binding.containerGenre.visibility = View.GONE
    }

    binding.movieTypes.setOnClickListener {
      binding.containerGenre.visibility = View.VISIBLE
      binding.cgMovieGenre.visibility = View.VISIBLE
      binding.cgTvGenre.visibility = View.GONE
    }

    binding.tvTypes.setOnClickListener {
      binding.containerGenre.visibility = View.VISIBLE
      binding.cgTvGenre.visibility = View.VISIBLE
      binding.cgMovieGenre.visibility = View.GONE
    }
  }


  private fun chipGroupListener() {
    var selectedChipCount: Byte = 0

    binding.cgMovieGenre.children.forEach { view ->
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

    var selectedChipCount2: Byte = 0
    binding.cgMovieGenre.children.forEach { view ->
      if (view is Chip) {
        view.setOnCheckedChangeListener { chip, isChecked ->
          if (isChecked) {
            if (selectedChipCount2 >= MAX_SELECTED_CHIPS) {
              // If maximum selected chips reached, uncheck the chip and show toast
              chip.isChecked = false
              showToastShort(
                requireActivity(),
                getString(maximum_selected_genres_reached)
              )
            } else selectedChipCount2++
          } else selectedChipCount2--
        }
      }
    }
  }

  interface ListenerFilter {
    fun passData(data: Any)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {
    const val MAX_SELECTED_CHIPS = 3
  }

}