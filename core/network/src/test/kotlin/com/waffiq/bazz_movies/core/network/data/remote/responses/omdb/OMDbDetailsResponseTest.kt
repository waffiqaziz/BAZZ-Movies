package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.omdbDetailsResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class OMDbDetailsResponseTest {

  @Test
  fun oMDbDetailsResponse_withValidValues_setsRatingFieldsCorrectly() {
    assertEquals("67", omdbDetailsResponseDump.metascore)
    assertEquals("7.5", omdbDetailsResponseDump.imdbRating)

    assertEquals("Internet Movie Database", omdbDetailsResponseDump.ratings?.get(0)?.source)
    assertEquals("7.5/10", omdbDetailsResponseDump.ratings?.get(0)?.value)

    assertEquals("Rotten Tomatoes", omdbDetailsResponseDump.ratings?.get(1)?.source)
    assertEquals("76%", omdbDetailsResponseDump.ratings?.get(1)?.value)

    assertEquals("Metacritic", omdbDetailsResponseDump.ratings?.get(2)?.source)
    assertEquals("67/100", omdbDetailsResponseDump.ratings?.get(2)?.value)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsBasicDetailsCorrectly() {
    assertEquals("Avatar: The Way of Water", omdbDetailsResponseDump.title)
    assertEquals("Action, Adventure, Fantasy", omdbDetailsResponseDump.genre)
    assertEquals("James Cameron, Rick Jaffa, Amanda Silver", omdbDetailsResponseDump.writer)
    assertEquals("James Cameron", omdbDetailsResponseDump.director)
    assertEquals("Sam Worthington, Zoe Saldana, Sigourney Weaver", omdbDetailsResponseDump.actors)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsPlotCorrectly() {
    assertEquals(
      """
        Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a 
        familiar threat returns to finish what was previously started, Jake must work with Neytiri 
        and the army of the Na'vi race to protect their home.
      """.trimIndent(),
      omdbDetailsResponseDump.plot
    )
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsBoxOfficeInfoCorrectly() {
    assertEquals("$684,075,767", omdbDetailsResponseDump.boxOffice)
    assertEquals("Won 1 Oscar. 75 wins & 152 nominations total", omdbDetailsResponseDump.awards)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsTechnicalDetailsCorrectly() {
    assertEquals("192 min", omdbDetailsResponseDump.runtime)
    assertEquals("English", omdbDetailsResponseDump.language)
    assertEquals("PG-13", omdbDetailsResponseDump.rated)
    assertEquals("16 Dec 2022", omdbDetailsResponseDump.released)
    assertEquals("United States", omdbDetailsResponseDump.country)
    assertEquals("tt1630029", omdbDetailsResponseDump.imdbID)
    assertEquals("519,728", omdbDetailsResponseDump.imdbVotes)
  }

  @Test
  fun oMDbDetailsResponse_withValidValues_setsPropertiesCorrectly() {
    assertEquals("N/A", omdbDetailsResponseDump.website)
    assertEquals("N/A", omdbDetailsResponseDump.production)
    assertEquals("True", omdbDetailsResponseDump.response)
    assertEquals("movie", omdbDetailsResponseDump.type)
    assertEquals("N/A", omdbDetailsResponseDump.dVD)
    assertEquals("2022", omdbDetailsResponseDump.year)
    assertEquals(
      "https://m.media-amazon.com/images/M/poster@._V1_SX300.jpg",
      omdbDetailsResponseDump.poster
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

  @Test
  fun ratingsItemResponse_withSomeNullValues_setsNullValueCorrectly() {
    val ratingsItemResponse = RatingsItemResponse()
    assertNull(ratingsItemResponse.value)
    assertNull(ratingsItemResponse.source)
  }
}
