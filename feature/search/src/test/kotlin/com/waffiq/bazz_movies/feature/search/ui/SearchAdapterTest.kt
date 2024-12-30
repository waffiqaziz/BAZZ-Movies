package com.waffiq.bazz_movies.feature.search.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.navigation.INavigator
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchAdapterTest {

  @Mock
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  private lateinit var adapter: SearchAdapter

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)
    adapter = SearchAdapter(navigator)
    recyclerView.adapter = adapter
  }

  @Test
  fun `submitData should update the adapter with PagingData`() = runTest {
    val pagingData = PagingData.from(
      listOf(
        ResultsItemSearch(id = 1, title = "Movie 1"),
        ResultsItemSearch(id = 2, title = "Movie 2")
      )
    )

    adapter.submitData(pagingData)
    assertEquals(2, adapter.itemCount)
  }

  @Test
  fun `onBindViewHolder should bind correct movie data from PagingData`() = runTest {
    val movieData = ResultsItemSearch(
      mediaType = "movie",
      id = 1,
      title = "Transformers",
      originalTitle = "Transformers",
      listGenreIds = listOf(12, 878, 28),
      releaseDate = "2007-06-27"
    )
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(movieData))
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals("Transformers", binding.tvTitle.text.toString())
    assertEquals("Adventure, Science Fiction, Action", binding.tvGenre.text.toString())
    assertEquals("2007-06-27", binding.tvYearReleased.text.toString())
  }

  @Test
  fun `onBindViewHolder should bind correct tv data from PagingData`() = runTest {
    val tvData = ResultsItemSearch(
      mediaType = "tv",
      id = 12345,
      title = "Bleach",
      originalTitle = "BLEACH",
      listGenreIds = listOf(10759, 16, 10765),
      releaseDate = "2004-10-05"
    )
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(tvData))
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals("Bleach", binding.tvTitle.text.toString())
    assertEquals("Action & Adventure, Animation, Sci-Fi & Fantasy", binding.tvGenre.text.toString())
    assertEquals("2004-10-05", binding.tvYearReleased.text.toString())
  }

  @Test
  fun `onBindViewHolder should bind correct person data from PagingData`() = runTest {
    val personData = ResultsItemSearch(
      mediaType = "person",
      id = 134321,
      name = "Jason Statham",
      originalName = "Jason Statham",
      adult = false,
      knownForDepartment = "Acting",
      listKnownFor = listOf(
        KnownForItem(title = "The Meg"),
        KnownForItem(title = "The Transporter"),
        KnownForItem(title = "Wrath of Man")
      )
    )
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(personData))
    adapter.submitData(pagingData)
    adapter.onBindViewHolder(viewHolder, 0)

    assertEquals("Jason Statham", binding.tvTitle.text.toString())
    assertEquals("The Meg, The Transporter, Wrath of Man", binding.tvGenre.text.toString())
    assertEquals("Acting", binding.tvYearReleased.text.toString())
  }

  @Test
  fun `onClick should trigger openDetails in navigator from PagingData`() = runTest {
    val data = ResultsItemSearch(
      id = 1,
      title = "Test Movie",
      name = "Test Name",
      overview = "Test Overview",
      mediaType = "movie"
    )
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }

    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(data))
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.containerResult.performClick()

    val expectedItem = ResultItem(
      id = 1,
      title = "Test Movie",
      overview = "Test Overview",
      name = "Test Name",
      mediaType = "movie"
    )

    verify(navigator).openDetails(eq(context), eq(expectedItem))
  }

  @Test
  fun `submitData with empty PagingData should not crash`() = runTest {
    val emptyPagingData = PagingData.from(emptyList<ResultsItemSearch>())
    adapter.submitData(emptyPagingData)
    assertEquals(0, adapter.itemCount)
  }

  @Test
  fun `submitData with multiple PagingData items should update item count correctly`() =
    runTest {
      val pagingData = PagingData.from(
        listOf(
          ResultsItemSearch(id = 1, title = "Movie 1"),
          ResultsItemSearch(id = 2, title = "Movie 2"),
          ResultsItemSearch(id = 3, title = "Movie 3")
        )
      )

      adapter.submitData(pagingData)
      assertEquals(3, adapter.itemCount)
    }
}
