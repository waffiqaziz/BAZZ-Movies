package com.waffiq.bazz_movies.feature.detail.utils.helpers

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.utils.helpers.CrewJobHelper.extractCrewDisplayNames
import org.junit.Assert.assertEquals
import org.junit.Test

class CrewJobHelperTest {

  private fun crew(job: String, name: String?) = MediaCrewItem(job = job, name = name)

  @Test
  fun detailCrew_withValidCrewList_returnsCorrectNamesAndRoles() {
    val crew = listOf(
      crew("Director", "John Doe"),
      crew("Writer", "Jane Smith"),
      crew("Producer", "Bob Wilson"),
    )

    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Director", "Writer"), displayNames)
    assertEquals(listOf("John Doe", "Jane Smith"), joinedNames)
  }

  @Test
  fun detailCrew_withEmptyNullName_filtersOut() {
    val crew = listOf(
      crew("Writer", null),
      crew("Writer", ""),
      crew("Writer", "Jane Smith"),
      crew("Writer", "Bob Jones"),
    )
    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Writer"), displayNames)
    assertEquals(listOf("Jane Smith, Bob Jones"), joinedNames)
  }

  @Test
  fun detailCrew_empty_returnsEmpty() {
    val (displayNames, joinedNames) = extractCrewDisplayNames(emptyList())

    assertEquals(emptyList<String>(), displayNames)
    assertEquals(emptyList<String>(), joinedNames)
  }

  @Test
  fun detailCrew_withMultipleNamesSameJob_returnsJoinedNames() {
    val crew = listOf(
      MediaCrewItem(job = "Writer", name = "Jane Smith"),
      MediaCrewItem(job = "Writer", name = "John Doe"),
    )
    val (displayNames, joinedNames) = extractCrewDisplayNames(crew)

    assertEquals(listOf("Writer"), displayNames)
    assertEquals(listOf("Jane Smith, John Doe"), joinedNames)
  }
}
