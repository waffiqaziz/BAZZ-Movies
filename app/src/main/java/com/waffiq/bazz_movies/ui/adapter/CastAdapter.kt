package com.waffiq.bazz_movies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.waffiq.bazz_movies.R
import com.waffiq.bazz_movies.data.remote.response.CastItem
import com.waffiq.bazz_movies.databinding.ItemCastBinding

class CastAdapter : RecyclerView.Adapter<CastAdapter.ViewHolder>() {

  private val listCast = ArrayList<CastItem>()

  fun setCast(itemStory: List<CastItem>) {
    val diffCallback = DiffCallback(this.listCast, itemStory)
    val diffResult = DiffUtil.calculateDiff(diffCallback)

    this.listCast.clear()
    this.listCast.addAll(itemStory)
    diffResult.dispatchUpdatesTo(this)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(listCast[position])
  }

  override fun getItemCount() = listCast.size

  inner class ViewHolder(private var binding: ItemCastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: CastItem) {
      with(binding) {
        Glide.with(imgCastPhoto)
          .load("http://image.tmdb.org/t/p/w300/" + cast.profilePath )
          .placeholder(R.drawable.ic_bazz_placeholder_poster)
          .transform(CenterCrop())
          .transition(DrawableTransitionOptions.withCrossFade())
          .error(R.drawable.ic_broken_image)
          .into(imgCastPhoto)

        tvCastName.text = cast.name?: cast.originalName
        tvCastCharacter.text = cast.character
        // image OnClickListener
//        imgCastPhoto.setOnClickListener {
//          val intent = Intent(it.context, DetailCastActivity::class.java)
//          intent.putExtra(DetailCastActivity.EXTRA_CAST, cast)
//          it.context.startActivity(intent)
//        }
      }
    }
  }

  inner class DiffCallback(
    private val oldList: List<CastItem>,
    private val newList: List<CastItem>
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