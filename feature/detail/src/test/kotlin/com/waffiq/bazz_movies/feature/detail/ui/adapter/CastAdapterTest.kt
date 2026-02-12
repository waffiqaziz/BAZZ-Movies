package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemCastBinding
import com.waffiq.bazz_movies.core.domain.MediaCastItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf

class CastAdapterTest : BaseAdapterTest() {
  lateinit var navigator: INavigator

  private lateinit var adapter: CastAdapter

  @Before
  fun setup() {
    super.baseSetup()
    navigator = mockk(relaxed = true)
    adapter = CastAdapter(navigator)
    recyclerView.adapter = adapter
  }

  @Test
  fun submitList_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(MediaCastItem(id = 1, name = "Old Cast"))
    val newList = listOf(
      MediaCastItem(id = 2, name = "New Cast"),
      MediaCastItem(id = 3, name = "No Cast")
    )

    adapter.submitList(oldList) {
      assertEquals(1, adapter.itemCount)

      adapter.submitList(newList) {
        assertEquals(2, adapter.itemCount)
        assertEquals(newList, adapter.currentList)
      }
    }
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    val inflater = LayoutInflater.from(context)
    val binding = ItemCastBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      MediaCastItem(name = "Test Name") to "Test Name",
      MediaCastItem(originalName = "Original Name") to "Original Name",
    )

    val characterCases = listOf(
      MediaCastItem(character = "Bjorn") to "Bjorn",
      MediaCastItem(character = null) to "TBA",
      MediaCastItem(character = "") to "TBA"
    )

    // test name/originalName
    for ((castItem, expectedText) in testCases) {
      adapter.submitList(listOf(castItem)) {
        adapter.onBindViewHolder(viewHolder, 0)
        assertEquals(expectedText, binding.tvCastName.text.toString())
      }

    }

    // test character fallback
    for ((castItem, expectedCharacter) in characterCases) {
      adapter.submitList(listOf(castItem)) {
        adapter.onBindViewHolder(viewHolder, 0)
        assertEquals(expectedCharacter, binding.tvCastCharacter.text.toString())
      }
    }
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val adapter = CastAdapter(navigator)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemCastBinding.inflate(LayoutInflater.from(context))
    val adapter = CastAdapter(navigator)
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      MediaCastItem(id = 1, name = "name1", profilePath = "valid_image.jpg") to "name1",
      MediaCastItem(id = 2, originalName = "name2", character = "", profilePath = "") to null,
      MediaCastItem(id = 3, name = "name3", character = "Char", profilePath = null) to "name3"
    )

    testCases.forEach { (data, expected) ->
      viewHolder.bind(data)

      // expect the content description has correct name
      assertEquals(expected, binding.imgCastPhoto.contentDescription)
    }
  }

  @Test
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val castItem = MediaCastItem(
      id = 1,
      castId = 123,
      character = "test char",
      gender = 1,
      creditId = "1234",
      knownForDepartment = "Acting",
      originalName = "original name",
      popularity = 12345.0,
      name = "name",
      profilePath = "profile path",
      adult = false,
      order = 123,
    )

    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemCastBinding.inflate(inflater, FrameLayout(inflater.context), false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.submitList(listOf(castItem))
    adapter.onBindViewHolder(viewHolder, 0)

    // use slot to capture MediaCastItem
    val resultSlot = slot<MediaCastItem>()
    binding.container.performClick()

    // wait the UI
    shadowOf(Looper.getMainLooper()).idle()

    // verify navigator.openDetails() is called with the correct argument
    verify { navigator.openPersonDetails(any(), capture(resultSlot)) }

    // expect captured MediaCastItem matches expected values
    val capturedResult = resultSlot.captured
    assertEquals(castItem.id, capturedResult.id)
    assertEquals(castItem.name, capturedResult.name)
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = MediaCastItem(id = 1, name = "Test Name", character = "Bjorn")
    val newItem = MediaCastItem(id = 1, name = "Test Name", character = "Bjorn")

    val diffCallback = CastAdapter.CastDiffCallback()
    assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenOnlyCharacterDifferent_returnsFalse() {
    val oldItem = MediaCastItem(id = 1, name = "Test Name", character = "Bjorn")
    val newItem = MediaCastItem(id = 1, name = "Test Name", character = "Ragnar")

    val diffCallback = CastAdapter.CastDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenOnlyNameDifferent_returnsFalse() {
    val oldItem = MediaCastItem(id = 1, name = "Test Name", character = "Bjorn")
    val newItem = MediaCastItem(id = 1, name = "Different Name", character = "Bjorn")

    val diffCallback = CastAdapter.CastDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenValueDifferent_returnsFalse() {
    val oldItem = MediaCastItem(id = 2, name = "Test Name2", character = "Bjorn")
    val newItem = MediaCastItem(id = 1, name = "Test Name", character = "Ragnar")

    val diffCallback = CastAdapter.CastDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }
}
