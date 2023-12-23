package com.waffiq.bazz_movies.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.FavoriteDB
import com.waffiq.bazz_movies.data.remote.response.tmdb.ResultItem
import com.waffiq.bazz_movies.databinding.ItemResultBinding
import com.waffiq.bazz_movies.ui.activity.detail.DetailMovieActivity
import com.waffiq.bazz_movies.utils.Constants.TMDB_IMG_LINK_BACKDROP_W300

class FavoriteAdapterDB : RecyclerView.Adapter<FavoriteAdapterDB.ViewHolder>() {

  private val listCast = ArrayList<FavoriteDB>()

  fun setFavorite(itemStory: List<FavoriteDB>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemResultBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(fav: FavoriteDB) {
      Glide.with(binding.ivPicture)
        .load(TMDB_IMG_LINK_BACKDROP_W300 + fav.imagePath )
        .placeholder(R.mipmap.ic_launcher)
        .error(R.drawable.ic_broken_image)
        .into(binding.ivPicture)

      binding.tvTitle.text = fav.title
      binding.tvGenre.text = fav.genre
      binding.tvYearReleased.text = fav.releaseDate

      val resultItem = ResultItem(
        posterPath = fav.imagePath,
        releaseDate = fav.releaseDate,
        overview = fav.overview,
        title = fav.title,
        originalTitle = fav.title,
        id = fav.mediaId
      )

      binding.containerResult.setOnClickListener {
        val intent = Intent(it.context, DetailMovieActivity::class.java)
        resultItem.mediaType = fav.mediaType
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
        it.context.startActivity(intent)
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<FavoriteDB>,
    private val newList: List<FavoriteDB>
  ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].mediaId == newList[newItemPosition].mediaId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldEmployee = oldList[oldItemPosition]
      val newEmployee = newList[newItemPosition]
      return oldEmployee.mediaId == newEmployee.mediaId
    }
  }
}