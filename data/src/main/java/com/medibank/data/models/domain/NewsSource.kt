package com.medibank.data.models.domain

data class NewsSource(
    val id: String,
    val name: String,
    val description: String,
    var saved: Boolean = false
)