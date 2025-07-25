package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.mappers.MediaStateMapper.toMediaState
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.MediaStateResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class MediaStateMapperTest {

  @Test
  fun toStated_withValidValue_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = 123,
      favorite = true,
      ratedResponse = RatedResponse.Value(5.0),
      watchlist = false
    )

    val stated = statedResponse.toMediaState()

    assertEquals(123, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(5.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun toStated_withNullRatedValue_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = 456,
      favorite = false,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toMediaState()

    assertEquals(456, stated.id)
    assertEquals(false, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun toStated_withValidRated_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = 789,
      favorite = true,
      ratedResponse = RatedResponse.Value(10.0),
      watchlist = false
    )

    val stated = statedResponse.toMediaState()

    assertEquals(789, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(10.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun toMediaState_whenRatedIsValueObject_returnsMediaState() {
    val complexRated = RatedResponse.Value(8.5)
    val statedResponse = MediaStateResponse(
      id = 321,
      favorite = false,
      ratedResponse = complexRated,
      watchlist = true
    )

    val stated = statedResponse.toMediaState()

    assertEquals(321, stated.id)
    assertEquals(false, stated.favorite)
    assertEquals(8.5, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun toStated_withExtremeIdValues_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = Int.MAX_VALUE,
      favorite = true,
      ratedResponse = RatedResponse.Value(10.0),
      watchlist = false
    )

    val stated = statedResponse.toMediaState()

    assertEquals(Int.MAX_VALUE, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(10.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun toStated_withZeroId_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = 0,
      favorite = false,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toMediaState()

    assertEquals(0, stated.id)
    assertEquals(false, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun toStated_withRatedUnrated_returnsStated() {
    val statedResponse = MediaStateResponse(
      id = 111,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = false
    )

    val stated = statedResponse.toMediaState()

    assertEquals(111, stated.id)
    assertEquals(true, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun toMediaState_withAllTrue_returnsMediaState() {
    val statedResponse = MediaStateResponse(
      id = 999,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toMediaState()

    assertEquals(999, stated.id)
    assertEquals(true, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }
}
