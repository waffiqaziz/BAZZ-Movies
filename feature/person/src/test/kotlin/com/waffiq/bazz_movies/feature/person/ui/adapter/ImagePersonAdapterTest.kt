package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W1280
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W185
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImagePersonAdapterTest {
  val clickedItems = mutableListOf<Pair<Int, List<String>>>()
  private lateinit var adapter: ImagePersonAdapter
  private lateinit var context: Context

  @Mock
  lateinit var recyclerView: RecyclerView

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)

    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }
    adapter = ImagePersonAdapter { position, list ->
      clickedItems.add(position to list)
    }
    recyclerView.adapter = adapter
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsDataAndHandlesClicks() {
    val testProfiles = listOf(
      ProfilesItem(filePath = "/test_path1.jpg"),
      ProfilesItem(filePath = "/test_path2.jpg")
    )

    val inflater = LayoutInflater.from(context)
    val binding = ItemPosterBinding.inflate(inflater, null, false)
    val viewHolder = ImagePersonAdapter.ViewHolder(binding)

    // Set the test data
    adapter.setImage(testProfiles)
    adapter.onBindViewHolder(viewHolder, 0)

    // Simulate click on the root view
    binding.root.performClick()

    // Verify click listener logic
    assertEquals(1, clickedItems.size)
    val (clickedPosition, clickedList) = clickedItems[0]
    assertEquals(0, clickedPosition)
    assertEquals(
      testProfiles.map { TMDB_IMG_LINK_POSTER_W1280 + it.filePath },
      clickedList
    )

    // check animation set on the itemView
    assertNotNull(viewHolder.itemView.animation)
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val onItemClick: (Int, List<String>) -> Unit = { _, _ -> }
    val adapter = ImagePersonAdapter(onItemClick)

    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun setImage_whenCalled_updatesListAndNotifiesAdapter() {
    val initialProfiles = listOf(
      ProfilesItem(filePath = "/path1.jpg"),
      ProfilesItem(filePath = "/path2.jpg"),
    )
    val newProfiles = listOf(
      ProfilesItem(filePath = "/path3.jpg"),
      ProfilesItem(filePath = "/path4.jpg"),
      ProfilesItem(filePath = "/path5.jpg")
    )
    val adapter = ImagePersonAdapter { _, _ -> }

    // Set initial data
    adapter.setImage(initialProfiles)
    assertEquals(2, adapter.itemCount)

    // Update data
    adapter.setImage(newProfiles)
    assertEquals(3, adapter.itemCount)
    assertEquals(newProfiles, adapter.getListCast())
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemPosterBinding.inflate(LayoutInflater.from(context))
    val viewHolder = ImagePersonAdapter.ViewHolder(binding)

    val testCases = listOf(
      ProfilesItem(filePath = "valid_image.jpg") to (TMDB_IMG_LINK_POSTER_W185 + "valid_image.jpg"),
      ProfilesItem(filePath = null) to null, // use `ic_poster_error`
      ProfilesItem(filePath = "") to null // use `ic_poster_error`
    )

    testCases.forEach { (profileItem, expectedUrl) ->
      viewHolder.bind(profileItem)

      // expect the tag has correct URL (or null for error case)
      assertEquals(expectedUrl, binding.imgPoster.tag)
    }
  }

  @Test
  fun areContentsTheSame_whenFilePathAreTheSame_returnsTrue() {
    val oldItem = ProfilesItem(filePath = "image1.jpg")
    val newItem = ProfilesItem(filePath = "image1.jpg") // same content

    val diffCallback = ImagePersonAdapter.DiffCallback(listOf(oldItem), listOf(newItem))
    assertTrue(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenDifferentFilePath_returnsFalse() {
    val oldItem = ProfilesItem(filePath = "image1.jpg")
    val newItem = ProfilesItem(filePath = "image2.jpg") // different content

    val diffCallback = ImagePersonAdapter.DiffCallback(listOf(oldItem), listOf(newItem))
    assertFalse(diffCallback.areContentsTheSame(0, 0))
  }
}
