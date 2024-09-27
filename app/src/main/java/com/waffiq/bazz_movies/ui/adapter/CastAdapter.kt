package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.R.drawable.ic_broken_image
import com.waffiq.bazz_movies.R.drawable.ic_no_profile_rounded
import com.waffiq.bazz_movies.data.remote.responses.tmdb.detail_movie_tv.cast_crew.MovieTvCastItemResponse
import com.waffiq.bazz_movies.databinding.ItemCastBinding
import com.waffiq.bazz_movies.ui.activity.person.PersonActivity
import com.waffiq.bazz_movies.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W300

class CastAdapter : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

  private val listCast = ArrayList<MovieTvCastItemResponse>()

  fun setCast(itemCast: List<MovieTvCastItemResponse>) {
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

    fun bind(cast: MovieTvCastItemResponse) {
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
        val intent = Intent(it.context, PersonActivity::class.java)
        intent.putExtra(PersonActivity.EXTRA_PERSON, cast)
        it.context.startActivity(intent)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<MovieTvCastItemResponse>,
    private val newList: List<MovieTvCastItemResponse>
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
