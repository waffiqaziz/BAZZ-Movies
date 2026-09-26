package com.waffiq.bazz_movies.core.database.data.model.v1

import kotlinx.serialization.Serializable

@Serializable
data class BackupPayloadV1(
  val version: Int,
  val createdAt: Long,
  val appVersion: String,
  val favorites: List<FavoriteBackupEntryV1>,
)
