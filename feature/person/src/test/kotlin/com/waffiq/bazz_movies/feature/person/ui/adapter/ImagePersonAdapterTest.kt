package com.waffiq.bazz_movies.feature.person.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.common.utils.Constants.TMDB_IMG_LINK_POSTER_W500
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPosterBinding
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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

  @Mock
  lateinit var recyclerView: RecyclerView

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)

    adapter = ImagePersonAdapter { position, list ->
      clickedItems.add(position to list)
    }
    recyclerView.adapter = adapter
  }

  @Test
  fun `onBindViewHolder should bind data and handle clicks`() {
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }

    val testProfiles = listOf(
      ProfilesItem(filePath = "/test_path1.jpg"),
      ProfilesItem(filePath = "/test_path2.jpg")
    )

    val inflater = LayoutInflater.from(context)
    val binding = ItemPosterBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

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
      testProfiles.map { TMDB_IMG_LINK_POSTER_W500 + it.filePath },
      clickedList
    )

    // Check animation (we're ensuring the animation was set on the itemView)
    assertNotNull(viewHolder.itemView.animation)
  }


  @Test
  fun `setImage should update list and notify adapter`() {
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
}
