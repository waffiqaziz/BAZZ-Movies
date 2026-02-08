package com.waffiq.bazz_movies.feature.detail.domain.model

/**

 * A model class representing the state of operations related to the watchlist and favorite
 * features on the detail page.
 *
 * @property isSuccess Indicates whether the operation (add/delete to watchlist or favorite) was
 *                     successful.
 * @property isDelete Specifies if the current operation is a delete action (true) or an add action
 *                    (false).
 * @property isFavorite Distinguishes between the type of operation:
 *  - true: The operation is related to the favorite feature.
 *  - false: The operation is related to the watchlist feature.
 *
 * This class is used to track the state of the user's actions on a specific post, such as
 * adding/removing it from their watchlist or marking/unmarking it as a favorite.
 */
data class UpdateMediaStateResult(
  val isSuccess: Boolean,
  val isDelete: Boolean,
  val isFavorite: Boolean,
)
