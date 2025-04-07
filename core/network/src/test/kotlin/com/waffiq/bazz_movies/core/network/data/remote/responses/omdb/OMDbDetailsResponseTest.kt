package com.waffiq.bazz_movies.core.network.data.remote.responses.omdb

import com.waffiq.bazz_movies.core.network.testutils.DataDumpManager.omdbDetailsResponseDump
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class OMDbDetailsResponseTest {

  @Test
  fun oMDbDetailsResponse_withValidValues_setsPropertiesCorrectly() {
    val oMDbDetailsResponse = omdbDetailsResponseDump
    assertEquals("67", oMDbDetailsResponse.metascore)
    assertEquals("$684,075,767", oMDbDetailsResponse.boxOffice)
    assertEquals("N/A", oMDbDetailsResponse.website)
    assertEquals("7.5", oMDbDetailsResponse.imdbRating)
    assertEquals("519,728", oMDbDetailsResponse.imdbVotes)
    assertEquals("7.5/10", oMDbDetailsResponse.ratings?.get(0)?.value)
    assertEquals("Internet Movie Database", oMDbDetailsResponse.ratings?.get(0)?.source)
    assertEquals("76%", oMDbDetailsResponse.ratings?.get(1)?.value)
    assertEquals("Rotten Tomatoes", oMDbDetailsResponse.ratings?.get(1)?.source)
    assertEquals("67/100", oMDbDetailsResponse.ratings?.get(2)?.value)
    assertEquals("Metacritic", oMDbDetailsResponse.ratings?.get(2)?.source)
    assertEquals("192 min", oMDbDetailsResponse.runtime)
    assertEquals("English", oMDbDetailsResponse.language)
    assertEquals("PG-13", oMDbDetailsResponse.rated)
    assertEquals("N/A", oMDbDetailsResponse.production)
    assertEquals("16 Dec 2022", oMDbDetailsResponse.released)
    assertEquals("tt1630029", oMDbDetailsResponse.imdbID)
    assertEquals(
      """
        Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a 
        familiar threat returns to finish what was previously started, Jake must work with Neytiri 
        and the army of the Na'vi race to protect their home.
      """.trimIndent(),
      oMDbDetailsResponse.plot
    )
    assertEquals("James Cameron", oMDbDetailsResponse.director)
    assertEquals("Avatar: The Way of Water", oMDbDetailsResponse.title)
    assertEquals("Sam Worthington, Zoe Saldana, Sigourney Weaver", oMDbDetailsResponse.actors)
    assertEquals("True", oMDbDetailsResponse.response)
    assertEquals("movie", oMDbDetailsResponse.type)
    assertEquals("Won 1 Oscar. 75 wins & 152 nominations total", oMDbDetailsResponse.awards)
    assertEquals("N/A", oMDbDetailsResponse.dVD)
    assertEquals("2022", oMDbDetailsResponse.year)
    assertEquals(
      "https://m.media-amazon.com/images/M/poster@._V1_SX300.jpg",
      oMDbDetailsResponse.poster
    )
    assertEquals("United States", oMDbDetailsResponse.country)
    assertEquals("Action, Adventure, Fantasy", oMDbDetailsResponse.genre)
    assertEquals("James Cameron, Rick Jaffa, Amanda Silver", oMDbDetailsResponse.writer)
  }

  @Test
  fun oMDbDetailsResponse_withDefaultValues_setsPropertiesCorrectly() {
    val oMDbDetailsResponse = OMDbDetailsResponse()
    assertNull(oMDbDetailsResponse.country)
    assertNull(oMDbDetailsResponse.metascore)
    assertNull(oMDbDetailsResponse.boxOffice)
    assertNull(oMDbDetailsResponse.website)
    assertNull(oMDbDetailsResponse.imdbRating)
    assertNull(oMDbDetailsResponse.imdbVotes)
    assertNull(oMDbDetailsResponse.ratings)
    assertNull(oMDbDetailsResponse.runtime)
    assertNull(oMDbDetailsResponse.language)
    assertNull(oMDbDetailsResponse.rated)
    assertNull(oMDbDetailsResponse.production)
    assertNull(oMDbDetailsResponse.released)
    assertNull(oMDbDetailsResponse.imdbID)
    assertNull(oMDbDetailsResponse.plot)
    assertNull(oMDbDetailsResponse.director)
    assertNull(oMDbDetailsResponse.title)
    assertNull(oMDbDetailsResponse.actors)
    assertNull(oMDbDetailsResponse.response)
    assertNull(oMDbDetailsResponse.type)
    assertNull(oMDbDetailsResponse.awards)
    assertNull(oMDbDetailsResponse.dVD)
    assertNull(oMDbDetailsResponse.year)
    assertNull(oMDbDetailsResponse.poster)
    assertNull(oMDbDetailsResponse.country)
    assertNull(oMDbDetailsResponse.genre)
  }

  @Test
  fun ratingsItemResponse_withSomeNullValues_setsPropertiesCorrectly() {
    val ratingsItemResponse = RatingsItemResponse()
    assertNull(ratingsItemResponse.value)
    assertNull(ratingsItemResponse.source)
  }
}
