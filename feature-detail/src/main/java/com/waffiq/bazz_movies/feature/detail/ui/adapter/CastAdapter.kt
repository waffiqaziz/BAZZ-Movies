package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.domain.model.MovieTvCastItem
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core.ui.databinding.ItemCastBinding
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.navigation.Navigator

class CastAdapter(private val navigator: Navigator) :
  RecyclerView.Adapter<CastAdapter.ViewHolder>() {

  private val listCast = ArrayList<MovieTvCastItem>()

  fun setCast(itemCast: List<MovieTvCastItem>) {
    val diffCallback = DiffCallback(this.listCast, itemCast)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemCast)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in)
    )
  }

  override fun getItemCount(): Int = listCast.size

  inner class ViewHolder(private var binding: ItemCastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: MovieTvCastItem) {
      binding.imgCastPhoto.contentDescription = cast.name

      Glide.with(binding.imgCastPhoto)
        .load(
          if (!cast.profilePath.isNullOrEmpty()) {
            TMDB_IMG_LINK_BACKDROP_W300 + cast.profilePath
          } else {
            ic_no_profile_rounded
          }
        )
        .placeholder(ic_no_profile_rounded)
        .transition(withCrossFade())
        .error(ic_broken_image)
        .into(binding.imgCastPhoto)

      binding.tvCastName.text = cast.name ?: cast.originalName
      binding.tvCastCharacter.text = cast.character?.takeIf { it.isNotBlank() } ?: "TBA"

      // image OnClickListener
      binding.container.setOnClickListener {
        navigator.openPersonDetails(itemView.context, cast)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<MovieTvCastItem>,
    private val newList: List<MovieTvCastItem>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldEmployee = oldList[oldItemPosition]
      val newEmployee = newList[newItemPosition]
      return oldEmployee.id == newEmployee.id
    }
  }
}
