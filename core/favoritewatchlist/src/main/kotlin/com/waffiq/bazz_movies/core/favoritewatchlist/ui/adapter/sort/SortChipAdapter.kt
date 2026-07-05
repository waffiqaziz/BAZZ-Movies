package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter.sort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.designsystem.R.string.recently_added
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemHeaderBinding

class SortChipAdapter(private val onSortClicked: () -> Unit) :
  RecyclerView.Adapter<SortChipAdapter.ViewHolder>() {

  @StringRes
  private var currentSortRes = recently_added

  fun updateSort(@StringRes textRes: Int) {
    currentSortRes = textRes
    notifyItemChanged(0)
  }

  override fun getItemCount() = 1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(currentSortRes)
  }

  inner class ViewHolder(private val binding: ItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(@StringRes sortRes: Int) {
      binding.chipSort.setText(sortRes)
      binding.chipSort.setOnClickListener { onSortClicked() }
    }
  }
}
