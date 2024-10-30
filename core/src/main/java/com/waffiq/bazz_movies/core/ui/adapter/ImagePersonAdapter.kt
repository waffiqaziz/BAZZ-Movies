package com.waffiq.bazz_movies.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.domain.model.person.ProfilesItem
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.ui.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W500

class ImagePersonAdapter(private val onItemClick: (Int, List<String>) -> Unit) :
  RecyclerView.Adapter<ImagePersonAdapter.ViewHolder>() {

  private val listCast = ArrayList<ProfilesItem>()

  fun setImage(itemPerson: List<ProfilesItem>) {
    val diffCallback = DiffCallback(this.listCast, itemPerson)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemPerson)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemPosterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )

    // on click listener to open image inside dialog
    holder.itemView.setOnClickListener {
      onItemClick.invoke(
        position,
        listCast.map { TMDB_IMG_LINK_POSTER_W500 + it.filePath.toString() }
      )
    }
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemPosterBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: ProfilesItem) {
      Glide.with(binding.imgPoster)
        .load(
          if (!cast.filePath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + cast.filePath
          } else {
            ic_poster_error
          }
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
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
