package com.waffiq.bazz_movies.feature.detail.domain.model

import com.waffiq.bazz_movies.core.models.MediaCastItem

data class MediaCredits(val cast: List<MediaCastItem>, val crew: List<MediaCrewItem>)
