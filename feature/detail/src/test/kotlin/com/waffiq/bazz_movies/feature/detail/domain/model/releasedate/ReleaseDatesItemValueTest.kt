package com.waffiq.bazz_movies.feature.detail.domain.model.releasedate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseDatesItemValueTest {

  @Test
  fun createReleaseDatesItemValue_withAllNullValues_shouldCreateInstance() {
    val item = ReleaseDatesItemValue()

    assertNull(item.descriptors)
    assertNull(item.note)
    assertNull(item.type)
    assertNull(item.iso6391)
    assertNull(item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withAllValidValues_shouldSetPropertiesCorrectly() {
    val descriptors = listOf("descriptor1", "descriptor2")
    val note = "Test note"
    val type = 1
    val iso6391 = "en"
    val certification = "PG-13"
    val releaseDate = "2024-01-01"

    val item = ReleaseDatesItemValue(
      descriptors = descriptors,
      note = note,
      type = type,
      iso6391 = iso6391,
      certification = certification,
      releaseDate = releaseDate
    )

    assertEquals(descriptors, item.descriptors)
    assertEquals(note, item.note)
    assertEquals(type, item.type)
    assertEquals(iso6391, item.iso6391)
    assertEquals(certification, item.certification)
    assertEquals(releaseDate, item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withEmptyDescriptorsList_shouldSetEmptyList() {
    val item = ReleaseDatesItemValue(descriptors = emptyList())

    assertEquals(emptyList<Any?>(), item.descriptors)
  }

  @Test
  fun createReleaseDatesItemValue_withNullDescriptorsInList_shouldAllowNullValues() {
    val descriptors = listOf("valid", null, "another")
    val item = ReleaseDatesItemValue(descriptors = descriptors)

    assertEquals(descriptors, item.descriptors)
  }

  @Test
  fun createReleaseDatesItemValue_withEmptyStringNote_shouldSetEmptyString() {
    val item = ReleaseDatesItemValue(note = "")

    assertEquals("", item.note)
  }

  @Test
  fun createReleaseDatesItemValue_withZeroType_shouldSetZeroType() {
    val item = ReleaseDatesItemValue(type = 0)

    assertEquals(0, item.type)
  }

  @Test
  fun createReleaseDatesItemValue_withNegativeType_shouldSetNegativeType() {
    val item = ReleaseDatesItemValue(type = -1)

    assertEquals(-1, item.type)
  }

  @Test
  fun createReleaseDatesItemValue_withEmptyStringIso6391_shouldSetEmptyString() {
    val item = ReleaseDatesItemValue(iso6391 = "")

    assertEquals("", item.iso6391)
  }

  @Test
  fun createReleaseDatesItemValue_withEmptyStringCertification_shouldSetEmptyString() {
    val item = ReleaseDatesItemValue(certification = "")

    assertEquals("", item.certification)
  }

  @Test
  fun createReleaseDatesItemValue_withEmptyStringReleaseDate_shouldSetEmptyString() {
    val item = ReleaseDatesItemValue(releaseDate = "")

    assertEquals("", item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withMixedNullAndValidValues_shouldSetCorrectly() {
    val descriptors = listOf("test")
    val item = ReleaseDatesItemValue(
      descriptors = descriptors,
      note = null,
      type = 2,
      iso6391 = null,
      certification = "R",
      releaseDate = null
    )

    assertEquals(descriptors, item.descriptors)
    assertNull(item.note)
    assertEquals(2, item.type)
    assertNull(item.iso6391)
    assertEquals("R", item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterDescriptors_shouldSetOnlyDescriptors() {
    val descriptors = listOf("single")
    val item = ReleaseDatesItemValue(descriptors = descriptors)

    assertEquals(descriptors, item.descriptors)
    assertNull(item.note)
    assertNull(item.type)
    assertNull(item.iso6391)
    assertNull(item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterNote_shouldSetOnlyNote() {
    val note = "Single note"
    val item = ReleaseDatesItemValue(note = note)

    assertNull(item.descriptors)
    assertEquals(note, item.note)
    assertNull(item.type)
    assertNull(item.iso6391)
    assertNull(item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterType_shouldSetOnlyType() {
    val item = ReleaseDatesItemValue(type = 3)

    assertNull(item.descriptors)
    assertNull(item.note)
    assertEquals(3, item.type)
    assertNull(item.iso6391)
    assertNull(item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterIso6391_shouldSetOnlyIso6391() {
    val item = ReleaseDatesItemValue(iso6391 = "fr")

    assertNull(item.descriptors)
    assertNull(item.note)
    assertNull(item.type)
    assertEquals("fr", item.iso6391)
    assertNull(item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterCertification_shouldSetOnlyCertification() {
    val item = ReleaseDatesItemValue(certification = "NC-17")

    assertNull(item.descriptors)
    assertNull(item.note)
    assertNull(item.type)
    assertNull(item.iso6391)
    assertEquals("NC-17", item.certification)
    assertNull(item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withSingleParameterReleaseDate_shouldSetOnlyReleaseDate() {
    val item = ReleaseDatesItemValue(releaseDate = "2024-12-31")

    assertNull(item.descriptors)
    assertNull(item.note)
    assertNull(item.type)
    assertNull(item.iso6391)
    assertNull(item.certification)
    assertEquals("2024-12-31", item.releaseDate)
  }

  @Test
  fun createReleaseDatesItemValue_withLargeDescriptorsList_shouldSetLargeList() {
    val descriptors = (1..100).map { "descriptor$it" }
    val item = ReleaseDatesItemValue(descriptors = descriptors)

    assertEquals(descriptors, item.descriptors)
  }

  @Test
  fun createReleaseDatesItemValue_withMaxIntType_shouldSetMaxInt() {
    val item = ReleaseDatesItemValue(type = Int.MAX_VALUE)

    assertEquals(Int.MAX_VALUE, item.type)
  }

  @Test
  fun createReleaseDatesItemValue_withMinIntType_shouldSetMinInt() {
    val item = ReleaseDatesItemValue(type = Int.MIN_VALUE)

    assertEquals(Int.MIN_VALUE, item.type)
  }
}
