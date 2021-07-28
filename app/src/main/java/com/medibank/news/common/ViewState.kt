package com.medibank.news.common

sealed class ViewState{
    object ShowLoading: ViewState()
    object DismissLoading: ViewState()
    data class ShowError(val message: String = ""): ViewState()
}