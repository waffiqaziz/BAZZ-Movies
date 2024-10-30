package com.waffiq.bazz_movies.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.waffiq.bazz_movies.core.domain.model.Favorite
import com.waffiq.bazz_movies.core.domain.model.ResultItem
import com.waffiq.bazz_movies.core.navigation.DetailNavigator
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_backdrop_error
import com.waffiq.bazz_movies.core.ui.R.drawable.ic_bazz_placeholder_search
import com.waffiq.bazz_movies.core.ui.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_BACKDROP_W300
import com.waffiq.bazz_movies.core.utils.common.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.utils.helpers.GeneralHelper.dateFormatterStandard

class FavoriteAdapterDB(private val detailNavigator: DetailNavigator) :
  RecyclerView.Adapter<FavoriteAdapterDB.ViewHolder>() {

  private val listItemDB = ArrayList<Favorite>()

  fun setFavorite(itemFavorite: List<Favorite>) {
    val diffCallback = DiffCallback(this.listItemDB, itemFavorite)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listItemDB.clear()
    this.listItemDB.addAll(itemFavorite)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listItemDB[position])
    holder.itemView.startAnimation(
      AnimationUtils.loadAnimation(
        holder.itemView.context,
        android.R.anim.fade_in
      )
    )
  }

  override fun getItemCount() = listItemDB.size

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var data: Favorite

    fun bind(fav: Favorite) {
      data = fav
      binding.ivPicture.contentDescription = fav.title

      Glide.with(binding.ivPicture)
        .load(
          if (fav.backDrop != "N/A") {
            TMDB_IMG_LINK_BACKDROP_W300 + fav.backDrop
          } else if (fav.poster != "N/A") {
            TMDB_IMG_LINK_POSTER_W185 + fav.poster
          } else {
            ic_backdrop_error
          }
        )
        .placeholder(ic_bazz_placeholder_search)
        .transition(withCrossFade())
        .error(ic_backdrop_error)
        .into(binding.ivPicture)

      binding.tvTitle.text = fav.title
      binding.tvGenre.text = fav.genre
      binding.tvYearReleased.text = dateFormatterStandard(fav.releaseDate).ifEmpty { "N/A" }

      val resultItem = ResultItem(
        backdropPath = fav.backDrop,
        posterPath = fav.poster,
        releaseDate = fav.releaseDate,
        overview = fav.overview,
        title = fav.title,
        originalTitle = fav.title,
        mediaType = fav.mediaType,
        id = fav.mediaId
      )

      binding.containerResult.setOnClickListener {
        detailNavigator.openDetails(resultItem)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<Favorite>,
    private val newList: List<Favorite>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      (
        oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId &&
          oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite &&
          oldList[oldItemPosition].isWatchlist == newList[newItemPosition].isWatchlist &&
          oldList[oldItemPosition].mediaType == newList[newItemPosition].mediaType
        )

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      (
        oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId &&
          oldList[oldItemPosition].isFavorite == newList[newItemPosition].isFavorite &&
          oldList[oldItemPosition].isWatchlist == newList[newItemPosition].isWatchlist &&
          oldList[oldItemPosition].mediaType == newList[newItemPosition].mediaType
        )
  }
}
