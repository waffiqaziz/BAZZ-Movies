package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.testutils.HelperTest.omdbDetailsResponse
import com.waffiq.bazz_movies.feature.detail.utils.mappers.OMDbMapper.toOMDbDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OMDbMapperTest {

  private val omdbDetailsResponseNull = OMDbDetailsResponse()

  @Test
  fun toOMDbDetails_withValidValues_returnsOMDbDetails() {
    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()

    assertEquals("85", omdbDetails.metascore)
    assertEquals("$100,000,000", omdbDetails.boxOffice)
    assertEquals("https://movie.com", omdbDetails.website)
    assertEquals("8.5", omdbDetails.imdbRating)
    assertEquals("500,000", omdbDetails.imdbVotes)
    assertEquals(1, omdbDetails.ratings?.size)
    assertEquals("8.5/10", omdbDetails.ratings?.get(0)?.value)
    assertEquals("Internet Movie Database", omdbDetails.ratings?.get(0)?.source)
    assertEquals("148 min", omdbDetails.runtime)
    assertEquals("English", omdbDetails.language)
    assertEquals("PG-13", omdbDetails.rated)
    assertEquals("Warner Bros", omdbDetails.production)
    assertEquals("15 Jul 2008", omdbDetails.released)
    assertEquals("tt0468569", omdbDetails.imdbID)
    assertEquals("When the menace known as the Joker wreaks havoc...", omdbDetails.plot)
    assertEquals("Christopher Nolan", omdbDetails.director)
    assertEquals("The Dark Knight", omdbDetails.title)
    assertEquals("Christian Bale, Heath Ledger, Aaron Eckhart", omdbDetails.actors)
    assertEquals("True", omdbDetails.response)
    assertEquals("movie", omdbDetails.type)
    assertEquals("Won 2 Oscars. Another 146 wins & 142 nominations.", omdbDetails.awards)
    assertEquals("09 Dec 2008", omdbDetails.dVD)
    assertEquals("2008", omdbDetails.year)
    assertEquals("https://poster.jpg", omdbDetails.poster)
    assertEquals("United States", omdbDetails.country)
    assertEquals("Action, Crime, Drama", omdbDetails.genre)
    assertEquals("Jonathan Nolan, Christopher Nolan", omdbDetails.writer)
  }

  @Test
  fun toOMDbDetails_withNullRatings_returnsOMDbDetails() {
    val omdbDetailsResponse = omdbDetailsResponse.copy(ratings = null)

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertNull(omdbDetails.ratings)
  }

  @Test
  fun toOMDbDetails_withEmptyRatings_returnsOMDbDetails() {
    val omdbDetailsResponse = omdbDetailsResponse.copy(ratings = emptyList())

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertEquals(0, omdbDetails.ratings?.size)
  }

  @Test
  fun toOMDbDetails_withMultipleRatings_returnsOMDbDetails() {
    val ratingsItem1 = RatingsItemResponse(
      value = "8.5/10",
      source = "Internet Movie Database"
    )

    val ratingsItem2 = RatingsItemResponse(
      value = "94%",
      source = "Rotten Tomatoes"
    )

    val ratingsItem3 = RatingsItemResponse(
      value = "85/100",
      source = "Metacritic"
    )

    val omdbDetailsResponse = omdbDetailsResponse.copy(
      ratings = listOf(ratingsItem1, ratingsItem2, ratingsItem3),
    )

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertEquals(3, omdbDetails.ratings?.size)
    assertEquals("8.5/10", omdbDetails.ratings?.get(0)?.value)
    assertEquals("Internet Movie Database", omdbDetails.ratings?.get(0)?.source)
    assertEquals("94%", omdbDetails.ratings?.get(1)?.value)
    assertEquals("Rotten Tomatoes", omdbDetails.ratings?.get(1)?.source)
    assertEquals("85/100", omdbDetails.ratings?.get(2)?.value)
    assertEquals("Metacritic", omdbDetails.ratings?.get(2)?.source)
  }

  @Test
  fun toOMDbDetails_withNullValues_returnsOMDbDetails() {
    val omdbDetails: OMDbDetails = omdbDetailsResponseNull.toOMDbDetails()
    assertNull(omdbDetails.metascore)
    assertNull(omdbDetails.boxOffice)
    assertNull(omdbDetails.website)
    assertNull(omdbDetails.imdbRating)
    assertNull(omdbDetails.imdbVotes)
    assertNull(omdbDetails.ratings)
    assertNull(omdbDetails.runtime)
    assertNull(omdbDetails.language)
    assertNull(omdbDetails.rated)
    assertNull(omdbDetails.production)
    assertNull(omdbDetails.released)
    assertNull(omdbDetails.imdbID)
    assertNull(omdbDetails.plot)
    assertNull(omdbDetails.director)
    assertNull(omdbDetails.title)
    assertNull(omdbDetails.actors)
    assertNull(omdbDetails.response)
    assertNull(omdbDetails.type)
    assertNull(omdbDetails.awards)
    assertNull(omdbDetails.dVD)
    assertNull(omdbDetails.year)
    assertNull(omdbDetails.poster)
    assertNull(omdbDetails.country)
    assertNull(omdbDetails.genre)
    assertNull(omdbDetails.writer)
  }
}
