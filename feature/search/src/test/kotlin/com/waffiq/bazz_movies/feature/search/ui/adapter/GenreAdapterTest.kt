package com.waffiq.bazz_movies.feature.search.ui.adapter

import com.waffiq.bazz_movies.core.common.Genre
import com.waffiq.bazz_movies.feature.search.databinding.ItemGenreListBinding
import com.waffiq.bazz_movies.feature.search.testutils.BaseAdapterTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GenreAdapterTest : BaseAdapterTest() {

  private lateinit var adapter: GenreAdapter
  private lateinit var binding: ItemGenreListBinding

  @Before
  fun setup() {
    super.baseSetup()
    adapter = GenreAdapter(navigator)
    recyclerView.adapter = adapter
    binding = ItemGenreListBinding.inflate(inflater, null, false)
  }

  @Test
  fun submitList_sameItem_onlyDisplayDifferentItem() {
    val sameGenre = Genre.ACTION
    adapter.submitList(listOf(sameGenre, sameGenre, Genre.COMEDY))
  }
}
