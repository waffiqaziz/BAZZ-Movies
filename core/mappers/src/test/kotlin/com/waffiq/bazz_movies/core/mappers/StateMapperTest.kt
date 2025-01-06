package com.waffiq.bazz_movies.core.mappers

import com.waffiq.bazz_movies.core.domain.Rated
import com.waffiq.bazz_movies.core.mappers.StateMapper.toStated
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.RatedResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.state.StatedResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class StateMapperTest {

  @Test
  fun `map StatedResponse with valid rated value then Stated should return mapped values`() {
    val statedResponse = StatedResponse(
      id = 123,
      favorite = true,
      ratedResponse = RatedResponse.Value(5.0),
      watchlist = false
    )

    val stated = statedResponse.toStated()

    assertEquals(123, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(5.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with null rated value then Stated should return Unrated`() {
    val statedResponse = StatedResponse(
      id = 456,
      favorite = false,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toStated()

    assertEquals(456, stated.id)
    assertEquals(false, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with rated as a string value then Stated should handle custom type`() {
    val statedResponse = StatedResponse(
      id = 789,
      favorite = true,
      ratedResponse = RatedResponse.Value(10.0),
      watchlist = false
    )

    val stated = statedResponse.toStated()

    assertEquals(789, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(10.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with rated as a complex object then Stated should handle it`() {
    val complexRated = RatedResponse.Value(8.5)
    val statedResponse = StatedResponse(
      id = 321,
      favorite = false,
      ratedResponse = complexRated,
      watchlist = true
    )

    val stated = statedResponse.toStated()

    assertEquals(321, stated.id)
    assertEquals(false, stated.favorite)
    assertEquals(8.5, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with extreme ID values`() {
    val statedResponse = StatedResponse(
      id = Int.MAX_VALUE,
      favorite = true,
      ratedResponse = RatedResponse.Value(10.0),
      watchlist = false
    )

    val stated = statedResponse.toStated()

    assertEquals(Int.MAX_VALUE, stated.id)
    assertEquals(true, stated.favorite)
    assertEquals(10.0, (stated.rated as Rated.Value).value, 0.0)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with zero ID`() {
    val statedResponse = StatedResponse(
      id = 0,
      favorite = false,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toStated()

    assertEquals(0, stated.id)
    assertEquals(false, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with rated as boolean then Stated should handle boolean rating`() {
    val statedResponse = StatedResponse(
      id = 111,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = false
    )

    val stated = statedResponse.toStated()

    assertEquals(111, stated.id)
    assertEquals(true, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(false, stated.watchlist)
  }

  @Test
  fun `map StatedResponse with extreme boolean combinations`() {
    val statedResponse = StatedResponse(
      id = 999,
      favorite = true,
      ratedResponse = RatedResponse.Unrated,
      watchlist = true
    )

    val stated = statedResponse.toStated()

    assertEquals(999, stated.id)
    assertEquals(true, stated.favorite)
    assertTrue(stated.rated is Rated.Unrated)
    assertEquals(true, stated.watchlist)
  }
}

