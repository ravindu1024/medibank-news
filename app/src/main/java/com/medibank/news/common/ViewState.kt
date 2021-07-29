package com.medibank.news.common

/**
 * Represents the state of a view while some action like an api call is going on and when we get a result from it
 */
sealed class ViewState {
    object ShowLoading : ViewState()
    object DismissLoading : ViewState()
    data class ShowError(val message: String = "") : ViewState()
}