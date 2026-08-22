package com.waffiq.bazz_movies.core.utils

import com.waffiq.bazz_movies.core.models.GenresItem
import com.waffiq.bazz_movies.core.utils.GenreHelper.getGenreName
import com.waffiq.bazz_movies.core.utils.GenreHelper.toListGenreIds
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreIdsToJoinName
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformListGenreToJoinString
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreCode
import com.waffiq.bazz_movies.core.utils.GenreHelper.transformToGenreIDs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GenreHelperTest {

  @Test
  fun getGenreName_withValidGenreId_returnsCorrectName() {
    assertEquals("Action", getGenreName(28)) // valid id
    assertEquals("", getGenreName(313213211)) // invalid id
  }

  @Test
  fun transformListGenreIdsToJoinName_withValidGenreNames_returnsCorrectString() {
    val input = listOf(28, 12, 16)
    val expectedOutput = "Action, Adventure, Animation"
    val actualOutput = transformListGenreIdsToJoinName(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreIdsToJoinName_withEmptyList_returnsEmptyString() {
    val input = emptyList<Int>()
    val actualOutput = transformListGenreIdsToJoinName(input)
    assertNull(actualOutput)
  }

  @Test
  fun transformListGenreIdsToJoinName_withValidGenreItems_handlesInvalidGenreId() {
    val input = listOf(999) // Non-existent genre ID
    val actualOutput = transformListGenreIdsToJoinName(input)
    assertNull(actualOutput)
  }

  @Test
  fun transformToGenreCode_withNullList_returnsCorrectCode() {
    val input = listOf("Action", "Comedy", "Romance")
    val expectedOutput = "28|35|10749"
    val actualOutput = transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreCode_withValidGenreItems_returnEmptyList() {
    val input = emptyList<String>()
    val expectedOutput = ""
    val actualOutput = transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreCode_with_handleInvalidGenreName() {
    val input = listOf("NonExistentGenre")
    val expectedOutput = ""
    val actualOutput = transformToGenreCode(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withNullList_returnsCorrectString() {
    val input = listOf(
      GenresItem("Action", 28),
      GenresItem("Mystery", 9648),
    )
    val expectedOutput = "Action, Mystery"
    val actualOutput = transformListGenreToJoinString(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withNullList_returnsNull() {
    val input: List<GenresItem>? = null
    val expectedOutput: String? = null
    val actualOutput = transformListGenreToJoinString(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withEmptyList_returnsNull() {
    val input = emptyList<GenresItem>()
    val actualOutput = transformListGenreToJoinString(input)
    assertNull(actualOutput)
  }

  @Test
  fun transformListGenreToJoinString_withNullGenresItem_returnsNull() {
    val input = listOf(null)
    val actualOutput = transformListGenreToJoinString(input)
    assertNull(actualOutput)
  }

  @Test
  fun toListGenreIds_stringValue_returnsCorrectGenreId(){
    assertEquals("Action".toListGenreIds(),listOf(28))
    assertEquals("Fantasy, History".toListGenreIds(),listOf(14,36))
    assertEquals("".toListGenreIds(),emptyList<Int>())
    assertEquals("222222222".toListGenreIds(),emptyList<Int>())
  }

  @Test
  fun transformToGenreIDs_withValidGenreList_returnsCorrectIds() {
    val input = listOf(
      GenresItem("News", 10763),
      GenresItem("Talk", 10767),
      GenresItem("Invalid", null),
    )
    val expectedOutput = listOf(10763, 10767, 0) // IDs, null mapped to 0
    val actualOutput = transformToGenreIDs(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreIDs_withNullList_returnsEmptyList() {
    val input: List<GenresItem>? = null
    val expectedOutput: List<Int>? = null
    val actualOutput = transformToGenreIDs(input)
    assertEquals(expectedOutput, actualOutput)
  }

  @Test
  fun transformToGenreIDs_withNullGenresItem_returnsEmptyList() {
    val input = listOf(null)
    val actualOutput = transformToGenreIDs(input)
    assertEquals(listOf(0), actualOutput)
  }
}
