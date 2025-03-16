package com.waffiq.bazz_movies.core.utils

import com.waffiq.bazz_movies.core.domain.GenresItem
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreHelperTest {

  @Test
  fun transformListGenreIdsToJoinName_withValidGenreNames_returnCorrectString() {
    val input = listOf(28, 12, 16)
    val expectedOutput = "Action, Adventure, Animation"
    val actualOutput = GenreHelper.transformListGenreIdsToJoinName(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreIdsToJoinName_withEmptyList_returnEmptyString() {
    val input = emptyList<Int>()
    val expectedOutput = ""
    val actualOutput = GenreHelper.transformListGenreIdsToJoinName(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreIdsToJoinName_withValidGenreItems_handleInvalidGenreId() {
    val input = listOf(999) // Non-existent genre ID
    val expectedOutput = ""
    val actualOutput = GenreHelper.transformListGenreIdsToJoinName(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreCode_withNullList_returnCorrectCode() {
    val input = listOf("Action", "Comedy", "Romance")
    val expectedOutput = "28|35|10749"
    val actualOutput = GenreHelper.transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreCode_withValidGenreItems_returnEmptyList() {
    val input = emptyList<String>()
    val expectedOutput = ""
    val actualOutput = GenreHelper.transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreCode_with_handleInvalidGenreName() {
    val input = listOf("NonExistentGenre")
    val expectedOutput = ""
    val actualOutput = GenreHelper.transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withNullList_returnCorrectString() {
    val input = listOf(
      GenresItem("Action", 28),
      GenresItem("Mystery", 9648)
    )
    val expectedOutput = "Action, Mystery"
    val actualOutput = GenreHelper.transformListGenreToJoinString(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withNullList_returnNull() {
    val input: List<GenresItem>? = null
    val expectedOutput: String? = null
    val actualOutput = GenreHelper.transformListGenreToJoinString(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreIDs_withValidGenreList_returnCorrectIds() {
    val input = listOf(
      GenresItem("News", 10763),
      GenresItem("Talk", 10767),
      GenresItem("Invalid", null)
    )
    val expectedOutput = listOf(10763, 10767, 0) // IDs, null mapped to 0
    val actualOutput = GenreHelper.transformToGenreIDs(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreIDs_withNullList_returnEmptyList() {
    val input: List<GenresItem>? = null
    val expectedOutput: List<Int>? = null
    val actualOutput = GenreHelper.transformToGenreIDs(input)
    assertEquals(expectedOutput, actualOutput)
  }
}
