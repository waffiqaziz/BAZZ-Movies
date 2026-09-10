package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import com.waffiq.bazz_movies.core.network.testutils.DummyData.omdbDetailsResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OMDbDetailsResponseTest {

  @Test
  fun oMDbDetailsResponse_withValidValues_setsRatingFieldsCorrectly() {
    assertEquals("67", omdbDetailsResponse.metascore)
    assertEquals("7.5", omdbDetailsResponse.imdbRating)

    assertEquals("Internet Movie Database", omdbDetailsResponse.ratings?.get(0)?.source)
    assertEquals("7.5/10", omdbDetailsResponse.ratings?.get(0)?.value)

    assertEquals("Rotten Tomatoes", omdbDetailsResponse.ratings?.get(1)?.source)
    assertEquals("76%", omdbDetailsResponse.ratings?.get(1)?.value)

    assertEquals("Metacritic", omdbDetailsResponse.ratings?.get(2)?.source)
    assertEquals("67/100", omdbDetailsResponse.ratings?.get(2)?.value)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsBasicDetailsCorrectly() {
    assertEquals("Avatar: The Way of Water", omdbDetailsResponse.title)
    assertEquals("Action, Adventure, Fantasy", omdbDetailsResponse.genre)
    assertEquals("James Cameron, Rick Jaffa, Amanda Silver", omdbDetailsResponse.writer)
    assertEquals("James Cameron", omdbDetailsResponse.director)
    assertEquals("Sam Worthington, Zoe Saldana, Sigourney Weaver", omdbDetailsResponse.actors)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsPlotCorrectly() {
    assertEquals(
      """
        Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a 
        familiar threat returns to finish what was previously started, Jake must work with Neytiri 
        and the army of the Na'vi race to protect their home.
      """.trimIndent(),
      omdbDetailsResponse.plot,
    )
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsBoxOfficeInfoCorrectly() {
    assertEquals("$684,075,767", omdbDetailsResponse.boxOffice)
    assertEquals("Won 1 Oscar. 75 wins & 152 nominations total", omdbDetailsResponse.awards)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsTechnicalDetailsCorrectly() {
    assertEquals("192 min", omdbDetailsResponse.runtime)
    assertEquals("English", omdbDetailsResponse.language)
    assertEquals("PG-13", omdbDetailsResponse.rated)
    assertEquals("16 Dec 2022", omdbDetailsResponse.released)
    assertEquals("United States", omdbDetailsResponse.country)
    assertEquals("tt1630029", omdbDetailsResponse.imdbID)
    assertEquals("519,728", omdbDetailsResponse.imdbVotes)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsPropertiesCorrectly() {
    assertEquals("N/A", omdbDetailsResponse.website)
    assertEquals("N/A", omdbDetailsResponse.production)
    assertEquals("True", omdbDetailsResponse.response)
    assertEquals("movie", omdbDetailsResponse.type)
    assertEquals("N/A", omdbDetailsResponse.dVD)
    assertEquals("2022", omdbDetailsResponse.year)
    assertEquals(
      "https://m.media-amazon.com/images/M/poster@._V1_SX300.jpg",
      omdbDetailsResponse.poster,
    )
  }

  @Test
  fun oMDbDetailsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val oMDbDetailsResponseNull = OMDbDetailsResponse()
    assertNull(oMDbDetailsResponseNull.country)
    assertNull(oMDbDetailsResponseNull.metascore)
    assertNull(oMDbDetailsResponseNull.boxOffice)
    assertNull(oMDbDetailsResponseNull.website)
    assertNull(oMDbDetailsResponseNull.imdbRating)
    assertNull(oMDbDetailsResponseNull.imdbVotes)
    assertNull(oMDbDetailsResponseNull.ratings)
    assertNull(oMDbDetailsResponseNull.runtime)
    assertNull(oMDbDetailsResponseNull.language)
    assertNull(oMDbDetailsResponseNull.rated)
    assertNull(oMDbDetailsResponseNull.production)
    assertNull(oMDbDetailsResponseNull.released)
    assertNull(oMDbDetailsResponseNull.imdbID)
    assertNull(oMDbDetailsResponseNull.plot)
    assertNull(oMDbDetailsResponseNull.director)
    assertNull(oMDbDetailsResponseNull.title)
    assertNull(oMDbDetailsResponseNull.actors)
    assertNull(oMDbDetailsResponseNull.response)
    assertNull(oMDbDetailsResponseNull.type)
    assertNull(oMDbDetailsResponseNull.awards)
    assertNull(oMDbDetailsResponseNull.dVD)
    assertNull(oMDbDetailsResponseNull.year)
    assertNull(oMDbDetailsResponseNull.poster)
    assertNull(oMDbDetailsResponseNull.country)
    assertNull(oMDbDetailsResponseNull.genre)
  }
}
