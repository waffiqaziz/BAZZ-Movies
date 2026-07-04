package com.waffiq.bazz_movies.core.uihelper.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waffiq.bazz_movies.core.designsystem.R.style.ThemeOverlay_App_AlertDialog_Sort
import com.waffiq.bazz_movies.core.designsystem.databinding.DialogSingleChoiceBinding
import com.waffiq.bazz_movies.core.uihelper.model.LabeledOption
import com.waffiq.bazz_movies.core.uihelper.ui.adapter.SingleChoiceAdapter

object SingleChoiceDialog {

  fun <T : LabeledOption> show(
    context: Context,
    @StyleRes style: Int = ThemeOverlay_App_AlertDialog_Sort,
    items: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
  ) {
    val binding = DialogSingleChoiceBinding.inflate(LayoutInflater.from(context))
    val dialog = MaterialAlertDialogBuilder(context, style)
      .setView(binding.root)
      .create()

    binding.rvOptions.layoutManager = LinearLayoutManager(context)
    binding.rvOptions.adapter = SingleChoiceAdapter(
      items = items,
      selected = selected,
      onSelected = {
        onSelected(it)
        dialog.dismiss()
      },
    )

    dialog.show()
  }
}
