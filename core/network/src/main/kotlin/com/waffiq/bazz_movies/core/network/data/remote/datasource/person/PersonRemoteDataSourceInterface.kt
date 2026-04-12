package com.waffiq.bazz_movies.core.network.data.remote.datasource.person

import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.CombinedCreditResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.DetailPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ExternalIDPersonResponse
import com.waffiq.bazz_movies.core.network.data.remote.responses.tmdb.person.ImagePersonResponse
import com.waffiq.bazz_movies.core.network.utils.result.NetworkResult
import kotlinx.coroutines.flow.Flow

interface PersonRemoteDataSourceInterface {
  fun getPersonDetails(id: Int): Flow<NetworkResult<DetailPersonResponse>>
  fun getPersonImages(id: Int): Flow<NetworkResult<ImagePersonResponse>>
  fun getPersonCredits(id: Int): Flow<NetworkResult<CombinedCreditResponse>>
  fun getPersonExternalIds(id: Int): Flow<NetworkResult<ExternalIDPersonResponse>>
}
