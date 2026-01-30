package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.feature.detail.databinding.ChipGenreBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

  private val genreIdList = ArrayList<Int>()

  fun setGenre(newList: List<Int>) {
    val diffCallback = DiffCallback(this.genreIdList, newList)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.genreIdList.clear()
    this.genreIdList.addAll(newList)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ChipGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(genreIdList[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
    )
  }

  override fun getItemCount(): Int = genreIdList.size

  class ViewHolder(private val binding: ChipGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(id: Int) {
      binding.chip.text = getGenreName(id)
    }
  }

  class DiffCallback(
    private val oldList: List<Int>,
    private val newList: List<Int>,
  ) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition] == newList[newItemPosition]
  }
}
