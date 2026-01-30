package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

  private var genreIdList = emptyList<Int>()

  @SuppressLint("NotifyDataSetChanged")
  fun setGenre(newList: List<Int>) {
    genreIdList = newList
    notifyDataSetChanged() // fine due to small data under 1-5 items
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ChipGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(genreIdList[position])
  }

  override fun getItemCount(): Int = genreIdList.size

  class ViewHolder(private val binding: ChipGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(id: Int) {
      binding.chip.text = getGenreName(id)
    }
  }
}
