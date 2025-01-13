package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemImageSliderBinding
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImagePagerAdapterTest {

  @Test
  fun onBindViewHolder_bindsDataAndAppliesAnimation() {
    val images = listOf("image1")
    val adapter = ImagePagerAdapter(images)

    // Use a real ViewGroup as the parent
    val parent = FrameLayout(ApplicationProvider.getApplicationContext())

    // Inflate the binding
    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemImageSliderBinding.inflate(inflater, parent, false)

    // Create and bind ViewHolder
    val viewHolder = adapter.ImageViewHolder(binding)
    adapter.onBindViewHolder(viewHolder, 0)

    // Assert that the placeholder image is displayed (example)
    // check the state of ImageView without mocking Glide
    val drawable = binding.imageViewSlider.drawable
    assertNotNull(drawable) // Ensures the image is loaded

    // Validate binding logic
    assertNotNull(viewHolder.itemView.animation)
  }

  @Test
  fun getItemCount_returnsCorrectSize() {
    val images = listOf("image1", "image2", "image3")
    val adapter = ImagePagerAdapter(images)

    assertEquals(images.size, adapter.itemCount) // 3
  }
}
