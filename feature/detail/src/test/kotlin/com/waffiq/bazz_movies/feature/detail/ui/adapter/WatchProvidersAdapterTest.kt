package com.waffiq.bazz_movies.feature.detail.ui.adapter

import android.content.Context
import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.feature.detail.databinding.ItemWatchProviderBinding
import com.waffiq.bazz_movies.feature.detail.domain.model.watchproviders.Provider
import com.waffiq.bazz_movies.feature.detail.testutils.BaseAdapterTest
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.robolectric.Shadows.shadowOf

class WatchProvidersAdapterTest : BaseAdapterTest(){

  private val pvd = Provider(
    providerId = 1,
    providerName = "provider name1",
    logoPath = "logo path",
    displayPriority = 1
  )

  private val clickListener: () -> Unit = mockk(relaxed = true)

  private lateinit var adapter: WatchProvidersAdapter

  @Before
  fun setup() {
    super.baseSetup()
    adapter = WatchProvidersAdapter(clickListener)
    recyclerView.adapter = adapter
  }

  @Test
  fun setProviders_whenCalled_updatesListAndNotifiesChanges() {
    val oldList = listOf(pvd)
    val newList = listOf(
      pvd.copy(providerId = 2),
      pvd.copy(providerId = 3)
    )

    adapter.setProviders(oldList)
    assertEquals(1, adapter.itemCount)

    adapter.setProviders(newList)
    assertEquals(2, adapter.itemCount)
    assertEquals(newList, adapter.getListProvider())
  }

  @Test
  fun onBindViewHolder_whenCalled_bindsCorrectForAllData() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val inflater = LayoutInflater.from(context)
    val binding = ItemWatchProviderBinding.inflate(inflater, null, false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setProviders(listOf(pvd))
    adapter.onBindViewHolder(viewHolder, 0)
    assertEquals(pvd.providerName, binding.ivPictureProvider.contentDescription)
  }

  @Test
  fun onCreateViewHolder_whenCalled_createsViewHolderCorrectly() {
    val parent = FrameLayout(context)
    val adapter = WatchProvidersAdapter(clickListener)
    val viewHolder = adapter.onCreateViewHolder(parent, 0)
    assertNotNull(viewHolder)

    // verify ViewHolder is correctly inflated
    assertNotNull(viewHolder.itemView)
    assertSame(parent.context, viewHolder.itemView.context)
  }

  @Test
  fun bind_whenCalled_loadsCorrectImageOrPlaceholder() {
    val binding = ItemWatchProviderBinding.inflate(LayoutInflater.from(context))
    val adapter = WatchProvidersAdapter(clickListener)
    val viewHolder = adapter.ViewHolder(binding)

    val testCases = listOf(
      pvd to "provider name1",
      pvd.copy(providerId = 22, providerName = "provider name2") to "provider name2",
      pvd.copy(providerId = 4, providerName = "provider name3") to "provider name3"
    )

    testCases.forEach { (provider, castId) ->
      viewHolder.bind(provider)

      // expect the content description has correct name
      assertEquals(castId, binding.ivPictureProvider.contentDescription)
    }
  }

  @Test
  fun onBindViewHolder_whenClicked_callsNavigator() {
    val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
    val binding = ItemWatchProviderBinding.inflate(inflater, FrameLayout(inflater.context), false)
    val viewHolder = adapter.ViewHolder(binding)

    adapter.setProviders(listOf(pvd))
    adapter.onBindViewHolder(viewHolder, 0)

    // perform click
    binding.ivPictureProvider.performClick()

    // wait UI
    shadowOf(Looper.getMainLooper()).idle()

    verify(exactly = 1) { clickListener.invoke() }
  }

  @Test
  fun areContentsTheSame_whenFilePathIsSame_returnsTrue() {
    val oldItem = pvd
    val newItem = pvd // same content

    val diffCallback =
      WatchProvidersAdapter(clickListener).DiffCallback(listOf(oldItem), listOf(newItem))
    assertTrue(diffCallback.areContentsTheSame(0, 0))
  }

  @Test
  fun areContentsTheSame_whenDifferentFilePath_returnsFalse() {
    val oldItem = pvd.copy(providerId = 33)
    val newItem = pvd.copy(providerId = 12) // different content

    val diffCallback =
      WatchProvidersAdapter(clickListener).DiffCallback(listOf(oldItem), listOf(newItem))
    assertFalse(diffCallback.areContentsTheSame(0, 0))
  }
}
