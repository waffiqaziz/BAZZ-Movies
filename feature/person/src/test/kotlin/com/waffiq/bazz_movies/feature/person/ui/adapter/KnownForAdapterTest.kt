package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.string.not_available
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPlayForBinding
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class KnownForAdapterTest {
  lateinit var context: Context
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  private lateinit var adapter: KnownForAdapter

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)
    navigator = mockk(relaxed = true)
    adapter = KnownForAdapter(navigator)
    recyclerView.adapter = adapter
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
  }

  @Test
  fun setCast_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(CastItem(id = 1, name = "Old Cast"))
    val newList = listOf(
      CastItem(id = 2, name = "New Cast"),
      CastItem(id = 3, name = "No Cast")
    )

    adapter.setCast(oldList)
    assertEquals(1, adapter.itemCount) // Assert old list size

    adapter.setCast(newList)
    assertEquals(2, adapter.itemCount) // Assert new list size
    assertEquals(newList, adapter.getListCast())
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inflater = LayoutInflater.from(context)
    val binding = ItemPlayForBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val notAvailable = context.getString(not_available)

    val testCases = listOf(
      CastItem(name = "Test Name") to "Test Name",
      CastItem(title = "Test Title") to "Test Title",
      CastItem(originalName = "Original Name") to "Original Name",
      CastItem(originalTitle = "Original Title") to "Original Title"
    )

    val characterCases = listOf(
      CastItem(character = "Bjorn") to "Bjorn",
      CastItem(character = null) to notAvailable
    )

    // test name/title/originalName/originalTitle branches
    for ((castItem, expectedText) in testCases) {
      adapter.setCast(listOf(castItem))
      adapter.onBindViewHolder(viewHolder, 0)

      assertEquals(expectedText, binding.tvCastName.text.toString())
    }

    // test character fallback
    for ((castItem, expectedCharacter) in characterCases) {
      adapter.setCast(listOf(castItem))
      adapter.onBindViewHolder(viewHolder, 0)

      assertEquals(expectedCharacter, binding.tvCastCharacter.text.toString())
    }
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val adapter = KnownForAdapter(navigator)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemPlayForBinding.inflate(LayoutInflater.from(context))
    val adapter = KnownForAdapter(navigator)
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      CastItem(id = 1, character = "Bjorn", posterPath = "valid_image.jpg") to 1,
      CastItem(id = 2, character = "Bjorn", posterPath = "") to 2,
      CastItem(id = 3, character = "Bjorn", posterPath = null) to 3
    )

    testCases.forEach { (castItem, castId) ->
      viewHolder.bind(castItem)

      // expect the tag has correct ID
      assertEquals(castId, binding.imgCastPhoto.tag)
    }
  }

  @Test
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val castItem = CastItem(
      id = 1,
      title = "Movie Title",
      overview = "Overview",
      name = "Test Name",
      originalTitle = "Original Title",
      mediaType = "movie",
      releaseDate = "2024-01-01",
      listGenreIds = listOf(12, 16),
      popularity = 8.5,
      voteAverage = 7.0f,
      voteCount = 100,
      posterPath = "/test.jpg",
      backdropPath = "/backdrop.jpg"
    )

    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemPlayForBinding.inflate(inflater, FrameLayout(inflater.context), false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setCast(listOf(castItem))
    adapter.onBindViewHolder(viewHolder, 0)

    // use slot to capture MediaItem
    val resultSlot = slot<MediaItem>()

    binding.container.performClick()

    // wait hte UI
    shadowOf(Looper.getMainLooper()).idle()

    // verify navigator.openDetails() is called with the correct argument
    verify { navigator.openDetails(any(), capture(resultSlot)) }

    // expect captured MediaItem matches expected values
    val capturedResult = resultSlot.captured
    assertEquals(castItem.id, capturedResult.id)
    assertEquals(castItem.title, capturedResult.title)
    assertEquals(castItem.name, capturedResult.name)
    assertEquals(castItem.originalTitle, capturedResult.originalTitle)
    assertEquals(castItem.releaseDate, capturedResult.releaseDate)
  }

  @Test
  fun areContentsTheSame_whenFilePathIsSame_returnsTrue() {
    val oldItem = CastItem(id = 1, name = "Test Name", character = "Bjorn")
    val newItem = CastItem(id = 1, name = "Test Name", character = "Bjorn") // same content

    val diffCallback = KnownForAdapter(navigator).DiffCallback(listOf(oldItem), listOf(newItem))

    assertTrue(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenDifferentFilePath_returnsFalse() {
    val oldItem = CastItem(id = 1, name = "Test Name", character = "Bjorn")
    val newItem = CastItem(id = 2, name = "Test Name2", character = "Ragnar") // different content

    val diffCallback = KnownForAdapter(navigator).DiffCallback(listOf(oldItem), listOf(newItem))

    assertFalse(diffCallback.areContentsTheSame(0, 0))
  }
}
