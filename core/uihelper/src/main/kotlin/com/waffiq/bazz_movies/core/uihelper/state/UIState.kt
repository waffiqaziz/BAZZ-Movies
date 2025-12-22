package com.waffiq.bazz_movies.core.uihelper.state

/**
 * A unified representation of UI state for observing asynchronous data flows.
 *
 * This state model is intended to be exposed from a ViewModel and observed by the UI
 * layer to render consistent states such as loading indicators, content, and errors.
 *
 * ### State contract
 * - [Idle] represents the initial state before any action is triggered.
 * - [Loading] indicates an ongoing operation and should be used to display progress.
 * - [Success] contains the successfully loaded data and should be rendered by the UI.
 * - [Error] represents a failure that should be communicated to the user.
 *
 * The [isLoading] extension provides a convenient and explicit way for the UI
 * to react to loading state without type checks.
 *
 * @param T the type of data delivered on a successful state.
 */
sealed interface UIState<out T> {
  data object Idle : UIState<Nothing>
  data object Loading : UIState<Nothing>
  data class Success<out T>(val data: T) : UIState<T>
  data class Error(val message: String) : UIState<Nothing>
}

/**
 * Indicates whether this [UIState] represents an active loading operation.
 */
val UIState<*>.isLoading: Boolean
  get() = this is UIState.Loading
