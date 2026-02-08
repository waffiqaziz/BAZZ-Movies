package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.R.anim.fade_in
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_bazz_placeholder_poster
import com.waffiq.bazz_movies.core.designsystem.R.drawable.ic_poster_error
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPlayForBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.navigation.INavigator

class KnownForAdapter(private val navigator: INavigator) :
  RecyclerView.Adapter<KnownForAdapter.ViewHolder>() {

  private val listCast = ArrayList<CastItem>()

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun getListCast(): List<CastItem> = listCast

  fun setCast(itemCast: List<CastItem>) {
    val diffCallback = DiffCallback(this.listCast, itemCast)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemCast)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemPlayForBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(holder.itemView.context, fade_in),
    )
  }

  override fun getItemCount(): Int = listCast.size

  inner class ViewHolder(private var binding: ItemPlayForBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastItem) {
      binding.imgCastPhoto.contentDescription = cast.title
      binding.imgCastPhoto.tag = cast.id

      Glide.with(binding.imgCastPhoto)
        .load(
          if (!cast.posterPath.isNullOrEmpty()) {
            TMDB_IMG_LINK_POSTER_W185 + cast.posterPath
          } else {
            ic_poster_error
          },
        )
        .placeholder(ic_bazz_placeholder_poster)
        .transform(CenterCrop())
        .transition(withCrossFade())
        .error(ic_poster_error)
        .into(binding.imgCastPhoto)

      binding.tvCastName.text = cast.name ?: cast.title ?: cast.originalName ?: cast.originalTitle
      binding.tvCastCharacter.text = cast.character ?: itemView.context.getString(not_available)

      val mediaItem = MediaItem(
        overview = cast.overview,
        title = cast.title,
        name = cast.name,
        originalTitle = cast.originalTitle,
        originalName = cast.originalTitle,
        mediaType = cast.mediaType,
        firstAirDate = cast.releaseDate,
        releaseDate = cast.releaseDate,
        listGenreIds = cast.listGenreIds,
        id = cast.id,
        popularity = cast.popularity,
        voteAverage = cast.voteAverage,
        voteCount = cast.voteCount,
        posterPath = cast.posterPath,
        backdropPath = cast.backdropPath,
      )

      // OnClickListener
      binding.container.setOnClickListener {
        navigator.openDetails(itemView.context, mediaItem)
      }
    }
  }

  class DiffCallback(private val oldList: List<CastItem>, private val newList: List<CastItem>) :
    DiffUtil.Callback() {

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
