package com.waffiq.bazz_movies.feature.detail.utils.mapper

import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.favoriteMovie
import com.waffiq.bazz_movies.feature.detail.testutils.DummyData.movieMediaDetail
import com.waffiq.bazz_movies.feature.detail.utils.mapper.BasicMediaDetailMapper.refreshWith
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshDetailMapperTest {

  @Test
  fun refreshWith_withValidGenreId_returnsCorrectGenreName() {
    val result = favoriteMovie.refreshWith(movieMediaDetail)
    println(movieMediaDetail.genreId)
    assertEquals(28, result.genreIds.first())
  }

  @Test
  fun refreshWith_genreIdNotValid_returnsEmpty() {
    val result = favoriteMovie.refreshWith(movieMediaDetail.copy(genreId = emptyList()))
    assertEquals(emptyList<Int>(), result.genreIds)

    val result2 = favoriteMovie.refreshWith(movieMediaDetail.copy(genreId = null))
    assertEquals(emptyList<Int>(), result2.genreIds)
  }

  @Test
  fun refreshWith_tmdbScoreNull_returnsZero() {
    val result = favoriteMovie.refreshWith(movieMediaDetail.copy(tmdbScore = null))
    assertEquals(0.0f, result.rating)
  }
}
