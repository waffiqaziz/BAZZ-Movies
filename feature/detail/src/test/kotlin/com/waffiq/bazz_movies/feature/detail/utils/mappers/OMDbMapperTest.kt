package com.waffiq.bazz_movies.feature.detail.utils.mappers

import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.OMDbDetailsResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.omdb.RatingsItemResponse
import com.waffiq.bazz_movies.feature.detail.domain.model.omdb.OMDbDetails
import com.waffiq.bazz_movies.feature.detail.utils.mappers.OMDbMapper.toOMDbDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OMDbMapperTest {

  @Test
  fun toOMDbDetails_withValidValues_returnsOMDbDetails() {
    val ratingsItemResponse = RatingsItemResponse(
      value = "8.5/10",
      source = "Internet Movie Database"
    )

    val omdbDetailsResponse = OMDbDetailsResponse(
      metascore = "85",
      boxOffice = "$100,000,000",
      website = "https://movie.com",
      imdbRating = "8.5",
      imdbVotes = "500,000",
      ratings = listOf(ratingsItemResponse),
      runtime = "148 min",
      language = "English",
      rated = "PG-13",
      production = "Warner Bros",
      released = "15 Jul 2008",
      imdbID = "tt0468569",
      plot = "When the menace known as the Joker wreaks havoc...",
      director = "Christopher Nolan",
      title = "The Dark Knight",
      actors = "Christian Bale, Heath Ledger, Aaron Eckhart",
      response = "True",
      type = "movie",
      awards = "Won 2 Oscars. Another 146 wins & 142 nominations.",
      dVD = "09 Dec 2008",
      year = "2008",
      poster = "https://poster.jpg",
      country = "United States",
      genre = "Action, Crime, Drama",
      writer = "Jonathan Nolan, Christopher Nolan"
    )

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
    val omdbDetailsResponse = OMDbDetailsResponse(
      metascore = "75",
      boxOffice = "$50,000,000",
      website = "https://movie2.com",
      imdbRating = "7.5",
      imdbVotes = "250,000",
      ratings = null,
      runtime = "120 min",
      language = "English",
      rated = "R",
      production = "Universal Pictures",
      released = "20 Jun 2010",
      imdbID = "tt1234567",
      plot = "A thrilling adventure...",
      director = "Steven Spielberg",
      title = "Example Movie",
      actors = "Actor One, Actor Two",
      response = "True",
      type = "movie",
      awards = "Nominated for 3 Oscars",
      dVD = "15 Dec 2010",
      year = "2010",
      poster = "https://poster2.jpg",
      country = "United States",
      genre = "Adventure, Thriller",
      writer = "Writer One, Writer Two"
    )

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertEquals("75", omdbDetails.metascore)
    assertEquals("$50,000,000", omdbDetails.boxOffice)
    assertEquals("https://movie2.com", omdbDetails.website)
    assertEquals("7.5", omdbDetails.imdbRating)
    assertEquals("250,000", omdbDetails.imdbVotes)
    assertNull(omdbDetails.ratings)
    assertEquals("120 min", omdbDetails.runtime)
    assertEquals("English", omdbDetails.language)
    assertEquals("R", omdbDetails.rated)
    assertEquals("Universal Pictures", omdbDetails.production)
    assertEquals("20 Jun 2010", omdbDetails.released)
    assertEquals("tt1234567", omdbDetails.imdbID)
    assertEquals("A thrilling adventure...", omdbDetails.plot)
    assertEquals("Steven Spielberg", omdbDetails.director)
    assertEquals("Example Movie", omdbDetails.title)
    assertEquals("Actor One, Actor Two", omdbDetails.actors)
    assertEquals("True", omdbDetails.response)
    assertEquals("movie", omdbDetails.type)
    assertEquals("Nominated for 3 Oscars", omdbDetails.awards)
    assertEquals("15 Dec 2010", omdbDetails.dVD)
    assertEquals("2010", omdbDetails.year)
    assertEquals("https://poster2.jpg", omdbDetails.poster)
    assertEquals("United States", omdbDetails.country)
    assertEquals("Adventure, Thriller", omdbDetails.genre)
    assertEquals("Writer One, Writer Two", omdbDetails.writer)
  }

  @Test
  fun toOMDbDetails_withEmptyRatings_returnsOMDbDetails() {
    val omdbDetailsResponse = OMDbDetailsResponse(
      metascore = "60",
      boxOffice = "$25,000,000",
      website = "https://movie3.com",
      imdbRating = "6.0",
      imdbVotes = "100,000",
      ratings = emptyList(),
      runtime = "90 min",
      language = "English",
      rated = "PG",
      production = "Sony Pictures",
      released = "10 Mar 2015",
      imdbID = "tt7654321",
      plot = "A family comedy...",
      director = "Comedy Director",
      title = "Family Fun",
      actors = "Comedy Actor One, Comedy Actor Two",
      response = "True",
      type = "movie",
      awards = "Winner of 1 award",
      dVD = "20 Aug 2015",
      year = "2015",
      poster = "https://poster3.jpg",
      country = "United States",
      genre = "Comedy, Family",
      writer = "Comedy Writer"
    )

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertEquals("60", omdbDetails.metascore)
    assertEquals("$25,000,000", omdbDetails.boxOffice)
    assertEquals("https://movie3.com", omdbDetails.website)
    assertEquals("6.0", omdbDetails.imdbRating)
    assertEquals("100,000", omdbDetails.imdbVotes)
    assertEquals(0, omdbDetails.ratings?.size)
    assertEquals("90 min", omdbDetails.runtime)
    assertEquals("English", omdbDetails.language)
    assertEquals("PG", omdbDetails.rated)
    assertEquals("Sony Pictures", omdbDetails.production)
    assertEquals("10 Mar 2015", omdbDetails.released)
    assertEquals("tt7654321", omdbDetails.imdbID)
    assertEquals("A family comedy...", omdbDetails.plot)
    assertEquals("Comedy Director", omdbDetails.director)
    assertEquals("Family Fun", omdbDetails.title)
    assertEquals("Comedy Actor One, Comedy Actor Two", omdbDetails.actors)
    assertEquals("True", omdbDetails.response)
    assertEquals("movie", omdbDetails.type)
    assertEquals("Winner of 1 award", omdbDetails.awards)
    assertEquals("20 Aug 2015", omdbDetails.dVD)
    assertEquals("2015", omdbDetails.year)
    assertEquals("https://poster3.jpg", omdbDetails.poster)
    assertEquals("United States", omdbDetails.country)
    assertEquals("Comedy, Family", omdbDetails.genre)
    assertEquals("Comedy Writer", omdbDetails.writer)
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

    val omdbDetailsResponse = OMDbDetailsResponse(
      metascore = "85",
      boxOffice = "$150,000,000",
      website = "https://blockbuster.com",
      imdbRating = "8.5",
      imdbVotes = "750,000",
      ratings = listOf(ratingsItem1, ratingsItem2, ratingsItem3),
      runtime = "160 min",
      language = "English",
      rated = "PG-13",
      production = "Marvel Studios",
      released = "04 May 2012",
      imdbID = "tt0848228",
      plot = "Earth's mightiest heroes must come together...",
      director = "Joss Whedon",
      title = "The Avengers",
      actors = "Robert Downey Jr., Chris Evans, Mark Ruffalo",
      response = "True",
      type = "movie",
      awards = "Nominated for 1 Oscar. Another 38 wins & 79 nominations.",
      dVD = "25 Sep 2012",
      year = "2012",
      poster = "https://avengers-poster.jpg",
      country = "United States",
      genre = "Action, Adventure, Sci-Fi",
      writer = "Joss Whedon, Zak Penn"
    )

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
    assertEquals("85", omdbDetails.metascore)
    assertEquals("$150,000,000", omdbDetails.boxOffice)
    assertEquals("https://blockbuster.com", omdbDetails.website)
    assertEquals("8.5", omdbDetails.imdbRating)
    assertEquals("750,000", omdbDetails.imdbVotes)
    assertEquals(3, omdbDetails.ratings?.size)
    assertEquals("8.5/10", omdbDetails.ratings?.get(0)?.value)
    assertEquals("Internet Movie Database", omdbDetails.ratings?.get(0)?.source)
    assertEquals("94%", omdbDetails.ratings?.get(1)?.value)
    assertEquals("Rotten Tomatoes", omdbDetails.ratings?.get(1)?.source)
    assertEquals("85/100", omdbDetails.ratings?.get(2)?.value)
    assertEquals("Metacritic", omdbDetails.ratings?.get(2)?.source)
    assertEquals("160 min", omdbDetails.runtime)
    assertEquals("English", omdbDetails.language)
    assertEquals("PG-13", omdbDetails.rated)
    assertEquals("Marvel Studios", omdbDetails.production)
    assertEquals("04 May 2012", omdbDetails.released)
    assertEquals("tt0848228", omdbDetails.imdbID)
    assertEquals("Earth's mightiest heroes must come together...", omdbDetails.plot)
    assertEquals("Joss Whedon", omdbDetails.director)
    assertEquals("The Avengers", omdbDetails.title)
    assertEquals("Robert Downey Jr., Chris Evans, Mark Ruffalo", omdbDetails.actors)
    assertEquals("True", omdbDetails.response)
    assertEquals("movie", omdbDetails.type)
    assertEquals("Nominated for 1 Oscar. Another 38 wins & 79 nominations.", omdbDetails.awards)
    assertEquals("25 Sep 2012", omdbDetails.dVD)
    assertEquals("2012", omdbDetails.year)
    assertEquals("https://avengers-poster.jpg", omdbDetails.poster)
    assertEquals("United States", omdbDetails.country)
    assertEquals("Action, Adventure, Sci-Fi", omdbDetails.genre)
    assertEquals("Joss Whedon, Zak Penn", omdbDetails.writer)
  }

  @Test
  fun toOMDbDetails_withNullValues_returnsOMDbDetails() {
    val omdbDetailsResponse = OMDbDetailsResponse(
      metascore = null,
      boxOffice = null,
      website = null,
      imdbRating = null,
      imdbVotes = null,
      ratings = null,
      runtime = null,
      language = null,
      rated = null,
      production = null,
      released = null,
      imdbID = null,
      plot = null,
      director = null,
      title = null,
      actors = null,
      response = null,
      type = null,
      awards = null,
      dVD = null,
      year = null,
      poster = null,
      country = null,
      genre = null,
      writer = null
    )

    val omdbDetails: OMDbDetails = omdbDetailsResponse.toOMDbDetails()
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
