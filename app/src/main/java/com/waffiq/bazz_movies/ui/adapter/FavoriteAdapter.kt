package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.local.model.Favorite
import com.waffiq.bazz_movies.databinding.ItemMoviesBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

  private val listCast = ArrayList<Favorite>()

  fun setFavorite(itemStory: List<Favorite>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemMoviesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var getFavorite: Favorite

    fun bind(fav: Favorite) {
      getFavorite = fav
      Glide.with(binding.ivPoster)
        .load("http://image.tmdb.org/t/p/w300/" + fav.imagePath )
        .placeholder(R.mipmap.ic_launcher)
        .error(R.drawable.ic_broken_image)
        .into(binding.ivPoster)

      binding.tvTitle.text = fav.title
      binding.tvGenre.text = fav.genre
      binding.tvYearReleased.text = fav.releaseDate

      //future update
//      val resultItem = ResultItem(
//        posterPath = fav.imagePath,
//        releaseDate = fav.releaseDate,
//        overview = fav.overview,
//        title = fav.title,
//        originalTitle = fav.title,
//        genre = fav.genre,
//        id = fav.mediaId
//      )
//
//      binding.containerMovies.setOnClickListener {
//        val intent = Intent(it.context, DetailMovieActivity::class.java)
//        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, resultItem)
//        it.context.startActivity(intent)
//      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<Favorite>,
    private val newList: List<Favorite>
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