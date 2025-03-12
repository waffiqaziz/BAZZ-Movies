package com.waffiq.bazz_movies.feature.search.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.feature.search.utils.SearchHelper.setupRecyclerView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SetupRecyclerViewTest {

  private lateinit var context: Context
  private lateinit var recyclerView: RecyclerView
  private lateinit var pagingAdapter: TestPagingAdapter

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    recyclerView = RecyclerView(context)
    pagingAdapter = TestPagingAdapter()
  }

  @Test
  fun setupRecyclerView_configureRecyclerViewCorrectly() {
    recyclerView.setupRecyclerView(context, pagingAdapter)

    // verify that layoutManager is a LinearLayoutManager with vertical orientation
    val layoutManager = recyclerView.layoutManager
    assertTrue("LayoutManager should be LinearLayoutManager", layoutManager is LinearLayoutManager)
    assertEquals("Orientation should be vertical",
      RecyclerView.VERTICAL,
      (layoutManager as LinearLayoutManager).orientation)

    // verify adapter is a ConcatAdapter (also wraps PagingAdapter with footer)
    val adapter = recyclerView.adapter
    assertTrue("Adapter should be ConcatAdapter", adapter is ConcatAdapter)

    // expect the contents of the ConcatAdapter
    val concatAdapter = adapter as ConcatAdapter
    assertTrue("ConcatAdapter should contain adapters", concatAdapter.itemCount >= 0)

    // expect PagingAdapter is ConcatAdapter
    val adapters = concatAdapter.adapters
    assertTrue("ConcatAdapter should contain our PagingAdapter",
      adapters.any { it is PagingDataAdapter<*, *> })

    // expect LoadStateAdapter in the ConcatAdapter
    assertTrue("ConcatAdapter should contain a LoadStateAdapter",
      adapters.any { it is LoadStateAdapter<*> })
  }

  // simple test PagingAdapter implementation
  private class TestPagingAdapter : PagingDataAdapter<String, TestViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
      val view = View(parent.context)
      return TestViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
      // No implementation needed for the test
    }

    companion object {
      private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
          return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
          return oldItem == newItem
        }
      }
    }
  }

  private class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  @Test
  fun loadingStateAdapter_callsRetryOnPagingAdapter() {
    // use spy of the pagingAdapter to verify retry() is called
    val spyAdapter = spy(pagingAdapter)

    recyclerView.setupRecyclerView(context, spyAdapter)

    // extract the LoadStateAdapter from the ConcatAdapter
    val concatAdapter = recyclerView.adapter as ConcatAdapter
    val loadStateAdapter = concatAdapter.adapters.find { it is LoadStateAdapter<*> } as LoadStateAdapter<*>

    // get retry function using reflection
    // this approach avoids needing to create and click a button
    val loadingStateAdapterClass = loadStateAdapter.javaClass
    val retryField = loadingStateAdapterClass.getDeclaredField("retry")
    retryField.isAccessible = true
    val retryFunction = retryField.get(loadStateAdapter) as Function0<*>

    // trigger retry function directly
    retryFunction.invoke()

    verify(spyAdapter).retry()
  }
}