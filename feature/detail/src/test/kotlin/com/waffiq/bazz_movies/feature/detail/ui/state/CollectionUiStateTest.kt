package com.waffiq.bazz_movies.feature.detail.ui.state

import com.waffiq.bazz_movies.feature.detail.domain.model.movie.PartsItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionUiStateTest {

  @Test
  fun collectionUiState_withDefaultValue_returnsCorrectly() {
    val state = CollectionUiState()
    assertEquals(false, state.isLoading)
    assertEquals(false, state.isError)
    assertEquals("", state.name)
    assertEquals("", state.overview)
    assertEquals(emptyList<Int>(), state.genreIds)
    assertEquals(null, state.backdropUrl)
    assertEquals(emptyList<PartsItem>(), state.parts)
  }
}
