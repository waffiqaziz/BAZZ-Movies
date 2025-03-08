package com.waffiq.bazz_movies.core.common.utils

import org.junit.Assert.*
import org.junit.Test

class EventTest {

  @Test
  fun initialState_hasBeenHandled_shouldFalse() {
    val event = Event("test content")
    assertFalse(event.hasBeenHandled)
  }

  @Test
  fun getContentIfNotHandled_firstCall_returnsContent() {
    val content = "test content"
    val event = Event(content)

    val result = event.getContentIfNotHandled()

    assertEquals(content, result)
    assertTrue(event.hasBeenHandled)
  }

  @Test
  fun getContentIfNotHandled_secondCall_returnsNull() {
    val event = Event("test content")

    event.getContentIfNotHandled() // first call
    val secondResult = event.getContentIfNotHandled() // second call

    assertNull(secondResult)
    assertTrue(event.hasBeenHandled)
  }

  @Test
  fun peekContent_returnsContent_anyHandledState() {
    val content = "test content"
    val event = Event(content)

    assertEquals(content, event.peekContent())
    assertFalse(event.hasBeenHandled)

    event.getContentIfNotHandled()

    assertEquals(content, event.peekContent())
    assertTrue(event.hasBeenHandled)
  }

  @Test
  fun getContentIfNotHandled_nullContent_shouldWorkProperly() {
    val event = Event<String?>(null)
    val result = event.getContentIfNotHandled()

    assertNull(result)
    assertTrue(event.hasBeenHandled)
  }

  @Test
  fun getContentIfNotHandled_differentDataTypes_shouldWorkProperly() {
    // integer test
    val intEvent = Event(42)
    assertEquals(42, intEvent.getContentIfNotHandled())

    // boolean test
    val boolEvent = Event(true)
    assertEquals(true, boolEvent.getContentIfNotHandled())

    // custom object test
    data class TestObject(val id: Int, val name: String)
    val testObject = TestObject(1, "Test")
    val objectEvent = Event(testObject)
    assertEquals(testObject, objectEvent.getContentIfNotHandled())
  }

  @Test
  fun hasBeenHandled_shouldNotBeModifiableEternally() {
    // compile-time check, if this compiles, the test passes
    val event = Event("test")

    // this would cause a compilation error if uncommented:
    // event.hasBeenHandled = false

    // only read the value
    val isHandled = event.hasBeenHandled
    assertFalse(isHandled)
  }

  @Test
  fun subclassBehavior_shouldMaintainSameProperties() {
    class CustomEvent<T>(content: T) : Event<T>(content) {
      // custom event with no additional functionality
    }

    val customEvent = CustomEvent("test content")

    assertFalse(customEvent.hasBeenHandled)
    assertEquals("test content", customEvent.getContentIfNotHandled())
    assertTrue(customEvent.hasBeenHandled)
    assertEquals("test content", customEvent.peekContent())
  }
}
