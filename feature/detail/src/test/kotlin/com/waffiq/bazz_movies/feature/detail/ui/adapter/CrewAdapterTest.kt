package com.waffiq.bazz_movies.feature.detail.ui.adapter

import com.waffiq.bazz_movies.feature.detail.domain.model.MediaCrewItem
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CrewAdapterTest : BaseAdapterTest() {

  @Test
  fun areItemsTheSame_whenItemsIsSame_returnsTrue() {
    val oldItem = MediaCrewItem(id = 1, creditId = "1")
    val newItem = MediaCrewItem(id = 1, creditId = "1")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertTrue(diffCallback.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenIdDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 1, creditId = "1")
    val newItem = MediaCrewItem(id = 2, creditId = "1")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenCreditIdDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 1, creditId = "1")
    val newItem = MediaCrewItem(id = 1, creditId = "2")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areItemsTheSame_whenAllDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 1, creditId = "1")
    val newItem = MediaCrewItem(id = 2, creditId = "2")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areItemsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenContentIsSame_returnsTrue() {
    val oldItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep")
    val newItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertTrue(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenDepartmentDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep")
    val newItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep1")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenNameDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep")
    val newItem = MediaCrewItem(id = 1, name = "Different Name", department = "dep")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }

  @Test
  fun areContentsTheSame_whenAllDifferent_returnsFalse() {
    val oldItem = MediaCrewItem(id = 2, name = "Test Name2", department = "dep1")
    val newItem = MediaCrewItem(id = 1, name = "Test Name", department = "dep2")

    val diffCallback = CrewAdapter.CrewDiffCallback()
    assertFalse(diffCallback.areContentsTheSame(oldItem, newItem))
  }
}
