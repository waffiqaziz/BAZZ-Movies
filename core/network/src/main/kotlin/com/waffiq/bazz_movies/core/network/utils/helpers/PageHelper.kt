package com.waffiq.bazz_movies.core.network.utils.helpers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.waffiq.bazz_movies.core.network.data.remote.pagingsources.GenericPagingSource
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.MediaResponseItem
import com.waffiq.bazz_movies.core.network.utils.common.Constants.PAGE_SIZE

object PageHelper {

  fun createPager(
    apiCall: suspend (Int) -> List<MediaResponseItem>,
  ): Pager<Int, MediaResponseItem> =
    Pager(
      config = PagingConfig(pageSize = PAGE_SIZE),
      pagingSourceFactory = { GenericPagingSource(apiCall) },
    )
}
