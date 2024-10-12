package com.waffiq.bazz_movies.core.navigation

import com.waffiq.bazz_movies.core.domain.model.ResultItem

interface DetailNavigator {
  fun openDetails(resultItem: ResultItem)
}