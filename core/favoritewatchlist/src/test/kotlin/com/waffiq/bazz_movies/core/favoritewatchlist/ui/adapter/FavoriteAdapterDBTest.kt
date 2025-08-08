package com.waffiq.bazz_movies.core.favoritewatchlist.ui.adapter

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.Favorite
import com.waffiq.bazz_movies.core.domain.MediaItem
import com.waffiq.bazz_movies.navigation.INavigator
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class FavoriteAdapterDBTest {
  private lateinit var context: Context
  private lateinit var navigator: INavigator
  private lateinit var adapter: FavoriteAdapterDB

  private val indonesianMovie = "Indonesian Movie"
  private val indonesianMovie2 = "Indonesian Movie 2"
  private val favorite = Favorite(
    id = 1,
    mediaId = 1,
    mediaType = "movie",
    genre = "Action",
    backDrop = "backdrop",
    poster = "poster",
    overview = "overview",
    title = indonesianMovie,
    releaseDate = "1979-04-04",
    popularity = 214.0,
    rating = 9.0f,
    isFavorite = true,
    isWatchlist = false
  )

  @Before
  fun setup() {
    navigator = mockk(relaxed = true)
    adapter = FavoriteAdapterDB(navigator)
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
  }

  @Test
  fun setFavorite_whenCalledTwice_updatesListAndItemCount() {
    val oldList = listOf(favorite)
    val newList = listOf(
      favorite.copy(id = 2, mediaId = 2, title = indonesianMovie2),
      favorite.copy(id = 3, mediaId = 3, title = "Indonesian Movie 3")
    )

    adapter.setFavorite(oldList)
    assertEquals(1, adapter.itemCount)

    adapter.setFavorite(newList)
    assertEquals(2, adapter.itemCount)
    assertEquals(newList, adapter.getListItemDB())
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectDataForAllCases() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    // test case 1: valid data
    adapter.setFavorite(listOf(favorite))
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(indonesianMovie, binding.tvTitle.text.toString())
    assertEquals("Action", binding.tvGenre.text.toString())
    assertEquals("Apr 04, 1979", binding.tvYearReleased.text.toString())

    // test case 2: released date empty
    adapter.setFavorite(listOf(favorite.copy(releaseDate = "")))
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals("N/A", binding.tvYearReleased.text.toString())
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun viewHolder_whenAccessedBeforeBind_throwsUntilBound() {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(context))
    val viewHolder = adapter.ViewHolder(binding)

    val exception = assertThrows(UninitializedPropertyAccessException::class.java) {
      viewHolder.data // accessing before initialization
    }
    assertTrue(exception.toString().contains("lateinit property data has not been initialized"))

    val spyViewHolder = spyk(viewHolder)
    assertThrows(UninitializedPropertyAccessException::class.java) {
      spyViewHolder.data
    }

    // test accessing data before initialization should throw exception
    var uninitializedExceptionThrown = false
    try {
      viewHolder.data // should throw UninitializedPropertyAccessException
    } catch (_: UninitializedPropertyAccessException) {
      uninitializedExceptionThrown = true
    }

    @Suppress("KotlinConstantConditions") // false warning
    // this will true, because accessing data before init
    assertTrue(
      "Expected UninitializedPropertyAccessException was not thrown",
      uninitializedExceptionThrown
    )

    // after binding, data should be accessible
    viewHolder.bind(favorite)
    assertNotNull(viewHolder.data)
    assertEquals(favorite, viewHolder.data)
    assertEquals("movie", viewHolder.data.mediaType)
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(context))
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      favorite.copy(
        backDrop = "N/A",
        poster = "N/A",
        title = indonesianMovie2
      ) to indonesianMovie2,
      favorite.copy(backDrop = "N/A") to indonesianMovie
    )

    testCases.forEach { (favorite, favoriteTitle) ->
      viewHolder.bind(favorite)
      assertNotNull(viewHolder.data)

      // expect the tag has correct ID
      assertEquals(favoriteTitle, binding.ivPicture.tag)
    }
  }

  @Test
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemResultBinding.inflate(inflater, FrameLayout(inflater.context), false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setFavorite(listOf(favorite))
    adapter.onBindViewHolder(viewHolder, 0)

    val resultSlot = slot<MediaItem>()

    binding.containerResult.performClick()

    // wait hte UI
    shadowOf(Looper.getMainLooper()).idle()

    // verify navigator.openDetails() is called with the correct argument
    verify { navigator.openDetails(any(), capture(resultSlot)) }

    // expect captured MediaItem matches expected values
    val capturedResult = resultSlot.captured
    assertEquals(favorite.mediaId, capturedResult.id)
    assertEquals(favorite.title, capturedResult.title)
    assertEquals(favorite.poster, capturedResult.posterPath)
    assertEquals(favorite.overview, capturedResult.overview)
    assertEquals(favorite.title, capturedResult.originalTitle)
    assertEquals(favorite.mediaType, capturedResult.mediaType)
    assertEquals(favorite.releaseDate, capturedResult.releaseDate)
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = favorite
    val newItem = favorite // same content

    val diffCallback = FavoriteAdapterDB.DiffCallback(listOf(oldItem), listOf(newItem))

    assertTrue(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenContentIsDifferent_returnsFalse() {
    val oldItem = favorite
    val newItem = Favorite(
      id = 4535,
      mediaId = 34215,
      mediaType = "tv",
      genre = "Romance",
      backDrop = "backdrop.jpg",
      poster = "poster.jpg",
      overview = "Lorem ipsum",
      title = "Indonesian Series",
      releaseDate = "2025-01-02",
      popularity = 3124.0,
      rating = 10.0f,
      isFavorite = false,
      isWatchlist = false
    ) // different content

    val diffCallback = FavoriteAdapterDB.DiffCallback(listOf(oldItem), listOf(newItem))

    assertFalse(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenDifferentContent_returnsFalse() {
    val testCases = listOf(
      Pair(favorite, favorite.copy(isFavorite = !favorite.isFavorite)),
      Pair(favorite, favorite.copy(isWatchlist = !favorite.isWatchlist)),
      Pair(
        favorite,
        favorite.copy(mediaType = if (favorite.mediaType == "movie") "tv" else "movie")
      )
    )

    testCases.forEach { (oldItem, newItem) ->
      val diffCallback = FavoriteAdapterDB.DiffCallback(listOf(oldItem), listOf(newItem))
      assertFalse(diffCallback.areContentsTheSame(0, 0))
    }
  }

  @Test
  fun areItemsTheSame_whenSameContent_returnsTrue() {
    val testCases = listOf(
      Pair(favorite, favorite.copy(isFavorite = !favorite.isFavorite)),
      Pair(favorite, favorite.copy(isWatchlist = !favorite.isWatchlist)),
      Pair(
        favorite,
        favorite.copy(mediaType = if (favorite.mediaType == "movie") "tv" else "movie")
      )
    )

    testCases.forEach { (oldItem, newItem) ->
      val diffCallback = FavoriteAdapterDB.DiffCallback(listOf(oldItem), listOf(newItem))
      assertFalse(diffCallback.areItemsTheSame(0, 0))
    }

    val diffCallbackSame = FavoriteAdapterDB.DiffCallback(listOf(favorite), listOf(favorite))
    assertTrue(diffCallbackSame.areItemsTheSame(0, 0))
  }

  @Test
  fun viewHolder_whenUnbound_throwsThenInitializes() {
    val binding = ItemResultBinding.inflate(LayoutInflater.from(context))
    val viewHolder = adapter.ViewHolder(binding)

    // uninitialized access should throw exception
    assertThrows(UninitializedPropertyAccessException::class.java) { viewHolder.data }

    // initialize with bind()
    viewHolder.bind(favorite)

    // explicitly access data again to hit both branches
    assertNotNull(viewHolder.data)
    assertEquals(favorite, viewHolder.data)
    assertEquals("movie", viewHolder.data.mediaType)

    // clickable
    viewHolder.itemView.performClick()
  }
}
