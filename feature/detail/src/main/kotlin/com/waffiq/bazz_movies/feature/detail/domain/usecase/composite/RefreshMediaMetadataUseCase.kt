package com.waffiq.bazz_movies.feature.detail.domain.usecase.composite

fun interface RefreshMediaMetadataUseCase {
  suspend fun refreshMedia(mediaId: Int, mediaType: String)
}
