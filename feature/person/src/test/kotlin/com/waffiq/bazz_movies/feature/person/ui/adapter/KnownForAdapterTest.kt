package com.waffiq.bazz_movies.feature.person.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies
import com.waffiq.bazz_movies.core.designsystem.databinding.ItemPlayForBinding
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.navigation.INavigator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KnownForAdapterTest {

  @Mock
  lateinit var navigator: INavigator

  @Mock
  lateinit var recyclerView: RecyclerView

  private lateinit var adapter: KnownForAdapter

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)
    adapter = KnownForAdapter(navigator)
    recyclerView.adapter = adapter
  }

  @Test
  fun `setCast should update list and notify changes`() {
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
  fun `onBindViewHolder should bind correct data`() {
    val castItem = CastItem(id = 1, name = "Test Name", character = "Bjorn")
    val context = ApplicationProvider.getApplicationContext<Context>().apply {
      setTheme(Base_Theme_BAZZ_movies) // set the theme
    }

    // Inflate a real ItemPlayForBinding
    val inflater = LayoutInflater.from(context)
    val binding = ItemPlayForBinding.inflate(inflater, null, false)

    // Create the ViewHolder with the real binding
    val viewHolder = adapter.ViewHolder(binding)

    // Set the adapter data and bind the view
    adapter.setCast(listOf(castItem))
    adapter.onBindViewHolder(viewHolder, 0)

    // Assert that the correct data is bound
    assertEquals("Test Name", binding.tvCastName.text.toString())
    assertEquals("Bjorn", binding.tvCastCharacter.text.toString())
  }
}