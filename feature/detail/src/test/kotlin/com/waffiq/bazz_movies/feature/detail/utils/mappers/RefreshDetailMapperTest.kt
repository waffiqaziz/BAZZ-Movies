package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.favoriteMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieMediaDetail
import com.waffiq.bazz_movies.feature.detail.utils.mappers.BasicMediaDetailMapper.refreshWith
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshDetailMapperTest {

  @Test
  fun refreshWith_withValidGenreId_returnsCorrectGenreName() {
    val result = favoriteMovie.refreshWith(movieMediaDetail)
    println(movieMediaDetail.genreId)
    assertEquals("Action", result.genre)
  }

  @Test
  fun refreshWith_genreIdNotValid_returnsEmpty() {
    val result = favoriteMovie.refreshWith(movieMediaDetail.copy(genreId = emptyList()))
    assertEquals("", result.genre)

    val result2 = favoriteMovie.refreshWith(movieMediaDetail.copy(genreId = null))
    assertEquals("", result2.genre)
  }

  @Test
  fun refreshWith_tmdbScoreNull_returnsZero() {
    val result = favoriteMovie.refreshWith(movieMediaDetail.copy(tmdbScore = null))
    assertEquals(0.0f, result.rating)
  }
}
