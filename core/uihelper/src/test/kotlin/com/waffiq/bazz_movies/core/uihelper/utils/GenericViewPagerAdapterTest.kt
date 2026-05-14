package com.waffiq.bazz_movies.core.uihelper.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GenericViewPagerAdapterTest {

  private lateinit var fragmentManager: FragmentManager
  private lateinit var lifecycle: Lifecycle
  private lateinit var adapter: GenericViewPagerAdapter

  class TestFragment1 : Fragment()
  class TestFragment2 : Fragment()
  class TestFragment3 : Fragment()

  private val factories = listOf(
    Pair(TestFragment1::class.java) { TestFragment1() },
    Pair(TestFragment2::class.java) { TestFragment2() },
    Pair(TestFragment3::class.java) { TestFragment3() },
  )

  @Before
  fun setup() {
    fragmentManager = mockk(relaxed = true)
    lifecycle = mockk(relaxed = true)
    adapter = GenericViewPagerAdapter(fragmentManager, lifecycle, factories)
  }

  @Test
  fun getItemCount_whenCalled_returnsCorrectNumbers() {
    assertEquals(3, adapter.itemCount)
  }

  @Test
  fun getItemCount_whenNoFragments_returnsZero() {
    val emptyAdapter = GenericViewPagerAdapter(fragmentManager, lifecycle, emptyList())
    assertEquals(0, emptyAdapter.itemCount)
  }

  @Test
  fun createFragment_whenCalled_returnsCorrectType() {
    assertThat(adapter.createFragment(0)).isInstanceOf(TestFragment1::class.java)
    assertThat(adapter.createFragment(1)).isInstanceOf(TestFragment2::class.java)
    assertThat(adapter.createFragment(2)).isInstanceOf(TestFragment3::class.java)
  }

  @Test
  fun createFragment_whenCalled_returnsNewInstanceEachTime() {
    val first = adapter.createFragment(0)
    val second = adapter.createFragment(0)
    assertNotSame(first, second)
  }

  @Test
  fun getItemId_whenCalled_returnsCorrectId() {
    val expectedId1 = TestFragment1::class.java.name.hashCode().toLong()
    val expectedId2 = TestFragment2::class.java.name.hashCode().toLong()
    val expectedId3 = TestFragment3::class.java.name.hashCode().toLong()

    assertEquals(expectedId1, adapter.getItemId(0))
    assertEquals(expectedId2, adapter.getItemId(1))
    assertEquals(expectedId3, adapter.getItemId(2))
  }

  @Test
  fun getItemId_whenSamePosition_returnsSameId() {
    assertEquals(adapter.getItemId(0), adapter.getItemId(0))
  }

  @Test
  fun getItemId_withDifferentFragments_generatesUniqueIds() {
    val id0 = adapter.getItemId(0)
    val id1 = adapter.getItemId(1)
    val id2 = adapter.getItemId(2)

    assertNotEquals(id0, id1)
    assertNotEquals(id1, id2)
    assertNotEquals(id0, id2)
  }

  @Test
  fun containsItem_whenItemExists_returnsTrue() {
    val itemId = TestFragment1::class.java.name.hashCode().toLong()
    assertTrue(adapter.containsItem(itemId))
  }

  @Test
  fun containsItem_whenCalled_returnsTrueForAllFragments() {
    assertTrue(adapter.containsItem(TestFragment1::class.java.name.hashCode().toLong()))
    assertTrue(adapter.containsItem(TestFragment2::class.java.name.hashCode().toLong()))
    assertTrue(adapter.containsItem(TestFragment3::class.java.name.hashCode().toLong()))
  }

  @Test
  fun containsItem_whenItemDoesNotExist_returnsFalse() {
    val nonExistentId = "com.example.NonExistentFragment".hashCode().toLong()
    assertFalse(adapter.containsItem(nonExistentId))
  }

  @Test
  fun containsItem_whenRandomId_returnsFalse() {
    assertFalse(adapter.containsItem(999999999L))
  }

  @Test
  fun adapter_whenCalled_shouldHandlesSingleFragment() {
    val singleAdapter = GenericViewPagerAdapter(
      fragmentManager,
      lifecycle,
      listOf(Pair(TestFragment1::class.java) { TestFragment1() }),
    )

    assertEquals(1, singleAdapter.itemCount)
    assertThat(singleAdapter.createFragment(0)).isInstanceOf(TestFragment1::class.java)
    assertTrue(singleAdapter.containsItem(TestFragment1::class.java.name.hashCode().toLong()))
  }

  @Test
  fun adapter_whenCalled_maintainsConsistentItemIds() {
    val itemId = adapter.getItemId(1)
    assertEquals(TestFragment2::class.java.name.hashCode().toLong(), itemId)
    assertTrue(adapter.containsItem(itemId))
  }

  @Test
  fun genericViewPagerAdapter_withSameClassTwice_haveSameItemId() {
    val multiAdapter = GenericViewPagerAdapter(
      fragmentManager,
      lifecycle,
      listOf(
        Pair(TestFragment1::class.java) { TestFragment1() },
        Pair(TestFragment1::class.java) { TestFragment1() },
      ),
    )

    // same class
    assertEquals(multiAdapter.getItemId(0), multiAdapter.getItemId(1))
  }
}
