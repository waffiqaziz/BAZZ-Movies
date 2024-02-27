package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.waffiq.bazz_movies.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.data.remote.response.tmdb.ProfilesItem
import com.waffiq.bazz_movies.databinding.ItemTrendingBinding
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_POSTER_W185

class ImagePersonAdapter : RecyclerView.Adapter<ImagePersonAdapter.ViewHolder>() {

  private val listCast = ArrayList<ProfilesItem>()

  fun setImage(itemStory: List<ProfilesItem>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemTrendingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: ProfilesItem) {
      Glide.with(binding.imgPoster)
        .load(TMDB_IMG_LINK_POSTER_W185 + cast.filePath)
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgPoster)
    }
  }

  inner class DiffCallback(
    private val oldList: List<ProfilesItem>,
    private val newList: List<ProfilesItem>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].filePath == newList[newItemPosition].filePath

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldEmployee = oldList[oldItemPosition]
      val newEmployee = newList[newItemPosition]
      return oldEmployee.filePath == newEmployee.filePath
    }
  }
}