package com.waffiq.bazz_movies.core.uihelper.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class GenericViewPagerAdapterTest {

  private lateinit var fragmentManager: FragmentManager
  private lateinit var lifecycle: Lifecycle
  private lateinit var fragments: List<Fragment>
  private lateinit var adapter: GenericViewPagerAdapter

  private lateinit var fragment1: TestFragment1
  private lateinit var fragment2: TestFragment2
  private lateinit var fragment3: TestFragment3

  class TestFragment1 : Fragment()
  class TestFragment2 : Fragment()
  class TestFragment3 : Fragment()

  @Before
  fun setup() {
    fragmentManager = mockk(relaxed = true)
    lifecycle = mockk(relaxed = true)

    // real fragment instances
    fragment1 = TestFragment1()
    fragment2 = TestFragment2()
    fragment3 = TestFragment3()

    fragments = listOf(fragment1, fragment2, fragment3)
    adapter = GenericViewPagerAdapter(fragmentManager, lifecycle, fragments)
  }

  @Test
  fun getItemCount_whenCalled_returnsCorrectNumbers() {
    val count = adapter.itemCount
    assertEquals(3, count)
  }

  @Test
  fun getItemCount_whenNoFragments_returnsZero() {
    val emptyAdapter = GenericViewPagerAdapter(fragmentManager, lifecycle, emptyList())
    val count = emptyAdapter.itemCount

    assertEquals(0, count)
  }

  @Test
  fun createFragment_whenCalled_returnsCorrectFragment() {
    val fragmentAt0 = adapter.createFragment(0)
    val fragmentAt1 = adapter.createFragment(1)
    val fragmentAt2 = adapter.createFragment(2)

    assertEquals(fragment1, fragmentAt0)
    assertEquals(fragment2, fragmentAt1)
    assertEquals(fragment3, fragmentAt2)
  }

  @Test
  fun getItemId_whenCalled_returnsCorrectId() {
    val expectedId1 = "TestFragment1".hashCode().toLong()
    val expectedId2 = "TestFragment2".hashCode().toLong()
    val expectedId3 = "TestFragment3".hashCode().toLong()

    val id0 = adapter.getItemId(0)
    val id1 = adapter.getItemId(1)
    val id2 = adapter.getItemId(2)

    assertEquals(expectedId1, id0)
    assertEquals(expectedId2, id1)
    assertEquals(expectedId3, id2)
  }

  @Test
  fun getItemId_whenSamePosition_returnsSameId() {
    val firstCall = adapter.getItemId(0)
    val secondCall = adapter.getItemId(0)

    assertEquals(firstCall, secondCall)
  }

  @Test
  fun containsItem_whenItemExists_returnsTrue() {
    val itemId = "TestFragment1".hashCode().toLong()
    val contains = adapter.containsItem(itemId)

    assertTrue(contains)
  }

  @Test
  fun containsItem_whenCalled_returnsTrueForAllFragments() {
    val itemId1 = "TestFragment1".hashCode().toLong()
    val itemId2 = "TestFragment2".hashCode().toLong()
    val itemId3 = "TestFragment3".hashCode().toLong()

    assertTrue(adapter.containsItem(itemId1))
    assertTrue(adapter.containsItem(itemId2))
    assertTrue(adapter.containsItem(itemId3))
  }

  @Test
  fun containsItem_whenItemDoesNotExist_returnsFalse() {
    val nonExistentItemId = "NonExistentFragment".hashCode().toLong()
    val contains = adapter.containsItem(nonExistentItemId)

    assertFalse(contains)
  }

  @Test
  fun containsItem_whenRandomId_returnsFalse() {
    val randomId = 999999999L
    val contains = adapter.containsItem(randomId)

    assertFalse(contains)
  }

  @Test
  fun onDetachedFromRecyclerView_callsSuperCompletes_shouldSuccessfully() {
    val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    val viewPager2 = androidx.viewpager2.widget.ViewPager2(context)
    val testAdapter = GenericViewPagerAdapter(fragmentManager, lifecycle, fragments)

    viewPager2.adapter = testAdapter
    viewPager2.adapter = null

    // test should without exception, and
    // super.onDetachedFromRecyclerView() is called successful
    assertTrue(true)
  }


  @Test
  fun adapter_whenCalled_shouldHandlesSingleFragment() {
    val singleFragment = TestFragment1()
    val singleAdapter = GenericViewPagerAdapter(
      fragmentManager,
      lifecycle,
      listOf(singleFragment)
    )

    assertEquals(1, singleAdapter.itemCount)
    assertEquals(singleFragment, singleAdapter.createFragment(0))
    assertTrue(
      singleAdapter.containsItem(
        "TestFragment1".hashCode().toLong()
      )
    )
  }

  @Test
  fun getItemId_withDifferentFragments_generatesUniqueIds() {
    val id0 = adapter.getItemId(0)
    val id1 = adapter.getItemId(1)
    val id2 = adapter.getItemId(2)

    assertTrue(id0 != id1)
    assertTrue(id1 != id2)
    assertTrue(id0 != id2)
  }

  @Test
  fun adapter_whenCalled_maintainsConsistentItemIds() {
    val position = 1
    val expectedId = "TestFragment2".hashCode().toLong()
    val itemId = adapter.getItemId(position)

    assertEquals(expectedId, itemId)
    assertTrue(adapter.containsItem(itemId))
  }

  @Test
  fun genericViewPagerAdapter_withMultipleFragments_haveSameItemId() {
    val fragment1Instance1 = TestFragment1()
    val fragment1Instance2 = TestFragment1()
    val multiAdapter = GenericViewPagerAdapter(
      fragmentManager,
      lifecycle,
      listOf(fragment1Instance1, fragment1Instance2)
    )

    val id0 = multiAdapter.getItemId(0)
    val id1 = multiAdapter.getItemId(1)

    // both have the same ID cuz same class
    assertEquals(id0, id1)
  }
}