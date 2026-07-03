package com.waffiq.bazz_movies.core.uihelper.ui.adapter

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.listitem.ListItemViewHolder
import com.waffiq.bazz_movies.core.common.utils.Constants.DEBOUNCE_SHORT
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_400
import com.waffiq.bazz_movies.core.designsystem.R.color.gray_800
import com.waffiq.bazz_movies.core.designsystem.R.color.white
import com.waffiq.bazz_movies.core.designsystem.R.color.yellow_alpha_9
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemSingleChoiceBinding
import com.waffiq.bazz_movies.core.uihelper.model.LabeledOption

class SingleChoiceAdapter<T : LabeledOption>(
  private val items: List<T>,
  selected: T,
  private val onSelected: (T) -> Unit,
  private val debounceMs: Long = DEBOUNCE_SHORT,
  private val postDelayed: (Long, () -> Unit) -> Unit = { delay, action ->
    Handler(Looper.getMainLooper()).postDelayed(action, delay)
  },
) : RecyclerView.Adapter<SingleChoiceAdapter<T>.ViewHolder>() {

  private var selectedPosition = items.indexOf(selected)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      ItemSingleChoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.onBind(items[position], position, position == selectedPosition)
  }

  override fun getItemCount() = items.size

  inner class ViewHolder(val binding: ItemSingleChoiceBinding) :
    ListItemViewHolder(binding.listItemLayout) {

    val textView: TextView = binding.tvLabel

    fun onBind(
      item: T,
      position: Int,
      isSelected: Boolean,
    ) {
      bind(position, itemCount)

      binding.item.setCardBackgroundColor(
        ContextCompat.getColor(
          binding.item.context,
          if (isSelected) yellow_alpha_9 else gray_800,
        ),
      )
      binding.ivSelected.isVisible = isSelected
      textView.apply {
        text = context.getString(item.label)
        setTypeface(typeface, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        setTextColor(ContextCompat.getColor(context, if (isSelected) white else gray_400))
        updatePadding(
          top = if (isSelected) 10 else -3,
          bottom = if (isSelected) 10 else -3,
        )
      }

      binding.item.setOnClickListener {
        val previous = selectedPosition
        selectedPosition = position

        notifyItemChanged(previous)
        notifyItemChanged(selectedPosition)

        postDelayed(debounceMs) {
          onSelected(item)
        }
      }
    }
  }
}
