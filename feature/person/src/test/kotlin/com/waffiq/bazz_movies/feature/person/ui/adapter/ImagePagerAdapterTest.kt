package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemImageSliderBinding
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImagePagerAdapterTest {

  private lateinit var context: Context
  private lateinit var parentView: FrameLayout

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    parentView = FrameLayout(context)
  }

  @Test
  fun onBindViewHolder_bindsDataAndAppliesAnimation() {
    val adapter = ImagePagerAdapter(images = listOf("image1"))
    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemImageSliderBinding.inflate(inflater, parentView, false)
    val viewHolder = adapter.ImageViewHolder(binding)
    adapter.onBindViewHolder(viewHolder, 0)

    // expect the placeholder image is displayed
    // check the state of ImageView without mocking Glide
    val drawable = binding.imageViewSlider.drawable
    assertNotNull(drawable) // Ensures the image is loaded

    // validate binding logic
    assertNotNull(viewHolder.itemView.animation)
  }

  @Test
  fun onCreateViewHolder_createsViewHolderCorrectly() {
    val adapter = ImagePagerAdapter(images = emptyList())
    val viewHolder = adapter.onCreateViewHolder(parentView, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parentView.context, viewHolder.itemView.context)
  }

  @Test
  fun getItemCount_returnsCorrectSize() {
    val images = listOf("image1", "image2", "image3")
    val adapter = ImagePagerAdapter(images)

    assertEquals(images.size, adapter.itemCount) // 3
  }
}
