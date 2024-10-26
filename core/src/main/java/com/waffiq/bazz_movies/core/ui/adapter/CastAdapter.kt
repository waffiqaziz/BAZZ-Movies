package com.waffiq.bazz_movies.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.core_ui.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.core_ui.databinding.ItemCastBinding
import com.waffiq.bazz_movies.core.domain.model.person.MovieTvCastItem
import com.waffiq.bazz_movies.core.navigation.PersonNavigator
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W300

class CastAdapter(private val personNavigator: PersonNavigator) :
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
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount() = listCast.size

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
      binding.tvCastCharacter.text =
        if (!cast.character.isNullOrEmpty() && cast.character.isNotBlank()) cast.character else "TBA"

      // image OnClickListener
      binding.container.setOnClickListener {
        personNavigator.openPersonDetails(cast)
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
