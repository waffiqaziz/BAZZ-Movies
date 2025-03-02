package com.waffiq.bazz_movies.feature.search.ui

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemResultBinding
import com.waffiq.bazz_movies.core.domain.MovieTvCastItem
import com.waffiq.bazz_movies.core.domain.ResultItem
import com.waffiq.bazz_movies.core.test.MainDispatcherRule
import com.waffiq.bazz_movies.feature.search.domain.model.KnownForItem
import com.waffiq.bazz_movies.feature.search.domain.model.ResultsItemSearch
import com.waffiq.bazz_movies.navigation.INavigator
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
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
  lateinit var context: Context

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
    context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }
  }

  @Test
  fun submitData_withPagingData_updateAdapter() = runTest {
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
  fun onBindViewHolder_bindCorrectMovieData() = runTest {
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
  fun onBindViewHolder_bindCorrectTvData() = runTest {
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
  fun onBindViewHolder_bindCorrectPersonData() = runTest {
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
  fun onBindViewHolder_withNullData_doesNotCrash() = runTest {
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)
    val fakePagingData = PagingData.empty<ResultsItemSearch>()


    adapter.submitData(fakePagingData)
    advanceUntilIdle()

    assertEquals(0, adapter.itemCount)

    if (adapter.itemCount > 0) adapter.onBindViewHolder(viewHolder, 0)
  }

  @Test
  fun onBindViewHolder_withValidData_bindsCorrectly() = runTest {
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val dummyData =
      listOf(ResultsItemSearch(id = 1, title = "Title", mediaType = "movie"))
    val pagingData = PagingData.from(dummyData)

    adapter.submitData(pagingData)
    advanceUntilIdle()

    assertEquals(1, adapter.itemCount)
    adapter.onBindViewHolder(viewHolder, 0)
  }

  @Test
  fun onCreateViewHolder_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val adapter = SearchAdapter(navigator)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun onClick_openDetailsMovie() = runTest {
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
  fun onClick_openPersonDetails() = runTest {
    val personData = ResultsItemSearch(
      id = 1,
      mediaType = "person",
      name = "Actor Name",
      originalName = "Original Actor Name",
      profilePath = "/profile/path.jpg"
    )

    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies)
    }

    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    val pagingData = PagingData.from(listOf(personData))
    adapter.submitData(pagingData)
    advanceUntilIdle()

    adapter.onBindViewHolder(viewHolder, 0)
    binding.containerResult.performClick()

    val expectedPerson = MovieTvCastItem(
      id = 1,
      name = "Actor Name",
      originalName = "Original Actor Name",
      profilePath = "/profile/path.jpg"
    )

    verify(navigator).openPersonDetails(eq(context), eq(expectedPerson))
  }

  @Test
  fun submitData_wthEmptyPagingData_shouldNotCrash() = runTest {
    val emptyPagingData = PagingData.from(emptyList<ResultsItemSearch>())
    adapter.submitData(emptyPagingData)
    assertEquals(0, adapter.itemCount)
  }

  @Test
  fun submitData_withMultiplePagingDataItems_shouldUpdateItemCountCorrectly() = runTest {
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

  @Test
  fun showDataPerson_handleAllPossibility() {
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, null, false)

    // test case 1: null name
    val personData1 = ResultsItemSearch(
      id = 1,
      mediaType = "person",
      name = null,
      originalName = "Original Actor Name",
      profilePath = "/profile/path.jpg"
    )
    adapter.showDataPerson(binding, personData1)

    assertEquals("Original Actor Name", binding.ivPicture.contentDescription)
    assertEquals("Original Actor Name", binding.tvTitle.text.toString())

    // test case 2: null profile path
    val personData2 = ResultsItemSearch(
      id = 1,
      mediaType = "person",
      name = "Actor Name",
      profilePath = ""
    )
    adapter.showDataPerson(binding, personData2)
  }

  @Test
  fun showDataMovieTv_handleNullData() {
    val parent = FrameLayout(context)
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, parent, false)

    // release data all null, title using original name
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(
        id = 1, mediaType = "movie", originalName = "Avatar",
        listGenreIds = emptyList()
      ),
      binding.root
    )
    assertEquals("Avatar", binding.tvTitle.text.toString())
    assertEquals("N/A", binding.tvYearReleased.text.toString())
    assertEquals("N/A", binding.tvGenre.text.toString())

    // release data all null, title using original title
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", originalTitle = "Avatar Name"),
      binding.root
    )
    assertEquals("Avatar Name", binding.tvTitle.text.toString())
  }

  @Test
  fun showDataMovieTv_handleYearReleased_allPossibility() {
    val parent = FrameLayout(context)
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, parent, false)

    // releaseDate null, firstAirDate null
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie"),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate null, firstAirDate valid
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", firstAirDate = "2023-05-15"),
      binding.root
    )
    assertEquals("2023-05-15", binding.tvYearReleased.text.toString())

    // releaseDate null, firstAirDate empty
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", firstAirDate = ""),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate null, firstAirDate blank
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", firstAirDate = " "),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate valid, firstAirDate valid
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(
        id = 1,
        mediaType = "movie",
        firstAirDate = "2023-05-15",
        releaseDate = "2023-05-15"
      ),
      binding.root
    )
    assertEquals("2023-05-15", binding.tvYearReleased.text.toString())

    // releaseDate valid, firstAirDate null
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "2023-05-15"),
      binding.root
    )
    assertEquals("2023-05-15", binding.tvYearReleased.text.toString())

    // releaseDate valid, firstAirDate empty
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "2010-10-10", firstAirDate = ""),
      binding.root
    )
    assertEquals("2010-10-10", binding.tvYearReleased.text.toString())

    // releaseDate valid, firstAirDate blank
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(
        id = 1,
        mediaType = "movie",
        releaseDate = "2022-08-20",
        firstAirDate = " "
      ),
      binding.root
    )
    assertEquals("2022-08-20", binding.tvYearReleased.text.toString())

    // releaseDate empty, firstAirDate null
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = ""),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate empty, firstAirDate valid
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "", firstAirDate = "2010-10-10"),
      binding.root
    )
    assertEquals("2010-10-10", binding.tvYearReleased.text.toString())

    // releaseDate empty, firstAirDate empty
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "", firstAirDate = ""),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate empty, firstAirDate blank
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "", firstAirDate = " "),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate blank, firstAirDate null
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = " "),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate blank, firstAirDate blank
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = "  ", firstAirDate = "  "),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate blank, firstAirDate empty
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", releaseDate = " ", firstAirDate = ""),
      binding.root
    )
    assertEquals("N/A", binding.tvYearReleased.text.toString())

    // releaseDate blank, firstAirDate valid
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(
        id = 1,
        mediaType = "movie",
        releaseDate = " ",
        firstAirDate = "2022-08-20"
      ),
      binding.root
    )
    assertEquals("2022-08-20", binding.tvYearReleased.text.toString())
  }

  @Test
  fun showDataMovieTv_handleValidReleaseDate() {
    val movie = ResultsItemSearch(
      id = 1,
      mediaType = "movie",
      originalTitle = "Valid Release Date",
      releaseDate = "2023-05-15",
      firstAirDate = "2010-10-10",
    )

    val parent = FrameLayout(context)
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, parent, false)

    adapter.showDataMovieTv(binding, movie, binding.root)

    assertEquals("Valid Release Date", binding.tvTitle.text.toString())
    assertEquals("2023-05-15", binding.tvYearReleased.text.toString())
  }

  @Test
  fun showDataMovieTv_handleSetImageMovieTv_handleAllPossibility() {
    val parent = FrameLayout(context)
    val inflater = LayoutInflater.from(context)
    val binding = ItemResultBinding.inflate(inflater, parent, false)

    adapter.showDataMovieTv(
      binding, ResultsItemSearch(id = 1, mediaType = "movie"),
      binding.root
    )
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", backdropPath = ""),
      binding.root
    )
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", backdropPath = " "),
      binding.root
    )
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", posterPath = ""),
      binding.root
    )
    adapter.showDataMovieTv(
      binding,
      ResultsItemSearch(id = 1, mediaType = "movie", posterPath = " "),
      binding.root
    )
    assertTrue(binding.ivPicture.isVisible)
  }

  @Test
  fun diffCallback_areItemsTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = ResultsItemSearch(id = 1, mediaType = "movie")
    val newItem = ResultsItemSearch(id = 1, mediaType = "movie")

    assertTrue(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_areItemsTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = ResultsItemSearch(id = 1, mediaType = "movie")
    val newItem1 = ResultsItemSearch(id = 2, mediaType = "movie") // different ID
    val newItem2 = ResultsItemSearch(id = 1, mediaType = "tv")   // different mediaType

    assertFalse(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem1))
    assertFalse(SearchAdapter.DIFF_CALLBACK.areItemsTheSame(oldItem, newItem2))
  }

  @Test
  fun diffCallback_areContentsTheSame_returnsTrueForSameIdAndMediaType() {
    val oldItem = ResultsItemSearch(id = 1, mediaType = "movie", title = "Movie 1")
    val newItem = ResultsItemSearch(id = 1, mediaType = "movie", title = "Different Title")

    assertTrue(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun diffCallback_areContentsTheSame_returnsFalseForDifferentIdOrMediaType() {
    val oldItem = ResultsItemSearch(id = 1, mediaType = "movie")
    val newItem1 = ResultsItemSearch(id = 2, mediaType = "movie") // different ID
    val newItem2 = ResultsItemSearch(id = 1, mediaType = "tv")   // different mediaType

    assertFalse(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem1))
    assertFalse(SearchAdapter.DIFF_CALLBACK.areContentsTheSame(oldItem, newItem2))
  }
}
