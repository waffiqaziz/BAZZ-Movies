package com.waffiq.bazz_movies.core.database.utils

import java.security.MessageDigest

/** Lowercase hex SHA-256 digest of this string's UTF-8 bytes. */
fun String.sha256(): String {
  // SHA-256 hash of the compact JSON encoding of [BackupPayload].
  // - Used for detects accidental corruption and casual edits
  // - It is NOT a cryptographic signature.
  //
  // Note:
  // A determined user who knows the format can modify the file and recompute a valid checksum.
  // For a user-exportable backup file this is an acceptable tradeoff: we have no safe place to
  // store a secret key that would survive cross-device restore.

  val bytes = MessageDigest.getInstance("SHA-256").digest(toByteArray(Charsets.UTF_8))
  return bytes.joinToString(separator = "") { byte -> "%02x".format(byte) }
}
